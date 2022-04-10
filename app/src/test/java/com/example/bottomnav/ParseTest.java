package com.example.bottomnav;


import java.io.*;
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

    @Test
    public void parseData() throws Exception {

        InputStream is = getClass().getClassLoader().getResourceAsStream("data2.txt");
        ByteArrayOutputStream barOutStream = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int length;
        while ((length = is.read(buf)) != -1) {
            barOutStream.write(buf, 0, length);
        }

        Parse test = new Parse(barOutStream.toString());
        HashMap<String, Contents> repository = test.constRepo;
        ArrayList<StructContents> struct1 = test.getStruct("ChargerControlStruct");
        ArrayList<StructContents> struct2 = test.getStruct("ChargerControlStruct");

        assertEquals(true, repository.containsKey("CAN_HEART_BMS"));
        assertEquals(true, repository.containsKey("CAN_BMS_FAN_SETPOINT"));
        assertEquals(true, repository.containsKey("CAN_CHARGER_STATUS"));

        assertEquals("const", test.getConst("CAN_HEART_BMS").typeQualifer);
        assertEquals("const", test.getConst("CAN_BMS_FAN_SETPOINT").typeQualifer);
        assertEquals("const", test.getConst("CAN_CHARGER_STATUS").typeQualifer);

        assertEquals("uint16_t", test.getConst("CAN_HEART_BMS").type);
        assertEquals("uint16_t", test.getConst("CAN_BMS_FAN_SETPOINT").type);
        assertEquals("uint16_t", test.getConst("CAN_CHARGER_STATUS").type);

        assertEquals("0x040", test.getConst("CAN_HEART_BMS").value);
        assertEquals("0x560", test.getConst("CAN_BMS_FAN_SETPOINT").value);
        assertEquals("0x18FF50E5", test.getConst("CAN_CHARGER_STATUS").value);

        assertEquals("voltage_be", struct1.get(0).name);
        assertEquals("current_be", struct1.get(1).name);
        assertEquals("control", struct1.get(2).name);
        assertEquals("reserved1", struct1.get(3).name);
        assertEquals("reserved2", struct1.get(4).name);
        assertEquals("reserved3", struct1.get(5).name);
        assertEquals("uint16_t", struct1.get(0).type);
        assertEquals("uint16_t", struct1.get(1).type);
        assertEquals("uint8_t", struct1.get(2).type);
        assertEquals("uint8_t", struct1.get(3).type);
        assertEquals("uint8_t", struct1.get(4).type);
        assertEquals("uint8_t", struct1.get(5).type);

        assertEquals("voltage_be", struct2.get(0).name);
        assertEquals("current_be", struct2.get(1).name);
        assertEquals("control", struct2.get(2).name);
        assertEquals("reserved1", struct2.get(3).name);
        assertEquals("reserved2", struct2.get(4).name);
        assertEquals("reserved3", struct2.get(5).name);
        assertEquals("uint16_t", struct2.get(0).type);
        assertEquals("uint16_t", struct2.get(1).type);
        assertEquals("uint8_t", struct2.get(2).type);
        assertEquals("uint8_t", struct2.get(3).type);
        assertEquals("uint8_t", struct2.get(4).type);
        assertEquals("uint8_t", struct2.get(5).type);

    }
}
