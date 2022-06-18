package com.example.bottomnav;

import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTName;

public class StructContents {
    String name;
    String payloadDataType;

    public StructContents(CPPASTName iName, CPPASTName iType) {
        name = iName.getRawSignature();
        payloadDataType = iType.toString();
    }
}
