package com.example.bottomnav;

import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

public class ParseTest {

    @Test
    public void checkNonExistenceSimple() throws Exception {
        Parse test = Parse.parseTextFile("constants.h");

        Optional<DecoderData> first = test.getDecoder(0x101);
        assertEquals(false, first.isPresent());

        Optional<DecoderData> second = test.getDecoder("ShawtyLikeAMelodyInMyHead");
        assertEquals(false, second.isPresent());
    }

    @Test
    public void testing() throws Exception {
        Parse test = Parse.parseTextFile("constants.h");
        byte[] eight = {(byte) 0xff};

        Optional<DecoderData> decoder = test.getDecoder(0x402);
        System.out.println(decoder.get().isPrimitive());
    }

    @Test
    public void decodeConsts() throws Exception {
        Parse test = Parse.parseTextFile("constants.h");

        byte[] eight = {(byte) 0xff};

        assertEquals("BRUH: 255", test.getDecoder(0x402).get().decodeToString(eight).get());
        assertEquals("YOUNG: -1", test.getDecoder(0x403).get().decodeToString(eight).get());

        byte[] sixteen = {(byte) 0xff, (byte) 0xff};
        assertEquals("FLAME: 65535", test.getDecoder(0x404).get().decodeToString(sixteen).get());
        assertEquals("HEIN: -1", test.getDecoder(0x405).get().decodeToString(sixteen).get());

        byte[] thirtytwo = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
        assertEquals("SICKO: 4294967295", test.getDecoder(0x406).get().decodeToString(thirtytwo).get());
        assertEquals("MODE: -1", test.getDecoder(0x407).get().decodeToString(thirtytwo).get());

        byte[] regularInt = {(byte) 0xff};
        assertEquals("YEET: -1", test.getDecoder(0x502).get().decodeToString(regularInt).get());
    }

    @Test
    public void decodeRandomConst() throws Exception {
        Parse test = Parse.parseTextFile("constants.h");

        Random choosing = new Random();
        byte[] sizeOne = new byte[1];
        byte[] sizeTwo = new byte[2];
        byte[] sizeFour = new byte[4];

        choosing.nextBytes(sizeOne);
        assertEquals("BRUH: " + (new BigInteger(sizeOne).intValue() & 0xff), test.getDecoder(0x402).get().decodeToString(sizeOne).get());
        assertEquals("YOUNG: " + new BigInteger(sizeOne).intValue(), test.getDecoder(0x403).get().decodeToString(sizeOne).get());


        choosing.nextBytes(sizeTwo);
        assertEquals("FLAME: " + (new BigInteger(sizeTwo).intValue() & 0xffff), test.getDecoder(0x404).get().decodeToString(sizeTwo).get());
        assertEquals("HEIN: " + new BigInteger(sizeTwo).intValue(), test.getDecoder(0x405).get().decodeToString(sizeTwo).get());

        choosing.nextBytes(sizeFour);
        assertEquals("SICKO: " + (new BigInteger(sizeFour).longValue() & 0xffffffffL), test.getDecoder(0x406).get().decodeToString(sizeFour).get());
        assertEquals("MODE: " + new BigInteger(sizeFour).intValue(), test.getDecoder(0x407).get().decodeToString(sizeFour).get());
    }

    @Test
    public void isTrue() throws Exception {
        Parse test = Parse.parseTextFile("structs.h");
        Optional<DecoderData> structDecoder = test.getDecoder(0x282);
        assertEquals(true, structDecoder.get().isStructure());

        Parse test2 = Parse.parseTextFile("constants.h");
        Optional<DecoderData> prim1 = test2.getDecoder(0x402);
        assertEquals(true, prim1.get().isPrimitive());
    }



    @Test
    public void parseResults() throws Exception {
        Parse test = Parse.parseTextFile("constants.h");
        String results = "Variable0: 0, Variable1: 1, Variable2: 2";
        ArrayList<DecodedContents> contents = test.parseDecoded(results);
        for (int i = 0; i < contents.size(); i++) {
            assertEquals("Variable" + i , contents.get(i).getName());
            assertEquals("" + i, contents.get(i).getValue());
        }
    }

    @Test
    public void incorrectlyFormattedPayloads() throws Exception {
        Parse test = Parse.parseTextFile("structs.h");
        byte[] payload = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
        Optional<String> structResults = test.getDecoder(0x282).get().decodeToString(payload);
        ArrayList<DecodedContents> contents = test.parseDecoded(structResults.get());

        assertEquals("accelPos", contents.get(0).getName());
        assertEquals("-1", contents.get(0).getValue());

        assertEquals("brakePos1", contents.get(1).getName());
        assertEquals("255", contents.get(1).getValue());

        assertEquals("brakePos2", contents.get(2).getName());
        assertEquals("-1", contents.get(2).getValue());

        assertEquals("reserved1Pos", contents.get(3).getName());
        assertEquals("65535", contents.get(3).getValue());

        assertEquals("reserved2Pos", contents.get(4).getName());
        assertEquals("-1", contents.get(4).getValue());

        assertEquals("reserved3Pos", contents.get(5).getName());
        assertEquals("4294967295", contents.get(5).getValue());

        assertEquals("incorrectlyFormattedPayload", contents.get(6).getName());
        assertEquals("null", contents.get(6).getValue());
    }
}
