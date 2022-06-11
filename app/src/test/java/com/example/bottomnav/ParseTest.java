package com.example.bottomnav;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

public class ParseTest {

    @Test
    public void doesNotExist() throws Exception {
        String code = "const uint16_t CAN_ID;";
        Parse test = new Parse(code.toCharArray());

        assertEquals(null, test.getConstContents("CAN_ID"));
        assertEquals(null, test.getConstContents("ducky"));
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

        assertEquals("const", test.getConstContents("CAN_ID").typeQualifer);
        assertEquals("const", test.getConstContents("CAN_ID2").typeQualifer);
        assertEquals("const", test.getConstContents("CAN_ID3").typeQualifer);

        assertEquals("uint16_t", test.getConstContents("CAN_ID").type);
        assertEquals("uint16_t", test.getConstContents("CAN_ID2").type);
        assertEquals("uint16_t", test.getConstContents("CAN_ID3").type);

        assertEquals("0x16", test.getConstContents("CAN_ID").value);
        assertEquals("0x32", test.getConstContents("CAN_ID2").value);
        assertEquals("0x64", test.getConstContents("CAN_ID3").value);
    }

    @Test
    public void simpleCheck2() throws Exception {
        String code = "const uint16_t CAN_ID1 = 0x16;\n" +
                "const uint32_t CAN_ID3 = 0x64;\n" +
                "const uint32_t CAP_ID2 = 0x18;";
        Parse test = new Parse(code.toCharArray());
        HashMap<String, ConstContents> repository = test.constRepo;
        assertEquals("const", test.getConstContents("CAN_ID1").typeQualifer);
        assertEquals("const", test.getConstContents("CAP_ID2").typeQualifer);
        assertEquals("const", test.getConstContents("CAN_ID3").typeQualifer);
        assertEquals("uint16_t", test.getConstContents("CAN_ID1").type);
        assertEquals("uint32_t", test.getConstContents("CAP_ID2").type);
        assertEquals("uint32_t", test.getConstContents("CAN_ID3").type);
        assertEquals("0x16", test.getConstContents("CAN_ID1").value);
        assertEquals("0x18", test.getConstContents("CAP_ID2").value);
        assertEquals("0x64", test.getConstContents("CAN_ID3").value);
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
        ArrayList<StructContents> contents = test.getStructContents("ChargerControlStruct");

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
        ArrayList<StructContents> contents = test.getStructContents("ChargerControlStruct");

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

        assertEquals("const", test.getConstContents("CAN_ID").typeQualifer);
        assertEquals("uint16_t", test.getConstContents("CAN_ID").type);
        assertEquals("0x16", test.getConstContents("CAN_ID").value);

        assertEquals("const", test.getConstContents("CAN_ID3").typeQualifer);
        assertEquals("uint16_t", test.getConstContents("CAN_ID3").type);
        assertEquals("0x64", test.getConstContents("CAN_ID3").value);
    }

    @Test
    public void parseData() throws Exception {
        Parse test = Parse.parseTextFile("parseData.h");
        HashMap<String, ConstContents> repository = test.constRepo;
        ArrayList<StructContents> struct = test.getStructContents("ChargerControlStruct");

        assertEquals(true, repository.containsKey("CAN_HEART_BMS"));
        assertEquals(true, repository.containsKey("CAN_BMS_FAN_SETPOINT"));
        assertEquals(true, repository.containsKey("CAN_CHARGER_STATUS"));

        assertEquals("const", test.getConstContents("CAN_HEART_BMS").typeQualifer);
        assertEquals("const", test.getConstContents("CAN_BMS_FAN_SETPOINT").typeQualifer);
        assertEquals("const", test.getConstContents("CAN_CHARGER_STATUS").typeQualifer);

        assertEquals("uint16_t", test.getConstContents("CAN_HEART_BMS").type);
        assertEquals("uint16_t", test.getConstContents("CAN_BMS_FAN_SETPOINT").type);
        assertEquals("uint16_t", test.getConstContents("CAN_CHARGER_STATUS").type);

        assertEquals("0x040", test.getConstContents("CAN_HEART_BMS").value);
        assertEquals("0x560", test.getConstContents("CAN_BMS_FAN_SETPOINT").value);
        assertEquals("0x18FF50E5", test.getConstContents("CAN_CHARGER_STATUS").value);

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
        
        assertEquals(test.getStructContents("ChargerStatusStruct"),
                test.getCanStruct("CAN_CHARGER_STATUS"));
        assertEquals(test.getStructContents("ChargerControlStruct"),
                test.getCanStruct("CAN_CHARGER_CONTROL"));
        assertEquals(test.getStructContents("CanStrainGaugeStruct"),
                test.getCanStruct("CAN_STRAIN_DATA"));
        assertEquals(test.getStructContents("CanPedalPosStruct"),
                test.getCanStruct("CAN_PEDAL_POS"));
    }

    @Test
    public void decodingSimple() throws Exception {
        Parse parsedFile = Parse.parseTextFile("decode.h");
        byte[] packedFloatPayload = {0x71, (byte) 0xFD, 0x47, 0x41};
        byte[] twovarPayload = {0x0B, 0x00, 0x00, (byte) 0xA5};
        byte[] fourVarPayload = {(byte) 0xED,  (byte) 0xC7, 0x00, 0x43,
                (byte) 0xD1, 0x53, 0x23, 0x41};

        Translation test = new Translation(parsedFile);
        test.decode(0x310, packedFloatPayload);
        test.decode(0x282, twovarPayload);


        test.decode(0x310, packedFloatPayload);
        DecodedFloat float1 = (DecodedFloat) test.getValue(0x310);
        assertEquals(12.499375343322754, float1.value, 0.000000000000001);
        /**
        DecodedInteger int1 = (DecodedInteger) test.payLoadMap.get("accelPos");
        DecodedInteger int2 = (DecodedInteger) test.payLoadMap.get("brakePos");
        DecodedInteger int3 = (DecodedInteger) test.payLoadMap.get("reserved1Pos");
        DecodedInteger int4 = (DecodedInteger) test.payLoadMap.get("reserved2Pos");

        test.decode(0x282, twovarPayload);
        assertEquals(11, int1.value);
        assertEquals(0, int2.value);
        assertEquals(0, int3.value);
        assertEquals(165, int4.value);

        test.decode(0x402, fourVarPayload);
        assertEquals(10.2, (float) test.payloadMap.get("mps"), 0.01);
        assertEquals(128.781, (float) test.payloadMap.get("rpm"), 0.001); */
    }
}
