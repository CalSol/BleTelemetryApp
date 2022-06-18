package com.example.bottomnav;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class StructDecoder implements DataDecoder {
    private HashMap<String, DataDecoder> decodedPrimatives = new HashMap<>();
    private ArrayList<StructContents> contents;

    public StructDecoder(ArrayList<StructContents> con) {
        contents = con;
    }

    @Override
    public String valueToString() {
        StringBuilder message = new StringBuilder();
        for (int i = 0; i < contents.size(); i++) {
            message.append("\n");
            String varName = contents.get(i).name;
            message.append("    " + varName + ", " +
                     decodedPrimatives.get(varName).valueToString());
        }
        return message.toString();
    }

    @Override
    public Object valueToRaw() {
        return null;
    }

    @Override
    public String decode(Integer canId, byte[] payload) {
        for (StructContents variable : contents) {
            Optional<DataDecoder> decoder = DataDecoder.getPrimativeDecoder(variable.payloadDataType);
            if (decoder.isPresent()) {
                decoder.get().decode(canId, payload);
                decodedPrimatives.put(variable.name, decoder.get());
                payload = adjustPayload(payload, ((PrimativeDecoder) decoder.get()).getPacketSize());
            }
        }
        return valueToString();
    }


    // Splicing function for each variable
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

    public PrimativeDecoder getValue(String variableName) {
        return (PrimativeDecoder) decodedPrimatives.get(variableName);
    }
}
