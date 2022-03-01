package com.company;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ParseTest {

    @Test
    public void doesNotExist() throws Exception {
        String code = "const uint16_t CAN_ID; \n" +
                "const CAN_ID2 = 0x32; \n" +
                "CAN_ID3 = 0x64;";
        Parse test = new Parse(code);

        Assertions.assertThrows(IllegalAccessError.class, () -> test.get("CAN_ID"));
        Assertions.assertThrows(IllegalAccessError.class, () -> test.get("CAN_ID2"));
        Assertions.assertThrows(IllegalAccessError.class, () -> test.get("CAN_ID3"));

        IllegalAccessError exception1 = assertThrows(IllegalAccessError.class, () -> test.get("CAN_ID"));
        IllegalAccessError exception2 = assertThrows(IllegalAccessError.class, () -> test.get("CAN_ID"));
        IllegalAccessError exception3 = assertThrows(IllegalAccessError.class, () -> test.get("CAN_ID"));

        Assert.assertEquals("The name does not exist!", exception1.getMessage());
        Assert.assertEquals("The name does not exist!", exception2.getMessage());
        Assert.assertEquals("The name does not exist!", exception3.getMessage());
    }

    @Test
    public void simpleCheck() throws Exception {
        String code = "const uint16_t CAN_ID = 0x16; \n" +
                "const uint16_t CAN_ID2 = 0x32; \n" +
                "const uint16_t CAN_ID3 = 0x64;";

        Parse test = new Parse(code);

        HashMap<String, Contents> repository = test.repo;

        Assert.assertEquals(true, repository.containsKey("CAN_ID"));
        Assert.assertEquals(true, repository.containsKey("CAN_ID2"));
        Assert.assertEquals(true, repository.containsKey("CAN_ID3"));

        Assert.assertEquals("const", test.get("CAN_ID").typeQualifer);
        Assert.assertEquals("const", test.get("CAN_ID2").typeQualifer);
        Assert.assertEquals("const", test.get("CAN_ID3").typeQualifer);

        Assert.assertEquals("uint16_t", test.get("CAN_ID").type);
        Assert.assertEquals("uint16_t", test.get("CAN_ID2").type);
        Assert.assertEquals("uint16_t", test.get("CAN_ID3").type);

        Assert.assertEquals("0x16", test.get("CAN_ID").value);
        Assert.assertEquals("0x32", test.get("CAN_ID2").value);
        Assert.assertEquals("0x64", test.get("CAN_ID3").value);
    }

    @Test
    public void checkdiverse() throws Exception {
        String code = "const uint16_t CAN_ID1 = 0x16;\n" +
                "const uint32_t CAN_ID3 = 0x64;\n" +
                "const uint32_t CAP_ID2 = 0x18;";

        Parse test = new Parse(code);

        HashMap<String, Contents> repository = test.repo;

        Assert.assertEquals(true, repository.containsKey("CAN_ID1"));
        Assert.assertEquals(true, repository.containsKey("CAP_ID2"));
        Assert.assertEquals(true, repository.containsKey("CAN_ID3"));

        Assert.assertEquals("const", test.get("CAN_ID1").typeQualifer);
        Assert.assertEquals("const", test.get("CAP_ID2").typeQualifer);
        Assert.assertEquals("const", test.get("CAN_ID3").typeQualifer);

        Assert.assertEquals("uint16_t", test.get("CAN_ID1").type);
        Assert.assertEquals("uint32_t", test.get("CAP_ID2").type);
        Assert.assertEquals("uint32_t", test.get("CAN_ID3").type);

        Assert.assertEquals("0x16", test.get("CAN_ID1").value);
        Assert.assertEquals("0x18", test.get("CAP_ID2").value);
        Assert.assertEquals("0x64", test.get("CAN_ID3").value);
    }

    @Test
    public void listCheck() throws Exception {
        String code = "const uint16_t CAN_ID1 = 0x16; \n" +
                "const uint32_t CAN_ID2 = 0x32; \n" +
                "const uint16_t CAN_ID3 = 0x64;";
        Parse test = new Parse(code);

        Assertions.assertThrows(IllegalArgumentException.class, () -> test.getByType("cons"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> test.getByType("co"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> test.getByType("cns"));

        ArrayList<Contents> list = test.getByType("uint16_t");
        Assert.assertEquals("0x16", list.get(0).value);
        Assert.assertEquals("0x64", list.get(1).value);

        ArrayList<Contents> list2 = test.getByType("uint32_t");
        Assert.assertEquals("0x32", list2.get(0).value);
    }

}
