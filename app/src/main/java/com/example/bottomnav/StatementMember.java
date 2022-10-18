package com.example.bottomnav;

import org.eclipse.cdt.core.dom.ast.ExpansionOverlapsBoundaryException;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTSimpleDeclaration;

public class StatementMember extends Statement{

    // Initialization function filters optional of variables if invalid member statement.
    public StatementMember(CPPASTSimpleDeclaration declaration) throws ExpansionOverlapsBoundaryException {
        super(declaration);
        variableContents = createVariableContents();
        variableContents.filter(this::isMemberValid);
    }

    // Filter helper function for optional.
    private boolean isMemberValid(VariableContents contents) {
        if (contents.name != null && contents.payloadDataType != null && contents.value == null && contents.typeQualifier == null) {
            return true;
        }
        return false;
    }
}
