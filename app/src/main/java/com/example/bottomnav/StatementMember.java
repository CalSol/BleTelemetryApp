package com.example.bottomnav;

import org.eclipse.cdt.core.dom.ast.ExpansionOverlapsBoundaryException;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTSimpleDeclaration;

import java.util.Optional;

public class StatementMember extends Statement{

    public StatementMember(Optional<VariableContents> contents) {
        super(contents);
        variableContents = variableContents.filter(StatementMember::isMemberValid);
    }

    public static Optional<Statement> create(CPPASTSimpleDeclaration declaration) throws ExpansionOverlapsBoundaryException {
        Optional<VariableContents> variableContents = getVariableContents(declaration);
        if (variableContents.isPresent()) {
            return Optional.of(new StatementMember(variableContents));
        }
        return Optional.empty();
    }

    // Filter helper function for optional.
    private static boolean isMemberValid(VariableContents contents) {
        if (contents.name != null && contents.payloadDataType != null && contents.value == null && contents.typeQualifier == null) {
            return true;
        }
        return false;
    }
}
