package com.example.bottomnav;

import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.gnu.cpp.GPPLanguage;
import org.eclipse.cdt.core.index.IIndex;
import org.eclipse.cdt.core.parser.*;
import org.eclipse.cdt.internal.core.dom.parser.cpp.*;
import org.eclipse.cdt.internal.core.model.Parent;
import org.eclipse.cdt.internal.core.parser.scanner.CharArray;

import java.io.*;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
public class Parse {
    public HashMap<String, Contents> constRepo = new HashMap<>();
    public HashMap<String, ArrayList<StructContents>> structRepo = new HashMap<>();

    public static Optional<Parse> parseTextFile(String fileName) throws Exception {
        OpenTextFile txt = new OpenTextFile(fileName);
        Optional<char[]> code = txt.code;
        if (code.isPresent()) {
            return Optional.of(new Parse(code.get()));
        }
        return Optional.empty();
    }

    public Parse(char[] code) throws Exception {
        IASTTranslationUnit translationUnit = getIASTTranslationUnit(code);
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
    }

    private void storeConst(CPPASTSimpleDeclaration declaration) throws Exception {
        Optional<Components> data = Components.Deconstruct(declaration);
        if (data.isPresent() && data.get().init.isPresent()) {
            Components comp = data.get();
            CPPASTLiteralExpression value =
                    (CPPASTLiteralExpression) comp.init.get().getInitializerClause();
            Contents contents = new Contents(comp.name, value, comp.typeQualifier, comp.type);
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

    public Contents getConst(String key) {
        return constRepo.get(key);
    }

    public ArrayList getStruct(String key) {
        return structRepo.get(key);
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
