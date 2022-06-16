package com.example.bottomnav;

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
        //If specifier is simple type, call deconstruct on specifier
        if (dec.getDeclSpecifier() instanceof CPPASTSimpleDeclSpecifier) {
            return makeOptComp((CPPASTSimpleDeclSpecifier) dec.getDeclSpecifier(),
                    (CPPASTDeclarator) dec.getDeclarators()[0]);
        } else { // If specifier is namedTyped, call deconstruct on specifier
            return makeOptComp((CPPASTNamedTypeSpecifier) dec.getDeclSpecifier(),
                    (CPPASTDeclarator) dec.getDeclarators()[0]);
        }
    }

    /**
     *
     * @param simpleSpec
     * @param declarator
     * @return Optional<Components>
     * @throws Exception
     *
     * Difference: Simple specifiers don't quantifier types and do not have name object
     */
    private static Optional<Components> makeOptComp(CPPASTSimpleDeclSpecifier simpleSpec,
                                                    CPPASTDeclarator declarator) throws Exception {
        CPPASTEqualsInitializer init = (CPPASTEqualsInitializer) declarator.getInitializer();
        Optional<CPPASTEqualsInitializer> initOpt = makeOptInit(init);
        if (declarator != null && simpleSpec != null) {
            CPPASTName name = new CPPASTName();
            name.setName(simpleSpec.getSyntax().getCharImage());
            return Optional.of(new Components((CPPASTName) declarator.getName(),
                    null,
                    name,
                    initOpt));
        } else {
            return Optional.empty();
        }
    }

    /**
     *
     * @param namedTypeSpec
     * @param declarator
     * @return Optional<Components>
     * @throws Exception
     *
     * Difference: Name typed specifiers have quantifer types and have name object
     */
    private static Optional<Components> makeOptComp(CPPASTNamedTypeSpecifier namedTypeSpec,
                                                    CPPASTDeclarator declarator) throws Exception {
        CPPASTEqualsInitializer init = (CPPASTEqualsInitializer) declarator.getInitializer();
        Optional<CPPASTEqualsInitializer> initOpt = makeOptInit(init);
        if (declarator != null && namedTypeSpec != null) {
            return Optional.of(new Components((CPPASTName) declarator.getName(),
                    (IToken) namedTypeSpec.getSyntax(),
                    (CPPASTName) namedTypeSpec.getName(),
                    initOpt));
        } else {
            return Optional.empty();
        }
    }

    private static Optional<CPPASTEqualsInitializer> makeOptInit(CPPASTEqualsInitializer init) {
        if (init != null) {
            return Optional.of(init);
        } else {
            return Optional.empty();
        }
    }
}
