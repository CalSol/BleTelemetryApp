package com.example.bottomnav;

import org.junit.Test;
import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;

public class ParseTest {

    @Test
    public void doesNotExist() throws Exception {
        String code = "const uint16_t CAN_ID;";
        Parse test = new Parse(code.toCharArray());

        assertEquals(null, test.getConst("CAN_ID"));
        assertEquals(null, test.getConst("ducky"));
    }

    @Test
    public void simpleCheck() throws Exception {
        String code = "const uint16_t CAN_ID = 0x16;\n" +
                "const uint16_t CAN_ID2 = 0x32;\n" +
                "const uint16_t CAN_ID3 = 0x64;";
        Parse test = new Parse(code.toCharArray());
        HashMap<String, ConstContents> repository = test.constRepo;

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
        Parse test = new Parse(code.toCharArray());
        HashMap<String, ConstContents> repository = test.constRepo;
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
                "\tuint16_t voltage_be;\n" +
                "\tuint16_t current_be;\n" +
                "\tuint8_t control;\n" +
                "\tuint8_t reserved1;\n" +
                "\tuint8_t reserved2;\n" +
                "\tuint8_t reserved3;\n" +
                "};";
        Parse test = new Parse(code.toCharArray());
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
                "\tuint16_t voltage_be;\n" +
                "\tuint16_t current_be;\n" +
                "\tuint8_t control;\n" +
                "\tuint8_t reserved1;\n" +
                "\tuint8_t reserved2;\n" +
                "\tuint8_t reserved3;\n" +
                "};\n" +
                "const uint16_t CAN_ID = 0x16;\n" +
                "const uint16_t CAN_ID3 = 0x64;";
        Parse test = new Parse(code.toCharArray());
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
        Parse test = Parse.parseTextFile("parseData.h");
        HashMap<String, ConstContents> repository = test.constRepo;
        ArrayList<StructContents> struct = test.getStruct("ChargerControlStruct");

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

        assertEquals("voltage_be", struct.get(0).name);
        assertEquals("current_be", struct.get(1).name);
        assertEquals("control", struct.get(2).name);
        assertEquals("reserved1", struct.get(3).name);
        assertEquals("reserved2", struct.get(4).name);
        assertEquals("reserved3", struct.get(5).name);
        assertEquals("uint16_t", struct.get(0).type);
        assertEquals("uint16_t", struct.get(1).type);
        assertEquals("uint8_t", struct.get(2).type);
        assertEquals("uint8_t", struct.get(3).type);
        assertEquals("uint8_t", struct.get(4).type);
        assertEquals("uint8_t", struct.get(5).type);

        assertEquals("voltage_be", struct.get(0).name);
        assertEquals("current_be", struct.get(1).name);
        assertEquals("control", struct.get(2).name);
        assertEquals("reserved1", struct.get(3).name);
        assertEquals("reserved2", struct.get(4).name);
        assertEquals("reserved3", struct.get(5).name);
        assertEquals("uint16_t", struct.get(0).type);
        assertEquals("uint16_t", struct.get(1).type);
        assertEquals("uint8_t", struct.get(2).type);
        assertEquals("uint8_t", struct.get(3).type);
        assertEquals("uint8_t", struct.get(4).type);
        assertEquals("uint8_t", struct.get(5).type);
    }

    @Test
    public void checkIDToStructMap() throws Exception {
        Parse test = Parse.parseTextFile("parseData.h");
        
        assertEquals(test.getStruct("ChargerStatusStruct"),
                test.getCanStruct("CAN_CHARGER_STATUS"));
        assertEquals(test.getStruct("ChargerControlStruct"),
                test.getCanStruct("CAN_CHARGER_CONTROL"));
        assertEquals(test.getStruct("CanStrainGaugeStruct"),
                test.getCanStruct("CAN_STRAIN_DATA"));
        assertEquals(test.getStruct("CanPedalPosStruct"),
                test.getCanStruct("CAN_PEDAL_POS"));
    }

    @Test
    public void decodingSimple() throws Exception {
        Parse test = Parse.parseTextFile("parseData.h");
        byte[] payload1 = {0x0B, 0x00, 0x00, (byte) 0x0A5};
        test.decode(0x282, payload1);
    }

    @Test
    public void decodingSimple2() throws Exception {
        Parse test = Parse.parseTextFile("parseData.h");
        byte[] payload = {0x0B, 0x00, 0x00, (byte) 0xA5};
        int canID = 0x282;
        test.decode(canID, payload);

        assertEquals(11, (int) test.payloadMap.get("accelPos"));
        assertEquals(0, (int) test.payloadMap.get("brakePos"));
        assertEquals(0, (int) test.payloadMap.get("reserved1Pos"));
        assertEquals(-91, (int) test.payloadMap.get("reserved2Pos"));
    }
}
