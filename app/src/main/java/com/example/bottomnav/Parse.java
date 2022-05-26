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
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
public class Parse {
    public HashMap<String, ConstContents> constRepo = new HashMap<>();
    public HashMap<String, ArrayList<StructContents>> structRepo = new HashMap<>();
    public HashMap<String, PayloadMap> idStruct = new HashMap<>();

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

        mapPayload(comments);
    }

    private void storeConst(CPPASTSimpleDeclaration declaration) throws Exception {
        Optional<Components> data = Components.Deconstruct(declaration);
        if (data.isPresent() && data.get().init.isPresent()) {
            Components comp = data.get();
            CPPASTLiteralExpression value =
                    (CPPASTLiteralExpression) comp.init.get().getInitializerClause();
            ConstContents contents = new ConstContents(comp.name, value, comp.typeQualifier,
                    comp.type);
            constRepo.put(contents.name, contents);
        }
    }

    private void storeStuct(IASTDeclaration[] declarations, String name) throws Exception {
        ArrayList<StructContents> struct = new ArrayList();
        for (IASTDeclaration element : declarations) {
            CPPASTSimpleDeclaration declaration = (CPPASTSimpleDeclaration) element;
            Optional<Components> data = Components.Deconstruct(declaration);
            if (data.isPresent() && !data.get().init.isPresent()) {
                Components comp = data.get();
                StructContents contents = new StructContents(comp.name, comp.type);
                struct.add(contents);
            }
        }
        structRepo.put(name, struct);
    }

    public void mapPayload(IASTNode[] comments) {

        for (IASTNode comment : comments) {
            char[] line = simplifyString(comment.getRawSignature());
            if (line[0] == '|') {
                String idName = findString(line, 1);
                int secondIndex = idName.length() + 2;
                String structName = findString(line, secondIndex);
                if (!idStruct.containsKey(structName)) {
                    PayloadMap rep = new PayloadMap(structName);
                    idStruct.put(structName, rep);
                }
                idStruct.get(structName).canIDNames.add(idName);
            }
        }
    }

    public char[] simplifyString(String line) {
        line = line.replace("//", "");
        line = line.replace(" ", "");
        return line.toCharArray();
    }

    public String findString(char[] line, int start){
        StringBuilder name = new StringBuilder();
        char character = line[start];
        while (character != '|') {
            name.append(character);
            start += 1;
            character = line[start];
        }
        return name.toString();
    }

    //Given a CAN ID name, retrieves its associated struct
    public ArrayList getAssociatedStruct(String idName) {
        for (String struct : idStruct.keySet()) {
            PayloadMap curr = idStruct.get(struct);
            if (curr.canIDNames.contains(idName)) {
                return getStruct(curr.struct);
            }
        }
        return null;
    }

    public ConstContents getConst(String key) {
        return constRepo.get(key);
    }

    public ArrayList getStruct(String key) {
        return structRepo.get(key);
    }

    private static char[] OpenTextFile(String fileName) throws IOException {
        /** Source: https://www.oreilly.com/content/how-to-convert-an-inputstream-to-a-string/ */
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

    public static IASTTranslationUnit getIASTTranslationUnit(char[] code) throws Exception {
        FileContent fc = FileContent.create("TestFile", code);
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
