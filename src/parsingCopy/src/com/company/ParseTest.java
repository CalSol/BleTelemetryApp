package com.company;

import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class ParseTest {
    @Test
    public void testConst() throws Exception {
        String code = "const uint32_t CAN_PETALS_POS = 0x42;\n" +
                "const uint32_t CAN_WHEELS_POS = 0x43;\n" +
                "const uint32_t CAN_TIRES_POS = 0x44;\n" +
                "const uint32_t CAN_FRONT_RPM = 0x45;\n" +
                "const uint42_t CAN_BACK_RPM = 0x46;\n" +
                "const uint42_t CAN_TEMP_CEL = 0x47;\n" +
                "const uint42_t CAN_MOTOR_RPM = 0x48;\n";

        Parse test = new Parse(code);
        HashMap constant30 = test.contents.get("const uint32_t");
        HashMap constant40 = test.contents.get("const uint42_t");

        Assert.assertEquals("0x43", constant30.get("CAN_WHEELS_POS"));
        Assert.assertEquals("0x44", constant30.get("CAN_TIRES_POS"));
        Assert.assertEquals("0x45", constant30.get("CAN_FRONT_RPM"));

        Assert.assertEquals("0x46", constant40.get("CAN_BACK_RPM"));
        Assert.assertEquals("0x47", constant40.get("CAN_TEMP_CEL"));
        Assert.assertEquals("0x48", constant40.get("CAN_MOTOR_RPM"));
    }
}