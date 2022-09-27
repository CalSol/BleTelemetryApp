package com.example.bottomnav;


import android.os.Build;

import androidx.annotation.RequiresApi;

import java.lang.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Optional;

public class StructDecoder extends DataDecoder {
    private ArrayList<Optional<DataDecoder>> decodedPrimatives;

    static Optional<DataDecoder> create(ArrayList<VariableContents> variables) {
        ArrayList<Optional<DataDecoder>> decodedPrimatives = new ArrayList<>();
        for (VariableContents variable : variables) {
            Optional<DataDecoder> primitiveDecoder = PrimitiveDecoder.create(variable);
            if (!primitiveDecoder.isPresent()) {
                return Optional.empty();
            }
            decodedPrimatives.add(primitiveDecoder);
        }
        return Optional.of(new StructDecoder(decodedPrimatives));
    }

    public StructDecoder(ArrayList<Optional<DataDecoder>> decoded) {
        decodedPrimatives = decoded;
    }

    @Override
    public Optional<ArrayList> decodeToRaw(byte[] payload) {
        ArrayList<Optional> rawOptionals = new ArrayList<>();
        for(Optional<DataDecoder> decoder : decodedPrimatives) {
            if (decoder.isPresent()) {
                rawOptionals.add(decoder.get().decodeToRaw(payload));
                payload = adjustPayload(payload, ((PrimitiveDecoder) decoder.get()).typeSize);
            }
        }
        return Optional.of(rawOptionals);
    }

    
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public Optional<String> decodeToString(byte[] payload) {
        ArrayList<String> decodedStrings = new ArrayList<>();
        for(Optional<DataDecoder> decoder : decodedPrimatives) {
            if (decoder.isPresent()) {
                Optional<String> decoded = decoder.get().decodeToString(payload);
                if (decoded.isPresent()) {
                    decodedStrings.add(decoded.get());
                } else {
                    break;
                }
                payload = adjustPayload(payload, ((PrimitiveDecoder) decoder.get()).typeSize);
            } else {
                break;
            }
        }
        return Optional.of(String.join(",", decodedStrings));
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
