package com.example.bottomnav;

import org.eclipse.cdt.core.dom.ast.ExpansionOverlapsBoundaryException;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTDeclarator;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTEqualsInitializer;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTLiteralExpression;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTNamedTypeSpecifier;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTSimpleDeclSpecifier;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTSimpleDeclaration;

import java.util.ArrayList;
import java.util.Optional;

// Only difference between const and members: type qualifiers and values. Parsing is the same.
public class Statement {
    protected Optional<VariableContents> variableContents;

    public Statement(Optional<VariableContents> contents) {
        variableContents = contents;
    }

    // parseStatement serves as a choke point for invalid statements
    protected static Optional<VariableContents> getVariableContents(CPPASTSimpleDeclaration declaration)
            throws ExpansionOverlapsBoundaryException {
        CPPASTDeclarator dec = (CPPASTDeclarator) declaration.getDeclarators()[0];
        CPPASTEqualsInitializer init = (CPPASTEqualsInitializer) dec.getInitializer();

        Optional<CPPASTDeclarator> declarator = Optional.ofNullable(dec);
        Optional<CPPASTEqualsInitializer> initializer = Optional.ofNullable(init);
        Optional<CPPASTSimpleDeclSpecifier> simpleSpecifier;
        Optional<CPPASTNamedTypeSpecifier> namedTypeSpecifier;

        /**
         * Declaration specifiers vary!
         * Simple Specifier: Type Specifier eg. int, float, double, etc.
         * Named Type Specifier: Type Specifier eg. uint8_t, uint16, etc. (custom)
         * Type Qualifier: eg. const, volatile, restrict, etc.
         */
        if (declaration.getDeclSpecifier() instanceof CPPASTSimpleDeclSpecifier) {
            simpleSpecifier = Optional.of((CPPASTSimpleDeclSpecifier) declaration.getDeclSpecifier());
            namedTypeSpecifier = Optional.empty();
        } else if (declaration.getDeclSpecifier() instanceof CPPASTNamedTypeSpecifier) {
            simpleSpecifier = Optional.empty();
            namedTypeSpecifier = Optional.of((CPPASTNamedTypeSpecifier) declaration.getDeclSpecifier());
        } else { // Possibility that specifier is neither simple or named type.
            simpleSpecifier = Optional.empty();
            namedTypeSpecifier = Optional.empty();
        }
        if (!(declarator.isPresent() && (simpleSpecifier.isPresent() || namedTypeSpecifier.isPresent()))) {
            return Optional.empty();
        }
        String name = declarator.get().getName().getRawSignature();
        String value = initializer.isPresent() == true ? ((CPPASTLiteralExpression) initializer.get().getInitializerClause()).getRawSignature() : null;
        String payloadType;
        String qualifier;
        if (simpleSpecifier.isPresent()) {
            qualifier = simpleSpecifier.get().getSyntax().getImage();
            payloadType = Statement.getPrimitiveType(simpleSpecifier.get().getType());
        } else {
            qualifier = namedTypeSpecifier.get().getSyntax().getImage();
            payloadType = namedTypeSpecifier.get().getName().getRawSignature();
        }
        /**
         * Declaration qualifiers vary!
         * Type Qualifer: eg. const, volatile, restrict, etc.
         * Type qualifiers sometimes to specifier if qualifier doesn't exist
         */
        if (qualifier.equals(payloadType)) {
            qualifier = null;
        }
        return Optional.of(new VariableContents(name, qualifier, payloadType, value));
    }

    public boolean areContentsPresent() {
        return variableContents.isPresent();
    }

    public VariableContents getContents() {
        return variableContents.get();
    }

    // AST parser retrieves an integer to represent a primitive type. Interpret a number as String.
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
