package com.example.bottomnav;

import org.eclipse.cdt.core.parser.IToken;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTLiteralExpression;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTName;

public class ConstContents {
    String value;
    String name;
    String typeQualifer;
    String payLoadDataType;

    public ConstContents(CPPASTName iName, CPPASTLiteralExpression iValue, CPPASTName iTQ, CPPASTName iType) {
        value = iValue.getRawSignature();
        typeQualifer = iTQ.toString();
        name = iName.getRawSignature();
        payLoadDataType = iType.toString();
    }
}
