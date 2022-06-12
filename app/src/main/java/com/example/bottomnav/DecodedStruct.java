package com.example.bottomnav;

import java.util.ArrayList;
import java.util.HashMap;

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
            PayLoadDataType type = DecodedData.lookup(variable.type);
            DecodedData data = DecodedData.decodePrimative(type);
            decodedValues.put(variable.name, data);
        }
    }

    public DecodedData getValue(String name) {
        return decodedValues.get(name);
    }
}
