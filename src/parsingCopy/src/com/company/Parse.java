package com.company;

import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.gnu.cpp.GPPLanguage;
import org.eclipse.cdt.core.index.IIndex;
import org.eclipse.cdt.core.parser.*;

import java.util.HashMap;
import java.util.Map;

public class Parse {

    private final String namedTypeSpecifier = "CPPASTNamedTypeSpecifier";
    private final String declarator = "CPPASTDeclarator";
    private final String name = "CPPASTName";
    private final String initializer = "CPPASTEqualsInitializer";
    private final String literalExpression ="CPPASTLiteralExpression";
    private final String simpleDeclaration = "CPPASTSimpleDeclaration";

    public HashMap<String, HashMap> contents; //Contents stored based on their type
    private String sourceCode; //Given code
    private IASTTranslationUnit translationUnit;


    public Parse(String code) throws Exception{
        contents = new HashMap<>();
        sourceCode = code;
        translationUnit = getIASTTranslationUnit(this.sourceCode.toCharArray());
        IASTTranslationUnit node =translationUnit;
        saveNodes(node);
    }

    /**Saves and organizes nodes*/
    private void saveNodes(IASTNode node) {
        IASTNode[] children = node.getChildren();

        for (IASTNode child : children) {
            if (child.getClass().getSimpleName().equals(this.simpleDeclaration)) {
                IASTNode type = get(child, this.namedTypeSpecifier);
                String nodeType = type.getRawSignature();

                checkType(nodeType);
                HashMap storage = contents.get(nodeType);

                IASTNode declarator = get(child, this.declarator);
                IASTNode name = get(declarator, this.name);
                IASTNode init = get(declarator, this.initializer);
                IASTNode value = get(init, this.literalExpression);

                storage.put(name.getRawSignature(), value.getRawSignature());
                contents.put(nodeType, storage);
            }
        }

    }

    /**Add a new hashmap for a new type if type doesn't exist*/
    private void checkType(String nodeType){
        if (!contents.containsKey(nodeType)) {
            //Can be changed to IASTNodes
            HashMap<String, String> keyValue = new HashMap<>();
            contents.put(nodeType, keyValue);
        }
    }

    /**Finds desired node based on type*/
    private IASTNode get(IASTNode node, String type) {
        IASTNode[] children = node.getChildren();
        IASTNode target = null;

        for (IASTNode child : children) {
            String childType = child.getClass().getSimpleName();
            if (childType.equals(type)) {
                target = child;
            }
        }
        return target;
    }

    /**Setup translation*/
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
