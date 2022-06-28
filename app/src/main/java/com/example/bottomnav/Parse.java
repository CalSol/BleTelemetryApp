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
    private HashMap<String, StructDecoder> structDecoderRepo = new HashMap<>();
    private HashMap<String, Optional<PrimitiveDecoder>> primitiveDecoderRepo = new HashMap<>();
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

        parseComments(comments);
        for (IASTNode child : children) {
            if (child instanceof CPPASTSimpleDeclaration) {
                CPPASTSimpleDeclaration declaration = (CPPASTSimpleDeclaration) child;
                if (declaration.getDeclSpecifier() instanceof CPPASTCompositeTypeSpecifier) {
                    CPPASTCompositeTypeSpecifier compSpec =
                            (CPPASTCompositeTypeSpecifier) declaration.getDeclSpecifier();
                    CPPASTName name = (CPPASTName) compSpec.getName();
                    storeStuct(compSpec.getDeclarations(false),
                            name.getRawSignature());
                } else {
                    storeConst(declaration);
                }
            }
        }
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

    private void storeConst(CPPASTSimpleDeclaration declaration) throws Exception {
        Optional<Components> data = Components.getComponents(declaration);
        if (data.isPresent() && data.get().init.isPresent()) {
            Components comp = data.get();
            CPPASTLiteralExpression value =
                    (CPPASTLiteralExpression) comp.init.get().getInitializerClause();
            ConstContents contents = new ConstContents(comp.name, value.getRawSignature(),
                    comp.typeQualifier, comp.type);
            Optional<PrimitiveDecoder> decoder = DataDecoder.getPrimativeDecoder(contents.payLoadDataType);
            primitiveDecoderRepo.put(contents.name, decoder);
            canIdToName.put(Integer.parseInt(contents.value), contents.name);
        }
    }

    private void storeStuct(IASTDeclaration[] declarations, String name) throws Exception {
        ArrayList<VariableContents> variables = new ArrayList();
        for (IASTDeclaration element : declarations) {
            CPPASTSimpleDeclaration declaration = (CPPASTSimpleDeclaration) element;
            Optional<Components> data = Components.getComponents(declaration);
            if (data.isPresent() && !data.get().init.isPresent()) {
                Components comp = data.get();
                VariableContents contents = new VariableContents(comp.name, comp.type);
                variables.add(contents);
            }
        }
        StructDecoder decoder = new StructDecoder(variables);
        structDecoderRepo.put(name, decoder);
    }

    public String decode(Integer canId, byte[] payload) {

        return "msg;";
    }

    private DataDecoder getDecoder(Integer canID) {
        if ()
    }

    private PrimitiveDecoder getPrimitiveDecoder(Integer canID) {
        
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
