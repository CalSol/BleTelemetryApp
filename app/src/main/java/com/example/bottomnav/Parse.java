package com.example.bottomnav;

import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.gnu.cpp.GPPLanguage;
import org.eclipse.cdt.core.index.IIndex;
import org.eclipse.cdt.core.parser.*;
import org.eclipse.cdt.internal.core.dom.parser.cpp.*;

import java.io.*;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parse {
    private HashMap<String, Optional<DataDecoder>> decoderRepo= new HashMap<>();
    private HashMap<String, String> canNameToStruct = new HashMap<>();
    private HashMap<Integer, String> canIdToName = new HashMap<>();

    public static Parse parseTextFile(String fileName) throws Exception {
        char[] code = Parse.OpenTextFile(fileName);
        return new Parse(code);
    }

    public Parse(char[] code) throws Exception {
        IASTTranslationUnit translationUnit = getIASTTranslationUnit(code);
        IASTNode[] comments = translationUnit.getComments();
        IASTNode[] children = translationUnit.getChildren();

        for (IASTNode child : children) {
            if (child instanceof CPPASTSimpleDeclaration) {
                CPPASTSimpleDeclaration declaration = (CPPASTSimpleDeclaration) child;
                if (declaration.getDeclSpecifier() instanceof CPPASTCompositeTypeSpecifier) {
                    CPPASTCompositeTypeSpecifier compSpec =
                            (CPPASTCompositeTypeSpecifier) declaration.getDeclSpecifier();
                    CPPASTName name = (CPPASTName) compSpec.getName();
                    storeStructDecoder(compSpec.getDeclarations(false),
                            name.getRawSignature());
                } else {
                    storeConstDecoder(declaration);
                }
            }
        }
        parseComments(comments);
    }

    private void parseComments(IASTNode[] comments) {
        Pattern pattern1 = Pattern.compile("@canPayloadStruct\\s+(\\S+)\\s*=\\s*(\\S+)");
        for (IASTNode comment : comments) {
            Matcher matcher1 = pattern1.matcher(comment.getRawSignature());
            if (matcher1.find()) { // associate ID to struct
                String canName = matcher1.group(1);
                String structName = matcher1.group(2);
                canNameToStruct.put(canName, structName);
            }
        }
    }

    private void storeConstDecoder(CPPASTSimpleDeclaration declaration) throws Exception {
        Optional<Components> data = Components.getComponents(declaration);
        if (data.isPresent() && data.get().init.isPresent()) {
            Components comp = data.get();
            CPPASTLiteralExpression value =
                    (CPPASTLiteralExpression) comp.init.get().getInitializerClause();
            VariableContents contents = new VariableContents(comp.name, value.getRawSignature(),
                    comp.typeQualifier, comp.type);
            Optional<DataDecoder> decoder = DataDecoder.createPrimitiveDecoder(contents);
            decoderRepo.put(contents.name, decoder);
            canIdToName.put(Integer.decode(contents.value), contents.name);
        }
    }

    private void storeStructDecoder(IASTDeclaration[] declarations, String name) throws Exception {
        ArrayList<VariableContents> variables = new ArrayList();
        for (IASTDeclaration element : declarations) {
            CPPASTSimpleDeclaration declaration = (CPPASTSimpleDeclaration) element;
            Optional<Components> data = Components.getComponents(declaration);
            if (data.isPresent() && !data.get().init.isPresent()) {
                Components comp = data.get();
                VariableContents contents = new VariableContents(comp.name, null, null, comp.type);
                variables.add(contents);
            }
        }
        Optional<DataDecoder> decoder = Optional.of((DataDecoder) new StructDecoder(variables));
        decoderRepo.put(name, decoder);
    }

    public Optional<String> decode(int canId, byte[] payload) {
        String canName = canIdToName.get(canId);
        Optional<DataDecoder> decoder = getDecoder(canName);
        if (decoder.isPresent()) {
            return decoder.get().decode(canId, payload);
        } return null;
    }

    public Optional<DataDecoder> getDecoder(String name) {
        if (canNameToStruct.containsKey(name)&& decoderRepo.containsKey(canNameToStruct.get(name))) {
            return decoderRepo.get(canNameToStruct.get(name));
        } else if (decoderRepo.containsKey(name)) {
            return decoderRepo.get(name);
        } return Optional.empty();
    }

    public Optional<DataDecoder> getDecoder(Integer canID) {
        return getDecoder(canIdToName.get(canID));
    }

    public VariableContents getConstContents(String name) {
        return ((PrimitiveDecoder) decoderRepo.get(name).get()).contents;
    }

    public ArrayList<VariableContents> getStructContents(String name) {
        return ((StructDecoder) getDecoder(name).get()).variables;
    }

    private static char[] OpenTextFile(String fileName) throws IOException {
        //Source: https://www.oreilly.com/content/how-to-convert-an-inputstream-to-a-string/
        InputStream is = Parse.class.getClassLoader().getResourceAsStream(fileName);
        if (is != null) {
            ByteArrayOutputStream barOutStream = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int length;
            while ((length = is.read(buf)) != -1) {
                barOutStream.write(buf, 0, length);
            }
            return barOutStream.toString().toCharArray();
        } else {
            throw new FileNotFoundException();
        }
    }

    private static IASTTranslationUnit getIASTTranslationUnit(char[] code) throws Exception {
        FileContent fc = FileContent.create("ParseFile", code);
        Map<String, String> macroDefinitions = new HashMap();
        String[] includeSearchPaths = new String[0];
        IScannerInfo si = new ScannerInfo(macroDefinitions, includeSearchPaths);
        IncludeFileContentProvider ifcp = IncludeFileContentProvider.getEmptyFilesProvider();
        IIndex idx = null;
        int options = 8;
        IParserLogService log = new DefaultLogService();
        return GPPLanguage.getDefault().getASTTranslationUnit(fc, si, ifcp, (IIndex)idx, options, log);
    }
}
