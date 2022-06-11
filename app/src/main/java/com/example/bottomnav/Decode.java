package com.example.bottomnav;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Shortcuts:
 * ByteBuffer.wrap(payload).order(ByteOrder.LITTLE_ENDIAN).getFloat()
 * byteArray[0] & 0xff
 *
 * decode method:
 * map can id to decoded value
 * return Decode object
 */

public class Decode {
    Parse parsedFile;
    public HashMap<Integer, DecodedData> payLoadMap = new HashMap<>();

    public Decode(Parse inputParsedFile) {
        parsedFile = inputParsedFile;
    }

    public DecodedData decodePayload(int canID, byte[] payLoad) {
        PayLoadDataType payLoadDataType = PayLoadDataType.valueOf(
                parsedFile.getConstContents(canID).type);

        switch (payLoadDataType) {
            case Struct:
                ArrayList<StructContents> contents = parsedFile.getStructContents(canID);
                DecodedData data = new DecodedStruct(canID, payLoad, contents);

                /**
                 * For every variable, recursion, 
                 */

                return null;
            case Integer:
                //create integer decoded object
                return null;
            case Float:
                //create float decoded object
                return null;
            case uint8_t:
                // create uint8_t decoded object
                return null;
            case uint16_t:
                // create uint16_t decoded object
                return null;
            case Double:
                // create doeuble decoded object
                return null;
            default:
                return null;
        }
    }



    public DecodedData getData(int canID) {
        return payLoadMap.get(canID);
    }

    public DecodedData getData(String canIDName) {
        return payLoadMap.get(parsedFile.getConstContents(canIDName));
    }
}
