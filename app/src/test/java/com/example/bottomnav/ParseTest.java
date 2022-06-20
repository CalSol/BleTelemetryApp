package com.example.bottomnav;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

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

        assertEquals("const", test.getConstContents("CAN_ID").typeQualifer);
        assertEquals("const", test.getConstContents("CAN_ID2").typeQualifer);
        assertEquals("const", test.getConstContents("CAN_ID3").typeQualifer);

        assertEquals("uint16_t", test.getConstContents("CAN_ID").payLoadDataType);
        assertEquals("uint16_t", test.getConstContents("CAN_ID2").payLoadDataType);
        assertEquals("uint16_t", test.getConstContents("CAN_ID3").payLoadDataType);

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
        assertEquals("const", test.getConstContents("CAN_ID1").typeQualifer);
        assertEquals("const", test.getConstContents("CAP_ID2").typeQualifer);
        assertEquals("const", test.getConstContents("CAN_ID3").typeQualifer);
        assertEquals("uint16_t", test.getConstContents("CAN_ID1").payLoadDataType);
        assertEquals("uint32_t", test.getConstContents("CAP_ID2").payLoadDataType);
        assertEquals("uint32_t", test.getConstContents("CAN_ID3").payLoadDataType);
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
                "const uint16_t CAN_ID3 = 0x64;";
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
        assertEquals("uint16_t", test.getConstContents("CAN_ID").payLoadDataType);
        assertEquals("0x16", test.getConstContents("CAN_ID").value);

        assertEquals("const", test.getConstContents("CAN_ID3").typeQualifer);
        assertEquals("uint16_t", test.getConstContents("CAN_ID3").payLoadDataType);
        assertEquals("0x64", test.getConstContents("CAN_ID3").value);
    }

    @Test
    public void parseData() throws Exception {
        Parse test = Parse.parseTextFile("parseData.h");
        ArrayList<VariableContents> struct = test.getStructContents("ChargerControlStruct");

        assertEquals("const", test.getConstContents("CAN_HEART_BMS").typeQualifer);
        assertEquals("const", test.getConstContents("CAN_BMS_FAN_SETPOINT").typeQualifer);
        assertEquals("const", test.getConstContents("CAN_CHARGER_STATUS").typeQualifer);

        assertEquals("uint16_t", test.getConstContents("CAN_HEART_BMS").payLoadDataType);
        assertEquals("uint16_t", test.getConstContents("CAN_BMS_FAN_SETPOINT").payLoadDataType);
        assertEquals("uint16_t", test.getConstContents("CAN_CHARGER_STATUS").payLoadDataType);

        assertEquals("0x040", test.getConstContents("CAN_HEART_BMS").value);
        assertEquals("0x560", test.getConstContents("CAN_BMS_FAN_SETPOINT").value);
        assertEquals("0x18FF50E5", test.getConstContents("CAN_CHARGER_STATUS").value);

        assertEquals("voltage_be", struct.get(0).name);
        assertEquals("current_be", struct.get(1).name);
        assertEquals("control", struct.get(2).name);
        assertEquals("reserved1", struct.get(3).name);
        assertEquals("reserved2", struct.get(4).name);
        assertEquals("reserved3", struct.get(5).name);
        assertEquals("uint16_t", struct.get(0).payloadDataType);
        assertEquals("uint16_t", struct.get(1).payloadDataType);
        assertEquals("uint8_t", struct.get(2).payloadDataType);
        assertEquals("uint8_t", struct.get(3).payloadDataType);
        assertEquals("uint8_t", struct.get(4).payloadDataType);
        assertEquals("uint8_t", struct.get(5).payloadDataType);

        assertEquals("voltage_be", struct.get(0).name);
        assertEquals("current_be", struct.get(1).name);
        assertEquals("control", struct.get(2).name);
        assertEquals("reserved1", struct.get(3).name);
        assertEquals("reserved2", struct.get(4).name);
        assertEquals("reserved3", struct.get(5).name);
        assertEquals("uint16_t", struct.get(0).payloadDataType);
        assertEquals("uint16_t", struct.get(1).payloadDataType);
        assertEquals("uint8_t", struct.get(2).payloadDataType);
        assertEquals("uint8_t", struct.get(3).payloadDataType);
        assertEquals("uint8_t", struct.get(4).payloadDataType);
        assertEquals("uint8_t", struct.get(5).payloadDataType);
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
        Parse test = Parse.parseTextFile("decode.h");

        byte[] packedFloatPayload = {0x71, (byte) 0xFD, 0x47, 0x41};
        String packedFloatMessage = "single PACKED_FLOAT, float: 12.499375";
        System.out.println(test.decode(0x310, packedFloatPayload));
        assertEquals(packedFloatMessage, test.decode(0x310, packedFloatPayload));
        PrimativeDecoder floatDec = (PrimativeDecoder) test.getDecoder(0x310);
        assertEquals(12.499375343322754,(float) floatDec.valueToRaw(), 0.000000000000001);

        byte[] var4Payload = {0x0B, 0x00, 0x00, (byte) 0xA5};
        String var4PaylodMessage = "struct CanPedalPosStruct, \n" +
                "    accelPos, uint8_t: 11\n" +
                "    brakePos, uint8_t: 0\n" +
                "    reserved1Pos, uint8_t: 0\n" +
                "    reserved2Pos, uint8_t: 165";
        System.out.println(test.decode(0x282, var4Payload));
        assertEquals(var4PaylodMessage, test.decode(0x282, var4Payload));
        StructDecoder struDec = (StructDecoder) test.getDecoder(0x282);
        PrimativeDecoder int1 = struDec.getValue("accelPos");
        PrimativeDecoder int2 = struDec.getValue("brakePos");
        PrimativeDecoder int3 = struDec.getValue("reserved1Pos");
        PrimativeDecoder int4 = struDec.getValue("reserved2Pos");
        assertEquals(11,  int1.valueToRaw());
        assertEquals(0, int2.valueToRaw());
        assertEquals(0, int3.valueToRaw());
        assertEquals(165, int4.valueToRaw());

        byte[] var2Payload = {(byte) 0xED,  (byte) 0xC7, 0x00, 0x43, (byte) 0xD1, 0x53, 0x23, 0x41};
        String var2PayloadMessage = "struct CanTritiumVelocityStruct, \n" +
                "    rpm, float: 128.78096\n" +
                "    mps, float: 10.207963";
        System.out.println(test.decode(0x402, var2Payload));
        assertEquals(var2PayloadMessage, test.decode(0x402, var2Payload));
        StructDecoder structDec = (StructDecoder) test.getDecoder(0x402);
        PrimativeDecoder floatDec1 = structDec.getValue("mps");
        PrimativeDecoder floatDec2 = structDec.getValue("rpm");
        assertEquals(10.2, (Float) floatDec1.valueToRaw(), 0.01);
        assertEquals(128.781, (Float) floatDec2.valueToRaw(), 0.001);
    }
}
