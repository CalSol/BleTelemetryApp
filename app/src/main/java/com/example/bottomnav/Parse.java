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
    public HashMap<String, ConstContents> constRepo = new HashMap<>();
    public HashMap<String, ArrayList<StructContents>> structRepo = new HashMap<>();
    public HashMap<String, ArrayList<String>> idStruct = new HashMap<>();

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
        associateIDStruct(comments);
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
        ArrayList<StructContents> struct = new ArrayList<>();
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

    private void associateIDStruct(IASTNode[] comments) {
        Pattern pattern = Pattern.compile("@canPayloadStruct\\s+(\\S+)\\s*=\\s*(\\S+)");
        for (IASTNode comment : comments) {
            Matcher matcher = pattern.matcher(comment.getRawSignature());
            if (matcher.find()) {
                if (!idStruct.containsKey(matcher.group(2))) {
                    ArrayList<String> list = new ArrayList<>();
                    idStruct.put(matcher.group(2), list);
                }
                idStruct.get(matcher.group(2)).add(matcher.group(1));
            }
        }
    }

    //Given a CAN ID name, retrieves its associated struct contents
    public ArrayList<StructContents> getAssociatedStruct(String idName) {
        for (String struct : idStruct.keySet()) {
            ArrayList<String> curr = idStruct.get(struct);
            if (curr.contains(idName)) {
                return getStruct(struct);
            }
        }
        return null;
    }

    public ConstContents getConst(String key) {
        return constRepo.get(key);
    }

    public ArrayList<StructContents> getStruct(String key) {
        return structRepo.get(key);
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
