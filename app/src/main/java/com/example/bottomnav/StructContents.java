package com.example.bottomnav;

import org.eclipse.cdt.core.parser.IToken;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTLiteralExpression;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTName;

import java.util.ArrayList;
import java.util.HashMap;

public class StructContents {
    public ArrayList structContents = new ArrayList();
    public StructContents() throws Exception {
    }

    public void save(Contents con) {
        structContents.add(con);
    }
}
