package com.example.bottomnav;

import org.eclipse.cdt.core.dom.ast.ExpansionOverlapsBoundaryException;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTSimpleDeclaration;

public class ConstStatement extends Statement{

    // Initialization function filters Optional of variables if invalid const statement.
    public ConstStatement(CPPASTSimpleDeclaration declaration) throws ExpansionOverlapsBoundaryException {
        super(declaration);
        variableContents = createVariableContents();
        variableContents.filter(this::isConstValid);
    }

    // Filter helper function for optional.
    private boolean isConstValid(VariableContents contents) {
        if (contents.value != null && contents.typeQualifier != null &&
                contents.payloadDataType != null && contents.name != null) {
            return true;
        }
        return false;
    }
}
