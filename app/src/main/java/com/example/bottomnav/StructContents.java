package com.example.bottomnav;

import org.eclipse.cdt.core.parser.IToken;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTLiteralExpression;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTName;

import java.util.ArrayList;
import java.util.HashMap;

public class StructContents {
    String name;
    String type;
    public StructContents(CPPASTName iName, CPPASTName iType) {
        name = iName.getRawSignature();
        type = iType.getRawSignature();
    }
}
