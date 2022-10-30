package com.example.bottomnav;

import org.eclipse.cdt.core.dom.ast.ExpansionOverlapsBoundaryException;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTSimpleDeclaration;

import java.util.Optional;

public class StatementConst extends Statement{

    public StatementConst(Optional<VariableContents> contents) {
        super(contents);
        variableContents = variableContents.filter(StatementConst::isConstValid);
    }

    public static Optional<Statement> create(CPPASTSimpleDeclaration declaration) throws ExpansionOverlapsBoundaryException {
        Optional<VariableContents> variableContents = Statement.getVariableContents(declaration);
        if (variableContents.isPresent()) {
            return Optional.of(new StatementConst(variableContents));
        }
        return Optional.empty();
    }

    // Filter helper function for optional.
    private static boolean isConstValid(VariableContents contents) {
        if (contents.value != null && contents.typeQualifier != null &&
                contents.payloadDataType != null && contents.name != null) {
            return true;
        }
        return false;
    }
}
