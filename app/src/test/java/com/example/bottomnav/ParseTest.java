package com.example.bottomnav;

import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class ParseTest {

    @Test
    public void doesNotExist() throws Exception {
        String code = "const uint16_t CAN_ID;";
        Parse test = new Parse(code.toCharArray());

        assertEquals(false, test.getDecoder("CAN_ID").isPresent());
        assertEquals(false, test.getDecoder("ducky").isPresent());
    }

    @Test
    public void simpleCheck() throws Exception {
        String code = "const uint16_t CAN_ID = 0x16;\n" +
                "const uint16_t CAN_ID2 = 0x32;\n" +
                "const uint16_t CAN_ID3 = 0x64;";
        Parse test = new Parse(code.toCharArray());

        assertEquals("const", test.getDecoder("CAN_ID").get().getContents().typeQualifer);
        assertEquals("const", test.getDecoder("CAN_ID2").get().getContents().typeQualifer);
        assertEquals("const", test.getDecoder("CAN_ID3").get().getContents().typeQualifer);

        assertEquals("uint16_t", test.getDecoder("CAN_ID").get().getContents().payloadDataType);
        assertEquals("uint16_t", test.getDecoder("CAN_ID2").get().getContents().payloadDataType);
        assertEquals("uint16_t", test.getDecoder("CAN_ID3").get().getContents().payloadDataType);

        assertEquals("0x16", test.getDecoder("CAN_ID").get().getContents().value);
        assertEquals("0x32", test.getDecoder("CAN_ID2").get().getContents().value);
        assertEquals("0x64", test.getDecoder("CAN_ID3").get().getContents().value);
    }

    @Test
    public void simpleCheck2() throws Exception {
        String code = "const uint16_t CAN_ID1 = 0x16;\n" +
                "const uint32_t CAN_ID3 = 0x64;\n" +
                "const uint32_t CAN_ID2 = 0x18;";
        Parse test = new Parse(code.toCharArray());

        assertEquals("const", test.getConstContents("CAN_ID1").typeQualifer);
        assertEquals("const", test.getConstContents("CAN_ID2").typeQualifer);
        assertEquals("const", test.getConstContents("CAN_ID3").typeQualifer);
        assertEquals("uint16_t", test.getConstContents("CAN_ID1").payloadDataType);
        assertEquals("uint32_t", test.getConstContents("CAN_ID2").payloadDataType);
        assertEquals("uint32_t", test.getConstContents("CAN_ID3").payloadDataType);
        assertEquals("0x16", test.getConstContents("CAN_ID1").value);
        assertEquals("0x18", test.getConstContents("CAN_ID2").value);
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
        ArrayList<VariableContents> contents = test.getStructContents("ChargerControlStruct");

        assertEquals("voltage_be", contents.get(0).name);
        assertEquals("current_be", contents.get(1).name);
        assertEquals("control", contents.get(2).name);
        assertEquals("reserved1", contents.get(3).name);
        assertEquals("reserved2", contents.get(4).name);
        assertEquals("reserved3", contents.get(5).name);
        assertEquals("uint16_t", contents.get(0).payloadDataType);
        assertEquals("uint16_t", contents.get(1).payloadDataType);
        assertEquals("uint8_t", contents.get(2).payloadDataType);
        assertEquals("uint8_t", contents.get(3).payloadDataType);
        assertEquals("uint8_t", contents.get(4).payloadDataType);
        assertEquals("uint8_t", contents.get(5).payloadDataType);
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
                "const uint16_t CAN_ID2 = 0x64;";
        Parse test = new Parse(code.toCharArray());
        ArrayList<VariableContents> contents = test.getStructContents("ChargerControlStruct");

        assertEquals("voltage_be", contents.get(0).name);
        assertEquals("current_be", contents.get(1).name);
        assertEquals("control", contents.get(2).name);
        assertEquals("reserved1", contents.get(3).name);
        assertEquals("reserved2", contents.get(4).name);
        assertEquals("reserved3", contents.get(5).name);

        assertEquals("uint16_t", contents.get(0).payloadDataType);
        assertEquals("uint16_t", contents.get(1).payloadDataType);
        assertEquals("uint8_t", contents.get(2).payloadDataType);
        assertEquals("uint8_t", contents.get(3).payloadDataType);
        assertEquals("uint8_t", contents.get(4).payloadDataType);
        assertEquals("uint8_t", contents.get(5).payloadDataType);

        assertEquals("const", test.getConstContents("CAN_ID").typeQualifer);
        assertEquals("uint16_t", test.getConstContents("CAN_ID").payloadDataType);
        assertEquals("0x16", test.getConstContents("CAN_ID").value);

        assertEquals("const", test.getConstContents("CAN_ID2").typeQualifer);
        assertEquals("uint16_t", test.getConstContents("CAN_ID2").payloadDataType);
        assertEquals("0x64", test.getConstContents("CAN_ID2").value);
    }

    @Test
    public void parseData() throws Exception {
        Parse test = Parse.parseTextFile("parseData.h");
        ArrayList<VariableContents> contents = test.getStructContents("ChargerControlStruct");

        assertEquals("const", test.getConstContents("CAN_HEART_BMS").typeQualifer);
        assertEquals("const", test.getConstContents("CAN_BMS_FAN_SETPOINT").typeQualifer);
        assertEquals("const", test.getConstContents("CAN_CHARGER_STATUS").typeQualifer);

        assertEquals("uint16_t", test.getConstContents("CAN_HEART_BMS").payloadDataType);
        assertEquals("uint16_t", test.getConstContents("CAN_BMS_FAN_SETPOINT").payloadDataType);
        assertEquals("uint16_t", test.getConstContents("CAN_CHARGER_STATUS").payloadDataType);

        assertEquals("0x040", test.getConstContents("CAN_HEART_BMS").value);
        assertEquals("0x560", test.getConstContents("CAN_BMS_FAN_SETPOINT").value);
        assertEquals("0x18FF50E5", test.getConstContents("CAN_CHARGER_STATUS").value);

        assertEquals("voltage_be", contents.get(0).name);
        assertEquals("current_be", contents.get(1).name);
        assertEquals("control", contents.get(2).name);
        assertEquals("reserved1", contents.get(3).name);
        assertEquals("reserved2", contents.get(4).name);
        assertEquals("reserved3", contents.get(5).name);
        assertEquals("uint16_t", contents.get(0).payloadDataType);
        assertEquals("uint16_t", contents.get(1).payloadDataType);
        assertEquals("uint8_t", contents.get(2).payloadDataType);
        assertEquals("uint8_t", contents.get(3).payloadDataType);
        assertEquals("uint8_t", contents.get(4).payloadDataType);
        assertEquals("uint8_t", contents.get(5).payloadDataType);

        assertEquals("voltage_be", contents.get(0).name);
        assertEquals("current_be", contents.get(1).name);
        assertEquals("control", contents.get(2).name);
        assertEquals("reserved1", contents.get(3).name);
        assertEquals("reserved2", contents.get(4).name);
        assertEquals("reserved3", contents.get(5).name);
        assertEquals("uint16_t", contents.get(0).payloadDataType);
        assertEquals("uint16_t", contents.get(1).payloadDataType);
        assertEquals("uint8_t", contents.get(2).payloadDataType);
        assertEquals("uint8_t", contents.get(3).payloadDataType);
        assertEquals("uint8_t", contents.get(4).payloadDataType);
        assertEquals("uint8_t", contents.get(5).payloadDataType);
    }

    @Test
    public void checkIDToStructMap() throws Exception {
        Parse test = Parse.parseTextFile("parseData.h");
        
        assertEquals(test.getStructContents("ChargerStatusStruct"),
                ((StructDecoder) test.getDecoder("CAN_CHARGER_STATUS").get()).contents);
        assertEquals(test.getStructContents("ChargerControlStruct"),
                ((StructDecoder) test.getDecoder("CAN_CHARGER_CONTROL").get()).contents);
        assertEquals(test.getStructContents("CanStrainGaugeStruct"),
                ((StructDecoder) test.getDecoder("CAN_STRAIN_DATA").get()).contents);
        assertEquals(test.getStructContents("CanPedalPosStruct"),
                ((StructDecoder) test.getDecoder("CAN_PEDAL_POS").get()).contents);
    }

    @Test
    public void decodeIntegers() throws Exception {
        Parse test = Parse.parseTextFile("integers.h");

        byte[] eight = {(byte) 0xff};
        assertEquals("BRUH: 255", test.decode(0x402, eight));
        assertEquals("YOUNG: -1", test.decode(0x403, eight));

        byte[] sixteen = {(byte) 0xff, (byte) 0xff};
        assertEquals("FLAME: 65535", test.decode(0x404, sixteen));
        assertEquals("HEIN: -1", test.decode(0x405, sixteen));

        byte[] thirtytwo = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
        assertEquals("SICKO: 4294967295", test.decode(0x406, thirtytwo));
        assertEquals("MODE: -1", test.decode(0x407, thirtytwo));

    }

    @Test
    public void decodingVariety() throws Exception {
        Parse test = Parse.parseTextFile("decode.h");

        byte[] packedFloatPayload = {0x71, (byte) 0xFD, 0x47, 0x41};
        String packedFloatMessage = "PACKED_FLOAT: 12.499375";
        assertEquals(packedFloatMessage, test.decode(0x310, packedFloatPayload));
        FloatDecoder floatDec = (FloatDecoder) test.getDecoder(0x310).get();
        assertEquals(12.499375343322754,(float) floatDec.valueToRaw(), 0.000000000000001);

        byte[] var4Payload = {0x0B, 0x00, 0x00, (byte) 0xA5};
        String var4PaylodMessage = "accelPos: 11\n" +
                "brakePos: 0\n" +
                "reserved1Pos: 0\n" +
                "reserved2Pos: 165\n";
        assertEquals(var4PaylodMessage, test.decode(0x282, var4Payload));
        StructDecoder struDec = (StructDecoder) test.getDecoder(0x282).get();
        IntegerDecoder int1 = (IntegerDecoder) struDec.getValue("accelPos");
        IntegerDecoder int2 = (IntegerDecoder) struDec.getValue("brakePos");
        IntegerDecoder int3 = (IntegerDecoder) struDec.getValue("reserved1Pos");
        IntegerDecoder int4 = (IntegerDecoder) struDec.getValue("reserved2Pos");
        assertEquals(11,  int1.valueToRaw());
        assertEquals(0, int2.valueToRaw());
        assertEquals(0, int3.valueToRaw());
        assertEquals(165, int4.valueToRaw());

        byte[] var2Payload = {(byte) 0xED,  (byte) 0xC7, 0x00, 0x43, (byte) 0xD1, 0x53, 0x23, 0x41};
        String var2PayloadMessage = "rpm: 128.78096\n" +
                "mps: 10.207963\n";
        assertEquals(var2PayloadMessage, test.decode(0x402, var2Payload));
        StructDecoder structDec = (StructDecoder) test.getDecoder(0x402).get();
        FloatDecoder float1 = (FloatDecoder) structDec.getValue("mps");
        FloatDecoder float2 = (FloatDecoder) structDec.getValue("rpm");
        assertEquals(10.2, (Float) float1.rawValue, 0.01);
        assertEquals(128.781, (Float) float2.rawValue, 0.001);
    }
}
