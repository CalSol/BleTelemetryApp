package com.example.bottomnav;

import org.junit.Test;
import static org.junit.Assert.*;

import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.Optional;

public class ParseTest {

    @Test
    public void decodeConsts() throws Exception {
        Parse test = Parse.parseTextFile("constants.h");

        byte[] one  = {(byte) 0xff};
        assertEquals("YEET: -1", test.decodeToString(0x502, one));

        byte[] eight = {(byte) 0xff};
        assertEquals("BRUH: 255", test.decodeToString(0x402, eight));
        assertEquals("YOUNG: -1", test.decodeToString(0x403, eight));

        byte[] sixteen = {(byte) 0xff, (byte) 0xff};
        assertEquals("FLAME: 65535", test.decodeToString(0x404, sixteen));
        assertEquals("HEIN: -1", test.decodeToString(0x405, sixteen));

        byte[] thirtytwo = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
        assertEquals("SICKO: 4294967295", test.decodeToString(0x406, thirtytwo));
        assertEquals("MODE: -1", test.decodeToString(0x407, thirtytwo));
    }

    @Test
    public void parseConstResults() throws Exception {
        Parse test = Parse.parseTextFile("constants.h");

        byte[] one  = {(byte) 0xff};
        Optional<String> integerResults = test.decodeToString(0x502, one);
        DataDecoder.Decoded parsed = DataDecoder.parseStringResult(integerResults.get()).get(0);

        assertEquals("YEET", parsed.getName());
        assertEquals("-1", parsed.getValue());
    }

    @Test
    public void decodeStructs() throws Exception {
        Parse test = Parse.parseTextFile("structs.h");

        byte[] payload = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
        Optional<String> structResults = test.decodeToString(0x282, payload);
        ArrayList<DataDecoder.Decoded> parsedResults = DataDecoder.parseStringResult(structResults.get());

        assertEquals("accelPos", parsedResults.get(0).getName());
        assertEquals("-1", parsedResults.get(0).getValue());

        assertEquals("brakePos1", parsedResults.get(1).getName());
        assertEquals("255", parsedResults.get(1).getValue());
        assertEquals("brakePos2", parsedResults.get(2).getName());
        assertEquals("-1", parsedResults.get(2).getValue());

        assertEquals("reserved1Pos", parsedResults.get(3).getName());
        assertEquals("65535", parsedResults.get(3).getValue());
        assertEquals("reserved2Pos", parsedResults.get(4).getName());
        assertEquals("-1", parsedResults.get(4).getValue());

        assertEquals("reserved3Pos", parsedResults.get(5).getName());
        assertEquals("4294967295", parsedResults.get(5).getValue());
        assertEquals("reserved4Pos", parsedResults.get(6).getName());
        assertEquals("-1", parsedResults.get(6).getValue());
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

    /**
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
    } */
}
