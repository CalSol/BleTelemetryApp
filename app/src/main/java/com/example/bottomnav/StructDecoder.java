package com.example.bottomnav;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class StructDecoder implements DataDecoder {
    private ArrayList<Optional<DataDecoder>> decodedPrimatives;


    public StructDecoder(ArrayList<Optional<DataDecoder>> decoded) {
        decodedPrimatives = decoded;
    }

    @Override
    public Optional<ArrayList> decodeToRaw(byte[] payload) {
        ArrayList<Optional> rawOptionals = new ArrayList<>();
        for(Optional<DataDecoder> decoder : decodedPrimatives) {
            if (decoder.isPresent()) {
                rawOptionals.add(decoder.get().decodeToRaw(payload));
                payload = adjustPayload(payload, ((PrimitiveDecoder) decoder.get()).packetSize);
            }
        }
        return Optional.of(rawOptionals);
    }

    @Override
    public String decode(Integer canId, byte[] payload) {
        for (VariableContents variable : variables) {
            Optional<DataDecoder> decoder = DataDecoder.createPrimitiveDecoder(variable);

    public Optional<String> decodeToString(byte[] payload) {
        String resultString = "";
        String tail = ", ";
        for(Optional<DataDecoder> decoder : decodedPrimatives) {
            if (decoder == decodedPrimatives.get(decodedPrimatives.size()-1)) {
                tail = "";
            }
            if (decoder.isPresent()) {
                Optional<String> decoded = decoder.get().decodeToString(payload);
                if (decoded.isPresent()) {
                    resultString += decoded.get() + tail;

                } else {
                    return Optional.empty();
                }
                payload = adjustPayload(payload, ((PrimitiveDecoder) decoder.get()).packetSize);
            } else {
                return Optional.empty();
            }
        }
        return valueToString();

        return Optional.of(resultString);
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
}
