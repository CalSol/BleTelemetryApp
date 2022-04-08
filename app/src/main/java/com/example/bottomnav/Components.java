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
    public Optional<CPPASTEqualsInitializer> init;

    public Components(CPPASTName givenName, IToken givenTQ,
                      CPPASTName givenType, Optional<CPPASTEqualsInitializer> givenInit) {
        name = givenName;
        typeQualifier = givenTQ;
        type = givenType;
        init = givenInit;
    }

    public static Optional<Components> Deconstruct(CPPASTSimpleDeclaration declaration) throws Exception {
        CPPASTNamedTypeSpecifier specifier = (CPPASTNamedTypeSpecifier) declaration.getDeclSpecifier();
        CPPASTDeclarator declarator = (CPPASTDeclarator) declaration.getDeclarators()[0];
        CPPASTEqualsInitializer init = (CPPASTEqualsInitializer) declarator.getInitializer();
        Optional<CPPASTEqualsInitializer> initOpt = makeOpt(init);
        if (declarator != null && specifier != null && declaration != null) {
            return Optional.of(new Components((CPPASTName) declarator.getName(),
                    (IToken) specifier.getSyntax(),
                    (CPPASTName) specifier.getName(),
                    initOpt));
        } else {
            return Optional.empty();
        }
    }

    private static Optional<CPPASTEqualsInitializer> makeOpt(CPPASTEqualsInitializer init) {
        if (init != null) {
            return Optional.of(init);
        } else {
            return Optional.empty();
        }
    }
}
