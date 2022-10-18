package com.example.bottomnav;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Optional;

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
    public void parseSimpleResults() throws Exception {
        Parse test = Parse.parseTextFile("structs.h");
        String result = "Gucci: -1, Flip: 0, Flops: 1";
        ArrayList<DecodedContents> parsedResults = test.parseDecodedString(result);

        assertEquals("Gucci", parsedResults.get(0).getName());
        assertEquals("Flip", parsedResults.get(1).getName());
        assertEquals("Flops", parsedResults.get(2).getName());

        assertEquals("-1", parsedResults.get(0).getValue());
        assertEquals("0", parsedResults.get(1).getValue());
        assertEquals("1", parsedResults.get(2).getValue());
    }

    @Test
    public void parseConstResults() throws Exception {
        Parse test = Parse.parseTextFile("constants.h");

        byte[] one  = {(byte) 0xff};
        Optional<String> integerResults = test.getDecoder(0x502).get().decodeToString(one);
        DecodedContents parsed = test.parseDecodedString(integerResults.get()).get(0);

        assertEquals("YEET", parsed.getName());
        assertEquals("-1", parsed.getValue());
    }

    @Test
    public void parseStructures() throws Exception {
        Parse test = Parse.parseTextFile("structs.h");

        byte[] payload = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
        Optional<String> structResults = test.getDecoder(0x282).get().decodeToString(payload);
        ArrayList<DecodedContents> parsedResults = test.parseDecodedString(structResults.get());

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
    public void incorrectlyFormattedPayloads() throws Exception {
        Parse test = Parse.parseTextFile("structs.h");
        byte[] payload = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
        Optional<String> structResults = test.getDecoder(0x282).get().decodeToString(payload);
        ArrayList<DecodedContents> parsedResults = test.parseDecodedString(structResults.get());

        assertEquals("incorrectlyFormattedPayload", parsedResults.get(6).getName());
        assertEquals("null", parsedResults.get(6).getValue());
    }
}
