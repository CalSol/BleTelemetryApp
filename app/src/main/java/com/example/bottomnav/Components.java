package com.example.bottomnav;

import org.eclipse.cdt.core.dom.ast.ExpansionOverlapsBoundaryException;
import org.eclipse.cdt.core.dom.ast.IASTDeclSpecifier;
import org.eclipse.cdt.core.parser.IToken;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTDeclarator;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTEqualsInitializer;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTName;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTNamedTypeSpecifier;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTSimpleDeclSpecifier;
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

    public static Optional<Components> getComponents(CPPASTSimpleDeclaration dec) throws Exception {
        CPPASTType specifierType = getSpecifierType(dec);
        switch (specifierType) {
            case Simple:
                return deconstruct((CPPASTSimpleDeclSpecifier) dec.getDeclSpecifier(),
                        (CPPASTDeclarator) dec.getDeclarators()[0]);
            case NamedType:
                return deconstruct((CPPASTNamedTypeSpecifier) dec.getDeclSpecifier(),
                        (CPPASTDeclarator) dec.getDeclarators()[0]);

            default:
                return Optional.empty();
        }
    }

    private static CPPASTType getSpecifierType(CPPASTSimpleDeclaration declaration) {
        if (declaration == null) {
            return null;
        } else if(declaration.getDeclSpecifier() instanceof CPPASTSimpleDeclSpecifier) {
            return CPPASTType.Simple;
        }
        return CPPASTType.NamedType;
    }

    private static Optional<Components> deconstruct(CPPASTSimpleDeclSpecifier specifier,
                                                    CPPASTDeclarator declarator) throws Exception {
        CPPASTEqualsInitializer init = (CPPASTEqualsInitializer) declarator.getInitializer();
        Optional<CPPASTEqualsInitializer> initOpt = makeOpt(init);
        if (declarator != null && specifier != null) {
            CPPASTName name = new CPPASTName();
            name.setName(specifier.getSyntax().getCharImage());
            return Optional.of(new Components((CPPASTName) declarator.getName(),
                    null,
                    name,
                    initOpt));
        } else {
            return Optional.empty();
        }
    }

    private static Optional<Components> deconstruct(CPPASTNamedTypeSpecifier specifier,
                                                    CPPASTDeclarator declarator) throws Exception {
        CPPASTEqualsInitializer init = (CPPASTEqualsInitializer) declarator.getInitializer();
        Optional<CPPASTEqualsInitializer> initOpt = makeOpt(init);
        if (declarator != null && specifier != null) {
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
