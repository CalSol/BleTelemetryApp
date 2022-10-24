package com.example.bottomnav;

import org.junit.Test;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

public class ParseTest {

    @Test
    public void checkNonExistenceSimple() throws Exception {
        Parse test = Parse.parseTextFile("constants.h");
        byte[] payload = {(byte) 0xff};

        assertEquals(false, test.decode("Steering", payload).isPresent());
        assertEquals(false, test.decode("Petals", payload).isPresent());
    }

    @Test
    public void decodeConsts() throws Exception {
        Parse test = Parse.parseTextFile("constants.h");

        byte[] eight = {(byte) 0xff};
        DecodedData decodedEightUnsigned = test.decode(0x402, eight).get();
        assertEquals(true, decodedEightUnsigned instanceof DecodedPrimitive);
        assertEquals("BRUH", ((DecodedPrimitive) decodedEightUnsigned).getName());
        assertEquals("255", ((DecodedPrimitive) decodedEightUnsigned).getValue());
        assertEquals("BRUH: 255", decodedEightUnsigned.dataToString());

        DecodedData decodedEightSigned = test.decode(0x403, eight).get();
        assertEquals(true, decodedEightSigned instanceof DecodedPrimitive);
        assertEquals("YOUNG", ((DecodedPrimitive) decodedEightSigned).getName());
        assertEquals("-1", ((DecodedPrimitive) decodedEightSigned).getValue());
        assertEquals("YOUNG: -1", decodedEightSigned.dataToString());

        byte[] sixteen = {(byte) 0xff, (byte) 0xff};
        DecodedData decodedSixteenUnsigned = test.decode(0x404, sixteen).get();
        assertEquals(true, decodedSixteenUnsigned instanceof DecodedPrimitive);
        assertEquals("FLAME", ((DecodedPrimitive) decodedSixteenUnsigned).getName());
        assertEquals("65535", ((DecodedPrimitive) decodedSixteenUnsigned).getValue());
        assertEquals("FLAME: 65535", decodedSixteenUnsigned.dataToString());

        DecodedData decodedSixteenSigned = test.decode(0x405, sixteen).get();
        assertEquals(true, decodedSixteenSigned instanceof DecodedPrimitive);
        assertEquals("HEIN", ((DecodedPrimitive) decodedSixteenSigned).getName());
        assertEquals("-1", ((DecodedPrimitive) decodedSixteenSigned).getValue());
        assertEquals("HEIN: -1", decodedSixteenSigned.dataToString());

        byte[] thirtytwo = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
        DecodedData decodedThirtyTwoUnsigned = test.decode(0x406, thirtytwo).get();
        assertEquals(true, decodedThirtyTwoUnsigned instanceof DecodedPrimitive);
        assertEquals("SICKO", ((DecodedPrimitive) decodedThirtyTwoUnsigned).getName());
        assertEquals("4294967295", ((DecodedPrimitive) decodedThirtyTwoUnsigned).getValue());
        assertEquals("SICKO: 4294967295", decodedThirtyTwoUnsigned.dataToString());

        DecodedData decodedThirtyTwoSigned = test.decode(0x407, thirtytwo).get();
        assertEquals(true, decodedThirtyTwoSigned instanceof DecodedPrimitive);
        assertEquals("MODE", ((DecodedPrimitive) decodedThirtyTwoSigned).getName());
        assertEquals("-1", ((DecodedPrimitive) decodedThirtyTwoSigned).getValue());
        assertEquals("MODE: -1", decodedThirtyTwoSigned.dataToString());


        byte[] regularInt = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
        DecodedData decodedInteger = test.decode(0x502, regularInt).get();
        assertEquals(true, decodedInteger instanceof DecodedPrimitive);
        assertEquals("YEET", ((DecodedPrimitive) decodedInteger).getName());
        assertEquals("-1", ((DecodedPrimitive) decodedInteger).getValue());
        assertEquals("YEET: -1", decodedInteger.dataToString());
    }

    @Test
    public void decodeRandomConst() throws Exception {
        Parse test = Parse.parseTextFile("constants.h");
        Random choosing = new Random();
        byte[] sizeOne = new byte[1];
        byte[] sizeTwo = new byte[2];
        byte[] sizeFour = new byte[4];

        choosing.nextBytes(sizeOne);
        assertEquals("BRUH: " + (new BigInteger(sizeOne).intValue() & 0xff),
                test.decode(0x402, sizeOne).get().dataToString());
        assertEquals("YOUNG: " + new BigInteger(sizeOne).intValue(),
                test.decode(0x403, sizeOne).get().dataToString());

        choosing.nextBytes(sizeTwo);
        assertEquals("FLAME: " + (new BigInteger(sizeTwo).intValue() & 0xffff),
                test.decode(0x404, sizeTwo).get().dataToString());
        assertEquals("HEIN: " + new BigInteger(sizeTwo).intValue(),
                test.decode(0x405, sizeTwo).get().dataToString());

        choosing.nextBytes(sizeFour);
        assertEquals("SICKO: " + (new BigInteger(sizeFour).longValue() & 0xffffffffL),
                test.decode(0x406, sizeFour).get().dataToString());
        assertEquals("MODE: " + new BigInteger(sizeFour).intValue(),
                test.decode(0x407, sizeFour).get().dataToString());
    }

    @Test
    public void decodeStructSimple() throws Exception {
        Parse test = Parse.parseTextFile("structs.h");
        byte[] payload = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                (byte) 0xff};
        DecodedData decodedStruct = test.decode(0x282, payload).get();
        assertEquals(true, decodedStruct instanceof DecodedStruct);
        assertEquals("brakePos1: 255, brakePos2: -1, reserved1Pos: 65535, reserved2Pos: -1, reserved3Pos: 4294967295, reserved4Pos: -1, accelPos: -1",
                decodedStruct.dataToString());
        ArrayList<DecodedData> decodedData = ((DecodedStruct) decodedStruct).getMembers();
        String[] decodedStrings = {"brakePos1: 255", "brakePos2: -1", "reserved1Pos: 65535", "reserved2Pos: -1",
        "reserved3Pos: 4294967295", "reserved4Pos: -1", "accelPos: -1"};
        for (int i = 0; i < decodedData.size(); i++) {
            assertEquals(decodedStrings[i], decodedData.get(i).dataToString());
        }
    }

    @Test
    public void incorrectlyFormattedPayloads() throws Exception {
        Parse test = Parse.parseTextFile("structs.h");
        byte[] payload = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
        assertEquals(false, test.decode(0x282, payload).isPresent());

        byte[] payload2 = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
        assertEquals(false, test.decode(0x282, payload2).isPresent());
    }

    @Test
    public void nestedStructSimple() throws Exception {
        Parse test = Parse.parseTextFile("structs.h");
        byte[] payload = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
        DecodedData structDecoded = test.decode(0x300, payload).get();
        assertEquals(true, structDecoded instanceof DecodedStruct);
        ArrayList<DecodedData> members = ((DecodedStruct) structDecoded).getMembers();
        assertEquals(true, members.get(0) instanceof DecodedPrimitive);
        assertEquals(true, members.get(1) instanceof DecodedStruct);
        assertEquals(true, members.get(2) instanceof DecodedStruct);
        assertEquals("positionDegree: -1, impulseResponse: -1, frequencyResponse: -1, pascal: -1", structDecoded.dataToString());
        assertEquals("impulseResponse: -1, frequencyResponse: -1", members.get(1).dataToString());
        assertEquals("pascal: -1", members.get(2).dataToString());
        assertEquals("positionDegree: -1", members.get(0).dataToString());
    }
}
