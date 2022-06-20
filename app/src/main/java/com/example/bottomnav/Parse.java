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
    private HashMap<String, ConstContents> constRepo = new HashMap<>();
    private HashMap<String, ArrayList<VariableContents>> structRepo = new HashMap<>();
    private HashMap<Integer, String> canIDToStruct = new HashMap<>();
    private HashMap<String, Integer> canNameToId = new HashMap<>();
    private HashMap<Integer, String> canIdToName = new HashMap<>();
    private HashMap<Integer, Optional<DataDecoder>> payloadMap = new HashMap<>();

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
                    storeStuct(compSpec.getDeclarations(false),
                            name.getRawSignature());
                } else {
                    storeConst(declaration);
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
                String idName = matcher1.group(1);
                String structName = matcher1.group(2);
                ArrayList<VariableContents> contents =  structRepo.get(structName);
                canIDToStruct.put(canNameToId.get(idName), structName);
                payloadMap.put(canNameToId.get(matcher1.group(1)),Optional.of(new StructDecoder(contents)));
            }
        }
    }

    private void storeConst(CPPASTSimpleDeclaration declaration) throws Exception {
        Optional<Components> data = Components.getComponents(declaration);
        if (data.isPresent() && data.get().init.isPresent()) {
            Components comp = data.get();
            CPPASTLiteralExpression value =
                    (CPPASTLiteralExpression) comp.init.get().getInitializerClause();
            ConstContents contents = new ConstContents(comp.name, value, comp.typeQualifier, comp.type);
            constRepo.put(contents.name, contents);
            payloadMap.put(Integer.decode(contents.value),
                    DataDecoder.getPrimativeDecoder(contents.payLoadDataType));
            canNameToId.put(contents.name, Integer.decode(contents.value));
            canIdToName.put(Integer.decode(contents.value), contents.name);
        }
    }

    private void storeStuct(IASTDeclaration[] declarations, String name) throws Exception {
        ArrayList<VariableContents> struct = new ArrayList();
        for (IASTDeclaration element : declarations) {
            CPPASTSimpleDeclaration declaration = (CPPASTSimpleDeclaration) element;
            Optional<Components> data = Components.getComponents(declaration);
            if (data.isPresent() && !data.get().init.isPresent()) {
                Components comp = data.get();
                VariableContents contents = new VariableContents(comp.name, comp.type);
                struct.add(contents);
            }
        }
        structRepo.put(name, struct);
    }

    public String decode(Integer canID, byte[] payload) {
        if (canIDToStruct.containsKey(canID)) {
            return "struct " + canIDToStruct.get(canID) + ", "
                    + payloadMap.get(canID).get().  decode(canID, payload);
        } else {
            return "single " + canIdToName.get(canID) + ", "
                    + payloadMap.get(canID).get().decode(canID, payload);
        }
    }

    public DataDecoder getDecoder(Integer canID) {
        if (payloadMap.get(canID).isPresent()) {
            return payloadMap.get(canID).get();
        }
        return null;
    }

    public ConstContents getConstContents(String key) {
        return constRepo.get(key);
    }

    public ConstContents getConstContents(int canId) {
        return getConstContents(canNameToId.get(canId));
    }

    public ArrayList<VariableContents> getCanStruct(int canId) {
        return getStructContents(canIDToStruct.get(canId));
    }

    public ArrayList<VariableContents> getCanStruct(String name) {
        return getStructContents(canIDToStruct.get(canNameToId.get(name)));
    }

    public ArrayList<VariableContents> getStructContents(String name) {
        return structRepo.get(name);
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
