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
    private ArrayList subStrucCon = null;

    public Parse(String code) throws Exception {
        IASTTranslationUnit translationUnit = getIASTTranslationUnit(code.toCharArray());
        IASTNode node = translationUnit;
        saveNodes(node);
    }

    /**Accesses all the nodes of the tree*/
    private void saveNodes(IASTNode node) throws Exception {
        IASTNode[] children = node.getChildren();
        for (IASTNode child : children) {

            if (child instanceof CPPASTSimpleDeclaration) {
                CPPASTSimpleDeclaration declaration = (CPPASTSimpleDeclaration) child;

                if (declaration.getDeclSpecifier() instanceof CPPASTCompositeTypeSpecifier) {
                    CPPASTCompositeTypeSpecifier compSpec = (CPPASTCompositeTypeSpecifier) declaration.getDeclSpecifier();
                    CPPASTName name = (CPPASTName) compSpec.getName();
                    structHelper(compSpec.getDeclarations(false), name.getRawSignature());
                } else {
                    saveAs("const", child);
                }

            }
        }
    }

    private void saveAs(String t, IASTNode node) throws Exception{
        CPPASTSimpleDeclaration declaration = (CPPASTSimpleDeclaration) node;
        CPPASTNamedTypeSpecifier specifier = (CPPASTNamedTypeSpecifier) declaration.getDeclSpecifier();
        CPPASTDeclarator declarator = (CPPASTDeclarator) declaration.getDeclarators()[0];

        if (declarator != null || specifier != null || declaration != null) {

            CPPASTName name = (CPPASTName) declarator.getName(); //get name
            IToken typeQualifier = specifier.getSyntax(); //get typeQualifer type (iToken)
            CPPASTName type = (CPPASTName) specifier.getName(); //get type

            if (t.equals("struct")) {
                StructContents content = new StructContents(name,type);
                subStrucCon.add(content);
            }

            else if (t.equals("const")) {
                CPPASTEqualsInitializer init = (CPPASTEqualsInitializer) declarator.getInitializer();
                if (init != null) {
                    CPPASTLiteralExpression value = (CPPASTLiteralExpression) init.getInitializerClause();
                    Contents contents = new Contents(name, value, typeQualifier, type);
                    constRepo.put(name.getRawSignature(), contents);
                }

            }

        }
    }

    private void structHelper(IASTDeclaration[] declarations, String name) throws Exception {
        subStrucCon = new ArrayList();
        for (IASTDeclaration element : declarations) {
            saveAs("struct", element);
        }
        structRepo.put(name, subStrucCon); //change name later
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
