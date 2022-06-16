package com.example.bottomnav;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

public class StructDecoder implements DataDecoder {
    public  HashMap<String, DataDecoder> decodedPrimatives = new HashMap<>();
    private StringBuilder message = new StringBuilder();
    private ArrayList<StructContents> contents;

    public StructDecoder(ArrayList<StructContents> con) {
        contents = con;
    }

    @Override
    public String decode(Integer canId, byte[] payload) {
        if (!decodedPrimatives.isEmpty()) {
            decodedPrimatives = new HashMap<>();
            message = new StringBuilder();
        }
        for (StructContents variable : contents) {
            PayLoadDataType type = DataDecoder.lookup(variable.type);
            DataDecoder primativeDecoder = DataDecoder.getPrimativeDecoder(type);
            primativeDecoder.decode(canId, payload);
            decodedPrimatives.put(variable.name, primativeDecoder);
            payload = adjustPayload(payload, primativeDecoder.getPacketSize());
        }
        return getDecodedValues();
    }

    public byte[] adjustPayload(byte[] payload, int packetSize) {
        int newSize = payload.length - packetSize;
        ByteBuffer bb = ByteBuffer.wrap(payload);
        if ((payload.length / packetSize) > 1) {
            byte[] newPayload = new byte[newSize];
            bb.get(newPayload, 0, packetSize);
            bb.get(newPayload, 0, newSize);
            return newPayload;
        }
        return payload;
    }

    public String getDecodedValues() {
        for (int i = 0; i < contents.size(); i++) {
            message.append("\n");
            String varName = contents.get(i).name;
            PayLoadDataType type = DataDecoder.lookup(contents.get(i).type);
            message.append("    " + varName + ", " +
                    DataDecoder.getPrimativeValue(type, decodedPrimatives.get(varName)));
        }
        return message.toString();
    }

    public DataDecoder getValue(String variableName) {
        return decodedPrimatives.get(variableName);
    }

    @Override
    public int getPacketSize() {
        return 0;
    }
}
