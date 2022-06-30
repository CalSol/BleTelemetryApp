package com.example.bottomnav;

import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTDeclarator;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTEqualsInitializer;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTNamedTypeSpecifier;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTSimpleDeclSpecifier;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTSimpleDeclaration;

import java.util.Optional;
public class Components {
    public String name;
    public String typeQualifier;
    public String type;
    public Optional<CPPASTEqualsInitializer> init;

    public Components(String givenName, String givenTQ,
                      String givenType, Optional<CPPASTEqualsInitializer> givenInit) {
        name = givenName;
        typeQualifier = givenTQ;
        type = givenType;
        init = givenInit;
    }

    public static Optional<Components> getComponents(CPPASTSimpleDeclaration dec) throws Exception {
        if (dec.getDeclSpecifier() instanceof CPPASTSimpleDeclSpecifier) {
            return makeOptFromDecl((CPPASTSimpleDeclSpecifier) dec.getDeclSpecifier(),
                    (CPPASTDeclarator) dec.getDeclarators()[0]);
        } else {
            return makeOptFromDecl((CPPASTNamedTypeSpecifier) dec.getDeclSpecifier(),
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
    private static Optional<Components> makeOptFromDecl(CPPASTSimpleDeclSpecifier simpleSpec,
                                                    CPPASTDeclarator declarator) throws Exception {
        CPPASTEqualsInitializer init = (CPPASTEqualsInitializer) declarator.getInitializer();
        Optional<CPPASTEqualsInitializer> initOpt = makeOptInit(init);

        if (declarator != null && simpleSpec != null) {
            String declaratorNameStr = declarator.getName().getRawSignature();
            String typeQualiferNameStr = simpleSpec.getSyntax().getImage();
            String primitiveNameStr = getPrimitiveType(simpleSpec.getType());
            return Optional.of(new Components(declaratorNameStr, typeQualiferNameStr,
                    primitiveNameStr, initOpt));
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
    private static Optional<Components> makeOptFromDecl(CPPASTNamedTypeSpecifier namedTypeSpec,
                                                    CPPASTDeclarator declarator) throws Exception {
        CPPASTEqualsInitializer init = (CPPASTEqualsInitializer) declarator.getInitializer();
        Optional<CPPASTEqualsInitializer> initOpt = makeOptInit(init);
        if (declarator != null && namedTypeSpec != null) {
            String declaratorNameStr = declarator.getName().getRawSignature();
            String typeQualifierNameStr = namedTypeSpec.getSyntax().getImage();
            String primitiveTypeNameStr = namedTypeSpec.getName().getRawSignature();
            return Optional.of(new Components(declaratorNameStr, typeQualifierNameStr,
                    primitiveTypeNameStr, initOpt));
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

    /**
     * @param primType
     * @return char[]
     *
     * AST parser retrieves an integer to represent a primitive type. This function allows to
     * interpret a number as String.
     */
    private static String getPrimitiveType(int primType) {
        switch (primType) {
            case 0:
                return "long";
            case 3:
                return "int";
            case 4:
                return "float";
            case 5:
                return "double";
            default:
                return null;
        }
    }
}
