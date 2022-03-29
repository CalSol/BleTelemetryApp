package com.example.bottomnav;

import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.gnu.cpp.GPPLanguage;
import org.eclipse.cdt.core.index.IIndex;
import org.eclipse.cdt.core.parser.*;
import org.eclipse.cdt.internal.core.dom.parser.cpp.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Parse {
    public HashMap<String, Contents> constRepo = new HashMap<>();
    public HashMap<String, ArrayList> structRepo = new HashMap<>();

    public Parse(String code) throws Exception {
        IASTTranslationUnit translationUnit = getIASTTranslationUnit(code.toCharArray());
        process(translationUnit);
    }

    /**Accesses all the nodes of the tree*/
    private void process(IASTNode node) throws Exception {
        IASTNode[] children = node.getChildren();
        for (IASTNode child : children) {
            if (child instanceof CPPASTSimpleDeclaration) {
                CPPASTSimpleDeclaration declaration = (CPPASTSimpleDeclaration) child;

                if (declaration.getDeclSpecifier() instanceof CPPASTCompositeTypeSpecifier) {
                    CPPASTCompositeTypeSpecifier compSpec = (CPPASTCompositeTypeSpecifier) declaration.getDeclSpecifier();
                    CPPASTName name = (CPPASTName) compSpec.getName();
                    saveStuct(compSpec.getDeclarations(false), name.getRawSignature());
                } else {
                    saveConst(child);
                }

            }
        }
    }

    private ArrayList getComponents(IASTNode node) throws Exception {
        ArrayList components = new ArrayList();
        CPPASTSimpleDeclaration declaration = (CPPASTSimpleDeclaration) node;
        CPPASTNamedTypeSpecifier specifier = (CPPASTNamedTypeSpecifier) declaration.getDeclSpecifier();
        CPPASTDeclarator declarator = (CPPASTDeclarator) declaration.getDeclarators()[0];
        CPPASTEqualsInitializer init = (CPPASTEqualsInitializer) declarator.getInitializer();

        if (declarator != null || specifier != null || declaration != null && init != null) {
            CPPASTName name = (CPPASTName) declarator.getName(); //get name
            IToken typeQualifier = (IToken) specifier.getSyntax(); //get typeQualifer type (iToken)
            CPPASTName type = (CPPASTName) specifier.getName(); //get type
            components.add(name); components.add(typeQualifier); components.add(type); components.add(init);
        }

        return components;
    }

    private void saveConst(IASTNode node) throws Exception {
        ArrayList components = getComponents(node);
        if (!components.isEmpty()) {
            CPPASTName name = (CPPASTName) components.get(0);
            IToken typeQualifier = (IToken) components.get(1);
            CPPASTName type = (CPPASTName) components.get(2);
            CPPASTEqualsInitializer init = (CPPASTEqualsInitializer) components.get(3);
            CPPASTLiteralExpression value = (CPPASTLiteralExpression) init.getInitializerClause();
            Contents contents = new Contents(name, value, typeQualifier, type);
            constRepo.put(name.getRawSignature(), contents);
        }
    }

    private void saveStrucMem(IASTNode node, ArrayList struct) throws Exception {
        ArrayList components = getComponents(node);

        if (!components.isEmpty()) {
            CPPASTName name = (CPPASTName) components.get(0);
            CPPASTName type = (CPPASTName) components.get(2);
            StructContents contents = new StructContents(name, type);
            struct.add(contents);
        }
    }

    private void saveStuct(IASTDeclaration[] declarations, String name) throws Exception {
        ArrayList struct = new ArrayList();

        for (IASTDeclaration element : declarations) {
            saveStrucMem(element, struct);
        }

        structRepo.put(name, struct);
    }

    public Contents get(String key) {
        return constRepo.get(key);
    }

    /**Initializes the translation unit setup*/
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
