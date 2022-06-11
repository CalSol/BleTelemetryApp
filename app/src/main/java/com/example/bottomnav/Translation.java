package com.example.bottomnav;

import java.util.ArrayList;
import java.util.HashMap;

public class Translation {
    public static byte[] payload;
    public static Parse parsedFile;
    // CAN ID to DecodedData
    public HashMap<Integer, DecodedData> payLoadMap = new HashMap<>();

    public Translation(Parse inputParsed) {
        parsedFile = inputParsed;
    }

    public DecodedData decode(Integer canID, byte[] inputPayload) {
        payload = inputPayload;
        PayLoadDataType type = parsedFile.getConstContents(canID).payLoadDataType;
        switch (type) {
            case Struct:
                ArrayList<StructContents> contents = parsedFile.getCanStruct(canID);
                DecodedData data = new DecodedStruct(contents);
                payLoadMap.put(canID, data);
                return data;
            default:
                data = DecodedData.decodePrimative(type);
                payLoadMap.put(canID, data);
                return data;
        }
    }

    public DecodedData getDecodedData(int canID) {
        return payLoadMap.get(canID);
    }

}
