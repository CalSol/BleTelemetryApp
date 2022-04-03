package com.example.bottomnav;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;


public class ParseTest {

    @Test
    public void doesNotExist() throws Exception {
        String code = "const uint16_t CAN_ID;";
        Parse test = new Parse(code);

        assertEquals(null, test.getConst("CAN_ID"));
        assertEquals(null, test.getConst("ducky"));
    }

    @Test
    public void simpleCheck() throws Exception {
        String code = "const uint16_t CAN_ID = 0x16; \n" +
                "const uint16_t CAN_ID2 = 0x32; \n" +
                "const uint16_t CAN_ID3 = 0x64;";

        Parse test = new Parse(code);

        HashMap<String, Contents> repository = test.constRepo;

        assertEquals(true, repository.containsKey("CAN_ID"));
        assertEquals(true, repository.containsKey("CAN_ID2"));
        assertEquals(true, repository.containsKey("CAN_ID3"));

        assertEquals("const", test.getConst("CAN_ID").typeQualifer);
        assertEquals("const", test.getConst("CAN_ID2").typeQualifer);
        assertEquals("const", test.getConst("CAN_ID3").typeQualifer);

        assertEquals("uint16_t", test.getConst("CAN_ID").type);
        assertEquals("uint16_t", test.getConst("CAN_ID2").type);
        assertEquals("uint16_t", test.getConst("CAN_ID3").type);

        assertEquals("0x16", test.getConst("CAN_ID").value);
        assertEquals("0x32", test.getConst("CAN_ID2").value);
        assertEquals("0x64", test.getConst("CAN_ID3").value);
    }

    @Test
    public void simpleCheck2() throws Exception {
        String code = "const uint16_t CAN_ID1 = 0x16;\n" +
                "const uint32_t CAN_ID3 = 0x64;\n" +
                "const uint32_t CAP_ID2 = 0x18;";
        Parse test = new Parse(code);
        HashMap<String, Contents> repository = test.constRepo;
        assertEquals("const", test.getConst("CAN_ID1").typeQualifer);
        assertEquals("const", test.getConst("CAP_ID2").typeQualifer);
        assertEquals("const", test.getConst("CAN_ID3").typeQualifer);
        assertEquals("uint16_t", test.getConst("CAN_ID1").type);
        assertEquals("uint32_t", test.getConst("CAP_ID2").type);
        assertEquals("uint32_t", test.getConst("CAN_ID3").type);
        assertEquals("0x16", test.getConst("CAN_ID1").value);
        assertEquals("0x18", test.getConst("CAP_ID2").value);
        assertEquals("0x64", test.getConst("CAN_ID3").value);
    }

    @Test
    public void structs() throws Exception {
        String code = "struct ChargerControlStruct {\n" +
                "  uint16_t voltage_be;\n" +
                "  uint16_t current_be;\n" +
                "  uint8_t control;\n" +
                "  uint8_t reserved1;\n" +
                "  uint8_t reserved2;\n" +
                "  uint8_t reserved3;\n" +
                "};";
        Parse test = new Parse(code);
        HashMap<String, ArrayList<StructContents>> structRepo = test.structRepo;
        ArrayList<StructContents> contents = test.getStruct("ChargerControlStruct");
        assertEquals("voltage_be", contents.get(0).name);
        assertEquals("current_be", contents.get(1).name);
        assertEquals("control", contents.get(2).name);
        assertEquals("reserved1", contents.get(3).name);
        assertEquals("reserved2", contents.get(4).name);
        assertEquals("reserved3", contents.get(5).name);
        assertEquals("uint16_t", contents.get(0).type);
        assertEquals("uint16_t", contents.get(1).type);
        assertEquals("uint8_t", contents.get(2).type);
        assertEquals("uint8_t", contents.get(3).type);
        assertEquals("uint8_t", contents.get(4).type);
        assertEquals("uint8_t", contents.get(5).type);
    }

    @Test
    public void structNConst() throws Exception {
        String code = "struct ChargerControlStruct {\n" +
                "  uint16_t voltage_be;\n" +
                "  uint16_t current_be;\n" +
                "  uint8_t control;\n" +
                "  uint8_t reserved1;\n" +
                "  uint8_t reserved2;\n" +
                "  uint8_t reserved3;\n" +
                "};\n" +
                "const uint16_t CAN_ID = 0x16;\n" +
                "const uint16_t CAN_ID3 = 0x64;";
        Parse test = new Parse(code);

        ArrayList<StructContents> contents = test.getStruct("ChargerControlStruct");

        assertEquals("voltage_be", contents.get(0).name);
        assertEquals("current_be", contents.get(1).name);
        assertEquals("control", contents.get(2).name);
        assertEquals("reserved1", contents.get(3).name);
        assertEquals("reserved2", contents.get(4).name);
        assertEquals("reserved3", contents.get(5).name);

        assertEquals("uint16_t", contents.get(0).type);
        assertEquals("uint16_t", contents.get(1).type);
        assertEquals("uint8_t", contents.get(2).type);
        assertEquals("uint8_t", contents.get(3).type);
        assertEquals("uint8_t", contents.get(4).type);
        assertEquals("uint8_t", contents.get(5).type);

        assertEquals("const", test.getConst("CAN_ID").typeQualifer);
        assertEquals("uint16_t", test.getConst("CAN_ID").type);
        assertEquals("0x16", test.getConst("CAN_ID").value);

        assertEquals("const", test.getConst("CAN_ID3").typeQualifer);
        assertEquals("uint16_t", test.getConst("CAN_ID3").type);
        assertEquals("0x64", test.getConst("CAN_ID3").value);
    }
}
