package com.example.bottomnav;

import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.gnu.cpp.GPPLanguage;
import org.eclipse.cdt.core.index.IIndex;
import org.eclipse.cdt.core.parser.*;
import org.eclipse.cdt.internal.core.dom.parser.cpp.*;

import java.util.HashMap;
import java.util.Map;

public class Parse {
    public HashMap<String, Contents> repo = new HashMap<>();
    public HashMap<String, StructContents> structRepo = new HashMap<>();
    private StructContents currStruc = null;

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
                    structHelper(compSpec.getDeclarations(false));
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
                Contents contents = new Contents(name, null, null, type);
                currStruc.save(contents);
            }

            else if (t.equals("const")) {
                CPPASTEqualsInitializer init = (CPPASTEqualsInitializer) declarator.getInitializer();
                CPPASTLiteralExpression value = (CPPASTLiteralExpression) init.getInitializerClause(); //get value
                Contents contents = new Contents(name, value, typeQualifier, type);
                repo.put(name.getRawSignature(), contents);
            }

        }
    }

    private void structHelper(IASTDeclaration[] declarations) throws Exception {
        StructContents con = new StructContents();
        currStruc  = con;
        for (IASTDeclaration element : declarations) {
            saveAs("struct", element);
        }
        structRepo.put("added", con); //change name later
    }

    public Contents get(String key) throws IllegalAccessError{
        if (repo.get(key) == null) {
            return null;
        }
        return repo.get(key);
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
