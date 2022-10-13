package com.example.bottomnav;

import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTDeclarator;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTEqualsInitializer;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTLiteralExpression;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTNamedTypeSpecifier;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTSimpleDeclSpecifier;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTSimpleDeclaration;
import org.eclipse.cdt.internal.core.model.Variable;

import java.util.Optional;
public class VariableContents {
    public String name;
    public String typeQualifier;
    public String payloadDataType;
    public String value;

    public VariableContents(String givenName, String givenTQ,
                            String givenType, String givenValue) {
        name = givenName;
        typeQualifier = givenTQ;
        payloadDataType = givenType;
        value = givenValue;
    }

    public static Optional<VariableContents> getContents(CPPASTSimpleDeclaration dec) throws Exception {
        if (dec.getDeclSpecifier() instanceof CPPASTSimpleDeclSpecifier) {
            return makeOptFromDecl((CPPASTSimpleDeclSpecifier) dec.getDeclSpecifier(),
                    (CPPASTDeclarator) dec.getDeclarators()[0]);
        } else {
            return makeOptFromDecl((CPPASTNamedTypeSpecifier) dec.getDeclSpecifier(),
                    (CPPASTDeclarator) dec.getDeclarators()[0]);
        }
    }
}
