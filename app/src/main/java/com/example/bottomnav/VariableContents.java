package com.example.bottomnav;

import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTName;

public class VariableContents {
    String name;
    String payloadDataType;

    public VariableContents(String  iName, String iType) {
        name = iName;
        payloadDataType = iType;
    }
}
