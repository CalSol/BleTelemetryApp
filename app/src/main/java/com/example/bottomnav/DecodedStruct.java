package com.example.bottomnav;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Shortcuts:
 * ByteBuffer.wrap(payload).order(ByteOrder.LITTLE_ENDIAN).getFloat()
 * byteArray[0] & 0xff
 */

public class DecodedStruct implements DecodedData {
    public HashMap<String, DecodedData> decodedValues = new HashMap<>();
    ArrayList<StructContents> contents;

    public DecodedStruct(ArrayList<StructContents> con) {
        contents = con;
        decode();
    }

    @Override
    public void decode() {
        for (StructContents variable : contents) {
            PayLoadDataType type = PayLoadDataType.valueOf(variable.type);
            DecodedData data = DecodedData.decodePrimative(type);
            decodedValues.put(variable.name, data);
        }
    }

    public DecodedData getValue(String name) {
        return decodedValues.get(name);
    }
}
