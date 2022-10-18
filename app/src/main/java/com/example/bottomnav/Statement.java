package com.example.bottomnav;

import org.eclipse.cdt.core.dom.ast.ExpansionOverlapsBoundaryException;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTDeclarator;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTEqualsInitializer;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTLiteralExpression;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTNamedTypeSpecifier;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTSimpleDeclSpecifier;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTSimpleDeclaration;

import java.util.Optional;

// Only difference between const and members: type qualifiers and values. Parsing is the same.
public abstract class Statement {
    protected Optional<VariableContents> variableContents;
    protected Optional<CPPASTDeclarator> declarator;
    protected Optional<CPPASTEqualsInitializer> initializer;
    protected Optional<CPPASTSimpleDeclSpecifier> simpleSpecifier;
    protected Optional<CPPASTNamedTypeSpecifier> namedTypeSpecifier;

    // Initializer function must check if components of the statement are null or not before
    // extracting the string values, else parser may break.
    public Statement(CPPASTSimpleDeclaration declaration) {
        CPPASTDeclarator decl = (CPPASTDeclarator) declaration.getDeclarators()[0];
        CPPASTEqualsInitializer init = (CPPASTEqualsInitializer) decl.getInitializer();

        if (decl != null) {
            declarator = Optional.of(decl);
        } else {
            declarator = Optional.empty();
        }
        if (init != null) {
            initializer = Optional.of(init);
        } else {
            initializer = Optional.empty();
        }

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
    }

    // Checks that both declarator and specifier exist. All variables and members must have them.
    protected boolean isStatementValid() {
        return declarator.isPresent() && (simpleSpecifier.isPresent() || namedTypeSpecifier.isPresent());
    }

    // String variables could be null, which is fine. Members don't have type qualifiers and values.
    protected Optional<VariableContents> createVariableContents() throws ExpansionOverlapsBoundaryException {
        if (isStatementValid()) {
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
            if (qualifier == payloadType) {
                qualifier = null;
            }
            return Optional.of(new VariableContents(name, qualifier, payloadType, value));
        }
        return Optional.empty();
    }

    public boolean isPresent() {
        return variableContents.isPresent();
    }

    // Functions isPresent must be called before using getVariableContents for presence check.
    public VariableContents getVariableContents() {
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
