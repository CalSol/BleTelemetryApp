package com.example.bottomnav;

import org.junit.Test;

import static org.junit.Assert.*;


class ParseTest {

    @Test
    public void doesNotExist() throws Exception {
        String code = "const uint16_t CAN_ID; \n" +
                "const CAN_ID2 = 0x32; \n" +
                "CAN_ID3 = 0x64;";
        Parse test = new Parse(code);

        assertThrows(IllegalAccessError.class, () -> test.get("CAN_ID"));
        assertThrows(IllegalAccessError.class, () -> test.get("CAN_ID2"));
        assertThrows(IllegalAccessError.class, () -> test.get("CAN_ID3"));

        IllegalAccessError exception1 = assertThrows(IllegalAccessError.class, () -> test.get("CAN_ID"));
        IllegalAccessError exception2 = assertThrows(IllegalAccessError.class, () -> test.get("CAN_ID"));
        IllegalAccessError exception3 = assertThrows(IllegalAccessError.class, () -> test.get("CAN_ID"));

        assertEquals("The name does not exist!", exception1.getMessage());
        assertEquals("The name does not exist!", exception2.getMessage());
        assertEquals("The name does not exist!", exception3.getMessage());
    }

    @Test
    public void simpleCheck() throws Exception {
        String code = "const uint16_t CAN_ID = 0x16; \n" +
                "const uint16_t CAN_ID2 = 0x32; \n" +
                "const uint16_t CAN_ID3 = 0x64;";

        Parse test = new Parse(code);

        HashMap<String, Contents> repository = test.repo;

        assertEquals(true, repository.containsKey("CAN_ID"));
        assertEquals(true, repository.containsKey("CAN_ID2"));
        assertEquals(true, repository.containsKey("CAN_ID3"));

        assertEquals("const", test.get("CAN_ID").typeQualifer);
        assertEquals("const", test.get("CAN_ID2").typeQualifer);
        assertEquals("const", test.get("CAN_ID3").typeQualifer);

        assertEquals("uint16_t", test.get("CAN_ID").type);
        assertEquals("uint16_t", test.get("CAN_ID2").type);
        assertEquals("uint16_t", test.get("CAN_ID3").type);

        assertEquals("0x16", test.get("CAN_ID").value);
        assertEquals("0x32", test.get("CAN_ID2").value);
        assertEquals("0x64", test.get("CAN_ID3").value);
    }

    @Test
    public void checkdiverse() throws Exception {
        String code = "const uint16_t CAN_ID1 = 0x16;\n" +
                "const uint32_t CAN_ID3 = 0x64;\n" +
                "const uint32_t CAP_ID2 = 0x18;";

        Parse test = new Parse(code);

        HashMap<String, Contents> repository = test.repo;

        assertEquals(true, repository.containsKey("CAN_ID1"));
        assertEquals(true, repository.containsKey("CAP_ID2"));
        assertEquals(true, repository.containsKey("CAN_ID3"));

        assertEquals("const", test.get("CAN_ID1").typeQualifer);
        assertEquals("const", test.get("CAP_ID2").typeQualifer);
        assertEquals("const", test.get("CAN_ID3").typeQualifer);

        assertEquals("uint16_t", test.get("CAN_ID1").type);
        assertEquals("uint32_t", test.get("CAP_ID2").type);
        assertEquals("uint32_t", test.get("CAN_ID3").type);

        assertEquals("0x16", test.get("CAN_ID1").value);
        assertEquals("0x18", test.get("CAP_ID2").value);
        assertEquals("0x64", test.get("CAN_ID3").value);
    }

    @Test
    public void listCheck() throws Exception {
        String code = "const uint16_t CAN_ID1 = 0x16; \n" +
                "const uint32_t CAN_ID2 = 0x32; \n" +
                "const uint16_t CAN_ID3 = 0x64;";
        Parse test = new Parse(code);

        assertThrows(IllegalArgumentException.class, () -> test.getByType("cons"));
        assertThrows(IllegalArgumentException.class, () -> test.getByType("co"));
        assertThrows(IllegalArgumentException.class, () -> test.getByType("cns"));

        ArrayList<Contents> list = test.getByType("uint16_t");
        assertEquals("0x16", list.get(0).value);
        assertEquals("0x64", list.get(1).value);

        ArrayList<Contents> list2 = test.getByType("uint32_t");
        assertEquals("0x32", list2.get(0).value);
    }

}
