package com.example.bottomnav;

import org.eclipse.cdt.core.parser.IToken;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTDeclarator;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTEqualsInitializer;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTName;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTNamedTypeSpecifier;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTSimpleDeclaration;

import java.util.Optional;

public class Components {
    public CPPASTName name;
    public IToken typeQualifier;
    public CPPASTName type;
    public CPPASTEqualsInitializer init;
    public boolean valid = false;

    public Components(CPPASTSimpleDeclaration declaration) throws Exception{
        CPPASTNamedTypeSpecifier specifier = (CPPASTNamedTypeSpecifier) declaration.getDeclSpecifier();
        CPPASTDeclarator declarator = (CPPASTDeclarator) declaration.getDeclarators()[0];
        init = (CPPASTEqualsInitializer) declarator.getInitializer();
        if (declarator != null && specifier != null && declaration != null) {
            name = (CPPASTName) declarator.getName(); //get name
            typeQualifier = (IToken) specifier.getSyntax(); //get typeQualifer type (iToken)
            type = (CPPASTName) specifier.getName(); //get type
            valid = true;
        }
    }
}
