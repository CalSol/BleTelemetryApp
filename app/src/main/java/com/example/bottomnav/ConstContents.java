package com.example.bottomnav;

import org.eclipse.cdt.core.parser.IToken;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTLiteralExpression;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTName;

public class ConstContents {
    String value;
    String name;
    String typeQualifer;
    String type;
    PayLoadDataType payLoadDataType;

    public ConstContents(CPPASTName iName, CPPASTLiteralExpression iValue, IToken iTQ, CPPASTName iType) throws Exception {
        value = iValue.getRawSignature();
        typeQualifer = iTQ.toString();
        name = iName.getRawSignature();
        type = iType.getRawSignature();
    }
}
