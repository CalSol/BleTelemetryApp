package com.example.bottomnav;

import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTName;

public class StructContents {
    String name;
    String type;

    public StructContents(CPPASTName iName, CPPASTName iType) {
        name = iName.getRawSignature();
        type = iType.toString();
    }
}
