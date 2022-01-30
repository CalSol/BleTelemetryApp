package com.company;
import java.awt.*;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.eclipse.cdt.core.dom.ast.*;
import org.eclipse.cdt.core.dom.ast.gnu.cpp.GPPLanguage;
import org.eclipse.cdt.core.index.IIndex;
import org.eclipse.cdt.core.parser.DefaultLogService;
import org.eclipse.cdt.core.parser.FileContent;
import org.eclipse.cdt.core.parser.IParserLogService;
import org.eclipse.cdt.core.parser.IScannerInfo;
import org.eclipse.cdt.core.parser.IncludeFileContentProvider;
import org.eclipse.cdt.core.parser.ScannerInfo;
import org.eclipse.cdt.internal.core.dom.parser.IASTAmbiguityParent;
import org.eclipse.cdt.internal.core.dom.parser.cpp.*;
import org.eclipse.cdt.internal.core.dom.parser.cpp.semantics.CPPVisitor;

import java.util.Iterator;


public class Parser {

    public HashMap<Integer, Parse> repo; //Store the translation unit with set ID
    private String sourceCode; //current source code
    private IASTTranslationUnit translationUnit; //current translation unit node
    public int ID;

    private static String namedTypeSpecifier = "CPPASTNamedTypeSpecifier";
    private static String declarator = "CPPASTDeclarator";
    private static String name = "CPPASTName";
    private static String initializer = "CPPASTEqualsInitializer";
    private static String literalExpression ="CPPASTLiteralExpression";

    //We need to somehow store the nodes for each specified translationUNit

    /**Instatiates the Parser class*/
    public Parser() {
        this.ID = 0; //next time use SHA-1's ?
        this.repo = new HashMap<>();
    }

    /**Stores contents*/
    public void parse(String code) throws Exception {
        this.sourceCode = code;
        this.translationUnit = getIASTTranslationUnit(this.sourceCode.toCharArray());

        //methods to store the children and their values
        IASTTranslationUnit node = this.translationUnit;
        Parse parsedContents = new Parse(node);

        repo.put(ID, parsedContents);
        ID += 1;
    }

    private static void check(IASTNode node, Parser current) {
        IASTNode[] children = node.getChildren();

        System.out.println(node.getClass().getSimpleName());

        for (IASTNode child : children) {
            check(child, current);
        }
    }

    private void saveNodes(IASTNode node) {
        IASTNode[] children = node.getChildren();

        for (IASTNode child : children) {
            IASTNode type = get(child, this.namedTypeSpecifier);



            IASTNode declarator = get(child, this.declarator);
            IASTNode name = get(declarator, this.name);
            IASTNode init = get(declarator, this.initializer);
            IASTNode value = get(init, this.literalExpression);
        }
    }

    //get method, pass in node and the type
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

    //Setup
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
