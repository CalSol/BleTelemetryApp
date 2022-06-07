package com.example.bottomnav;

import org.eclipse.cdt.core.parser.IToken;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTLiteralExpression;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTName;

import java.util.HashMap;
public class ConstContents {
    String value;
    String name;
    String typeQualifer;
    String type;
    String payLoadDataType;

    public ConstContents(CPPASTName iName, CPPASTLiteralExpression iValue, IToken iTQ, CPPASTName iType) throws Exception {
        value = iValue.getRawSignature();
        typeQualifer = iTQ.toString();
        name = iName.getRawSignature();
        type = iType.getRawSignature();
    }
}
