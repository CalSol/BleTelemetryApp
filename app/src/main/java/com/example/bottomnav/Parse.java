package com.company;

import org.eclipse.cdt.core.dom.ast.ExpansionOverlapsBoundaryException;
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

    private IASTTranslationUnit translationUnit; //current translation unit node
    public HashMap<String, Contents> repo;

    public Parse(String code) throws Exception {
        repo = new HashMap<>();

        translationUnit = getIASTTranslationUnit(code.toCharArray());
        IASTNode node = translationUnit;

        saveNodes(node);
    }

    /**Accesses all the nodes of the tree*/
    private void saveNodes(IASTNode node) throws Exception {
        IASTNode[] children = node.getChildren();
        for (IASTNode child :children) {
            if (child instanceof CPPASTSimpleDeclaration) {

                /**Setup*/
                CPPASTSimpleDeclaration declaration = (CPPASTSimpleDeclaration) child;
                CPPASTNamedTypeSpecifier specifier = (CPPASTNamedTypeSpecifier) declaration.getDeclSpecifier();
                CPPASTDeclarator declarator = (CPPASTDeclarator) declaration.getDeclarators()[0];
                CPPASTEqualsInitializer init = (CPPASTEqualsInitializer) declarator.getInitializer();

                if (init == null) {
                    continue;
                }

                if (declarator == null) {
                    continue;
                }

                if (specifier == null) {
                    continue;
                }

                if (declaration == null) {
                    continue;
                }

                CPPASTLiteralExpression value = (CPPASTLiteralExpression) init.getInitializerClause(); //get value
                CPPASTName name = (CPPASTName) declarator.getName(); //get name
                IToken typeQualifier = specifier.getSyntax(); //get typeQualifer type (iToken)
                CPPASTName type = (CPPASTName) specifier.getName(); //get type

                Contents contents = new Contents(name, value, typeQualifier, type);
                repo.put(name.getRawSignature(), contents);
            }
        }
    }

    public Contents get(String key) throws IllegalAccessError{
        if (repo.get(key) == null) {
            throw new IllegalAccessError("The name does not exist!");
        }
        return repo.get(key);
    }

    public ArrayList<Contents> getByType(String key) {
        return findList(key, 0);
    }

    public ArrayList<Contents> getTypeQualifier(String key) {
        return findList(key, 1);
    }

    /**Makes and return a list of contents*/
    private ArrayList<Contents> findList (String key, int i) {
        ArrayList<Contents> list = new ArrayList<>();
        for (String con : repo.keySet()) {
            Contents contents = get(con);
            if (i == 0) {
                if (contents.type.equals(key)) {
                    list.add(contents);
                }
            }
            else {
                if (contents.typeQualifer.equals(key)) {
                    list.add(contents);
                }
            }
        }
        if (list.isEmpty()) {
            throw new IllegalArgumentException("No contents[s] were found for given key.");
        }
        return list;
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
