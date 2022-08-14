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

        assertEquals("const", ((IntegerDecoder) test.getDecoder("CAN_ID").get()).contents.typeQualifer);
        assertEquals("const", ((IntegerDecoder) test.getDecoder("CAN_ID2").get()).contents.typeQualifer);
        assertEquals("const", ((IntegerDecoder) test.getDecoder("CAN_ID3").get()).contents.typeQualifer);

        assertEquals("uint16_t", ((IntegerDecoder) test.getDecoder("CAN_ID").get()).contents.payloadDataType);
        assertEquals("uint16_t", ((IntegerDecoder) test.getDecoder("CAN_ID2").get()).contents.payloadDataType);
        assertEquals("uint16_t", ((IntegerDecoder) test.getDecoder("CAN_ID3").get()).contents.payloadDataType);

        assertEquals("0x16", ((IntegerDecoder) test.getDecoder("CAN_ID").get()).contents.value);
        assertEquals("0x32", ((IntegerDecoder) test.getDecoder("CAN_ID2").get()).contents.value);
        assertEquals("0x64", ((IntegerDecoder) test.getDecoder("CAN_ID3").get()).contents.value);
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
                ((StructDecoder) test.getDecoder("CAN_CHARGER_STATUS").get()).variables);
        assertEquals(test.getStructContents("ChargerControlStruct"),
                ((StructDecoder) test.getDecoder("CAN_CHARGER_CONTROL").get()).variables);
        assertEquals(test.getStructContents("CanStrainGaugeStruct"),
                ((StructDecoder) test.getDecoder("CAN_STRAIN_DATA").get()).variables);
        assertEquals(test.getStructContents("CanPedalPosStruct"),
                ((StructDecoder) test.getDecoder("CAN_PEDAL_POS").get()).variables);
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
    public void doubleParse() throws Exception {
        Parse test = Parse.parseTextFile("decode.h");
        // Start
        DataDecoder solution2 = test.getDecoder(0x310).get();

        byte[] packedFloatPayload = {0x71, (byte) 0xFD, 0x47, 0x41};
        String packedFloatMessage = "PACKED_FLOAT: 12.499375";
        assertEquals(packedFloatMessage, test.decode(0x310, packedFloatPayload));

        byte[] packet2 = {0x21, (byte) 0xFF, 0x37, 0x21};
        test.decode(0x310, packet2);
        System.out.println(solution2.getVarNameAt(0));
        System.out.println(solution2.getValueStringAt(0));

    }
    @Test
    public void decodingVariety() throws Exception {
        Parse test = Parse.parseTextFile("decode.h");

        // Start
        byte[] packedFloatPayload = {0x71, (byte) 0xFD, 0x47, 0x41};
        String packedFloatMessage = "PACKED_FLOAT: 12.499375";
        assertEquals(packedFloatMessage, test.decode(0x310, packedFloatPayload));

        // Method 1
        FloatDecoder solution1 = (FloatDecoder) test.getDecoder(0x310).get();
        assertEquals(12.499375343322754,(float) solution1.valueToRaw(), 0.000000000000001);
        assertEquals("PACKED_FLOAT", solution1.getVarName());
        assertEquals("12.499375", solution1.getValueString());

        // Method 2
        DataDecoder solution2 = test.getDecoder(0x310).get();
        assertEquals("PACKED_FLOAT", solution2.getVarNameAt(0));
        assertEquals("12.499375", solution2.getValueStringAt(0));
        assertEquals(1, solution2.getSize());
        // End

        // Start
        byte[] var4Payload = {0x0B, 0x00, 0x00, (byte) 0xA5};
        String var4PaylodMessage = "accelPos: 11\n"
                + "brakePos: 0\n"
                + "reserved1Pos: 0\n"
                + "reserved2Pos: 165\n";
        assertEquals(var4PaylodMessage, test.decode(0x282, var4Payload));

        // Method 1
        StructDecoder structSol1 = (StructDecoder) test.getDecoder(0x282).get();
        IntegerDecoder int1 = (IntegerDecoder) structSol1.getPrimitiveDecoder(0);
        IntegerDecoder int2 = (IntegerDecoder) structSol1.getPrimitiveDecoder(1);
        IntegerDecoder int3 = (IntegerDecoder) structSol1.getPrimitiveDecoder(2);
        IntegerDecoder int4 = (IntegerDecoder) structSol1.getPrimitiveDecoder(3);
        assertEquals(11,  int1.valueToRaw());
        assertEquals(0, int2.valueToRaw());
        assertEquals(0, int3.valueToRaw());
        assertEquals(165, int4.valueToRaw());

        // Method 2
        DataDecoder structSol2 =  test.getDecoder(0x282).get();
        assertEquals("accelPos",  structSol2.getVarNameAt(0));
        assertEquals("brakePos",  structSol2.getVarNameAt(1));
        assertEquals("reserved1Pos",  structSol2.getVarNameAt(2));
        assertEquals("reserved2Pos",  structSol2.getVarNameAt(3));

        assertEquals("11",  structSol2.getValueStringAt(0));
        assertEquals("0",  structSol2.getValueStringAt(1));
        assertEquals("0",  structSol2.getValueStringAt(2));
        assertEquals("165",  structSol2.getValueStringAt(3));
        assertEquals(4, structSol2.getSize());
        // End

        // Start
        byte[] var2Payload = {(byte) 0xED,  (byte) 0xC7, 0x00, 0x43, (byte) 0xD1, 0x53, 0x23, 0x41};
        String var2PayloadMessage = "rpm: 128.78096\n"
                + "mps: 10.207963\n";
        assertEquals(var2PayloadMessage, test.decode(0x402, var2Payload));

        // Method 1
        StructDecoder structDecSol1 = (StructDecoder) test.getDecoder(0x402).get();
        FloatDecoder float1 = (FloatDecoder) structDecSol1.getPrimitiveDecoder(0);
        FloatDecoder float2 = (FloatDecoder) structDecSol1.getPrimitiveDecoder(1);
        assertEquals(128.781, (Float) float1.valueToRaw(), 0.001);
        assertEquals(10.2, (Float) float2.valueToRaw(), 0.01);

        // Method 2
        DataDecoder  structDecSol2 = test.getDecoder(0x402).get();
        assertEquals("rpm", structDecSol2.getVarNameAt(0));
        assertEquals("mps", structDecSol2.getVarNameAt(1));
        assertEquals("128.78096", structDecSol2.getValueStringAt(0));
        assertEquals("10.207963", structDecSol2.getValueStringAt(1));
        assertEquals(2, structDecSol2.getSize());
        // End
    }
}
