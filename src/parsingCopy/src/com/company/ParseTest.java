package com.company;

import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTLiteralExpression;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTName;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParseTest {
    @Test
    public void checkLitExpr() throws Exception {
        String code = "const uint16_t CAN_ID = 0x16; \n" +
                "const uint16_t CAN_ID2 = 0x32; \n" +
                "const uint16_t CAN_ID3 = 0x64;";

        Parse test = new Parse(code);

        String value1 = "0x16";
        String value2 = "0x32";
        String value3 = "0x64";

        String n1 = "CAN_ID";
        String n2 = "CAN_ID2";
        String n3 = "CAN_ID3";

        CPPASTName name1 = new CPPASTName(n1.toCharArray());
        CPPASTName name2 = new CPPASTName(n2.toCharArray());
        CPPASTName name3 = new CPPASTName(n3.toCharArray());

        String litExpr1 = test.get(name1).getRawSignature();
        String litExpr2 = test.get(name2).getRawSignature();
        String litExpr3 = test.get(name3).getRawSignature();

        Assert.assertEquals(value1, litExpr1);
        Assert.assertEquals(value2, litExpr2);
        Assert.assertEquals(value3, litExpr3);
    }

    @Test
    public void checkLitExprdiverse() throws Exception {
        String code = "const uint16_t CAN_ID = 0x16; \n" +
                "const uint32_t CAN_ID2 = 0x32; \n" +
                "const uint32_t CAN_ID3 = 0x64;\n" +
                "define CAN_HEART_MCC_RIGHT = 0x043; \n" +
                "define CAN_HEART_DASHBOARD = 0x044; \n" +
                "define CAN_HEART_POWERHUB_TOP = 0x045;\n" +
                "const uint16_t CAP_ID = 0x17; \n" +
                "const uint32_t CAP_ID2 = 0x18; \n" +
                "const uint32_t CAP_ID3 = 0x19;";

        Parse test = new Parse(code);

        String value1 = "0x16";
        String value2 = "0x32";
        String value3 = "0x64";
        String value4 = "0x043";
        String value5 = "0x044";
        String value6 = "0x045";
        String value7 = "0x17";
        String value8 = "0x18";
        String value9 = "0x19";

        String n1 = "CAN_ID";
        String n2 = "CAN_ID2";
        String n3 = "CAN_ID3";
        String n4 = "CAN_HEART_MCC_RIGHT";
        String n5 = "CAN_HEART_DASHBOARD";
        String n6 = "CAN_HEART_POWERHUB_TOP";
        String n7 = "CAP_ID";
        String n8 = "CAP_ID2";
        String n9 = "CAP_ID3";

        CPPASTName name1 = new CPPASTName(n1.toCharArray());
        CPPASTName name2 = new CPPASTName(n2.toCharArray());
        CPPASTName name3 = new CPPASTName(n3.toCharArray());
        CPPASTName name4 = new CPPASTName(n4.toCharArray());
        CPPASTName name5 = new CPPASTName(n5.toCharArray());
        CPPASTName name6 = new CPPASTName(n6.toCharArray());
        CPPASTName name7 = new CPPASTName(n7.toCharArray());
        CPPASTName name8 = new CPPASTName(n8.toCharArray());
        CPPASTName name9 = new CPPASTName(n9.toCharArray());

        String litExpr1 = test.get(name1).getRawSignature();
        String litExpr2 = test.get(name2).getRawSignature();
        String litExpr3 = test.get(name3).getRawSignature();
        String litExpr4 = test.get(name4).getRawSignature();
        String litExpr5 = test.get(name5).getRawSignature();
        String litExpr6 = test.get(name6).getRawSignature();
        String litExpr7 = test.get(name7).getRawSignature();
        String litExpr8 = test.get(name8).getRawSignature();
        String litExpr9 = test.get(name9).getRawSignature();

        Assert.assertEquals(value1, litExpr1);
        Assert.assertEquals(value2, litExpr2);
        Assert.assertEquals(value3, litExpr3);
        Assert.assertEquals(value4, litExpr4);
        Assert.assertEquals(value5, litExpr5);
        Assert.assertEquals(value6, litExpr6);
        Assert.assertEquals(value7, litExpr7);
        Assert.assertEquals(value8, litExpr8);
        Assert.assertEquals(value9, litExpr9);


    }
}
