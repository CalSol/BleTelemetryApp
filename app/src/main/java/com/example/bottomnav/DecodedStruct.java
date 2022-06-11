package com.example.bottomnav;

import java.util.ArrayList;

/**
 * Shortcuts:
 * ByteBuffer.wrap(payload).order(ByteOrder.LITTLE_ENDIAN).getFloat()
 * byteArray[0] & 0xff
 */

public class DecodedStruct implements DecodedData {
    public ArrayList<DecodedData> decodedValues = new ArrayList<>();
    ArrayList<StructContents> contents;

    public DecodedStruct(ArrayList<StructContents> con) {
        contents = con;
        decode();
    }

    @Override
    public void decode() {
        int index = 0;
        for (StructContents variable : contents) {
            PayLoadDataType type = PayLoadDataType.valueOf(variable.type);
            DecodedData data = DecodedData.decodePrimative(type);
            decodedValues.add(index, data);
            index++;
        }
    }
}
