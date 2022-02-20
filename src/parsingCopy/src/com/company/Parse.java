package com.company;

import org.eclipse.cdt.core.dom.ast.ExpansionOverlapsBoundaryException;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.gnu.cpp.GPPLanguage;
import org.eclipse.cdt.core.index.IIndex;
import org.eclipse.cdt.core.parser.*;
import org.eclipse.cdt.internal.core.dom.parser.cpp.*;

import java.util.HashMap;
import java.util.Map;

public class Parse {

    private IASTTranslationUnit translationUnit; //current translation unit node
    public HashMap<IToken, HashMap> bigTypes;

    public Parse(String code) throws Exception {
        bigTypes = new HashMap<>();

        translationUnit = getIASTTranslationUnit(code.toCharArray());
        IASTNode node = translationUnit;

        saveNodes(node);
    }

    /**Accesses all the nodes of the tree*/
    private void saveNodes(IASTNode node) throws ExpansionOverlapsBoundaryException {
        IASTNode[] children = node.getChildren();
        for (IASTNode child :children) {
            if (child instanceof CPPASTSimpleDeclaration) {

                /**Setup*/
                CPPASTSimpleDeclaration declaration = (CPPASTSimpleDeclaration) child;
                CPPASTNamedTypeSpecifier specifier = (CPPASTNamedTypeSpecifier) declaration.getDeclSpecifier();
                CPPASTDeclarator declarator = (CPPASTDeclarator) declaration.getDeclarators()[0];
                CPPASTEqualsInitializer init = (CPPASTEqualsInitializer) declarator.getInitializer();

                /**Get what is needed: Value, Name, bigType & smallType*/
                CPPASTLiteralExpression value = (CPPASTLiteralExpression) init.getInitializerClause(); //get value
                CPPASTName name = (CPPASTName) declarator.getName(); //get name
                IToken bigType = specifier.getSyntax(); //get big type (iToken)
                CPPASTName smallType = (CPPASTName) specifier.getName(); //get small type

                /**Checks/setups its types if it doesn't exist,    eg. bigType = Const   smallType = uint_32*/
                checkBigTypeExists(bigType);
                checkSmallTypeExists(bigType, smallType);

                /**Saves name and value to their respective location*/
                HashMap<CPPASTName, CPPASTLiteralExpression> contentMap = getContentMap(bigType, smallType);
                contentMap.put(name, value);
            }
        }
    }

    /**Adds a new hashmap for Large Types if bType does not exist*/
    private void checkBigTypeExists(IToken bType) {
        if (!bigTypes.containsKey(bType)) {
            HashMap<CPPASTName, HashMap> subTypes = new HashMap();
            bigTypes.put(bType, subTypes);
        }
    }

    /**Adds a new hashmap for Small Types if sType does not exist*/
    private void checkSmallTypeExists(IToken bType, CPPASTName sType) {
        checkBigTypeExists(bType);
        HashMap subTypes = bigTypes.get(bType);
        if (!subTypes.containsKey(sType)) {
            HashMap<CPPASTName, CPPASTLiteralExpression> contents = new HashMap<>();
            subTypes.put(sType, contents);
        }
    }

    /**Only works if sType and bTYpe exist*/
    private HashMap<CPPASTName, CPPASTLiteralExpression> getContentMap(IToken bType, CPPASTName sType) {
        HashMap subTypes = bigTypes.get(bType);
        HashMap contents = (HashMap<CPPASTName, CPPASTLiteralExpression>) subTypes.get(sType);
        return contents;
    }

    /**Gets value of given name*/
    public CPPASTLiteralExpression get(CPPASTName name) {
        //Iterate over big types
        for(IToken key : bigTypes.keySet()) {
            HashMap bType = bigTypes.get(key);

            //Iterate over small types
            for (Object ke : bType.keySet()) {
                CPPASTName smallTypeName = (CPPASTName) ke;
                HashMap<CPPASTName, CPPASTLiteralExpression> sType = (HashMap) bType.get(smallTypeName);

                //Iterate over all names
                for (Object ks : sType.keySet()) {
                    CPPASTName n = (CPPASTName) ks;
                    if (n.toString().equals(name.toString())) {
                        return sType.get(n);
                    }
                }

            }

        }
        throw new IllegalArgumentException("Given name does not exist!");
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

    /** Below are the functions used to print all*/

    /**Function called by instance to print all types*/
    public void showNameValue() {
        iterateBigTypes();
    }
    /**Iterate through all big types eg. const*/
    private void iterateBigTypes() {
        for(IToken key : bigTypes.keySet()) {
            System.out.println("Big type " + key + " : ");
            HashMap bType = bigTypes.get(key);
            iterateSmallTypes(bType);
        }
    }

    /**Iterate through all small types eg. uint_32*/
    private void iterateSmallTypes(HashMap bType) {

        for (Object key : bType.keySet()) {
            CPPASTName smallTypeName = (CPPASTName) key;
            System.out.println("Small type " + smallTypeName + " : ");
            HashMap sType = (HashMap) bType.get(smallTypeName);
            iterateNameVal(sType);
        }
    }

    /**Iterate throught all names and get their values*/
    private void iterateNameVal(HashMap sType) {
        for (Object key : sType.keySet()) {
            CPPASTName name = (CPPASTName) key;
            CPPASTLiteralExpression value = (CPPASTLiteralExpression) sType.get(name);
            System.out.println("Name is: " + name + ", Value is: " + value);
        }
    } //Only prints

    /**Prints out their types*/
    private void check(IASTNode node) throws ExpansionOverlapsBoundaryException {
        IASTNode[] children = node.getChildren();
        for (IASTNode child :children) {
            System.out.println("Class: " + child.getClass() + "," + " Literal: " + child.getRawSignature());
            check(child);
        }
    }
}
