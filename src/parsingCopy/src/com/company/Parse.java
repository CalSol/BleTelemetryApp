package com.company;

import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;

import java.util.Hashtable;

public class Parse {
    private static String namedTypeSpecifier = "CPPASTNamedTypeSpecifier";
    private static String declarator = "CPPASTDeclarator";
    private static String name = "CPPASTName";
    private static String initializer = "CPPASTEqualsInitializer";
    private static String literalExpression ="CPPASTLiteralExpression";

    public Hashtable<IASTTranslationUnit, IASTTranslationUnit> contents;


    public Parse(IASTTranslationUnit node) {
        contents = new Hashtable<>();
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

}
