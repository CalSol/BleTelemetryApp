package com.example.bottomnav;


import android.os.Build;

import androidx.annotation.RequiresApi;

import java.lang.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Optional;

public class StructDecoder extends DataDecoder {
    private ArrayList<DataDecoder> decodedPrimitives;

    static Optional<DataDecoder> create(ArrayList<VariableContents> variables) {
        ArrayList<DataDecoder> decodedPrimatives = new ArrayList<>();
        for (VariableContents variable : variables) {
            Optional<DataDecoder> primitiveDecoder = PrimitiveDecoder.create(variable);
            if (!primitiveDecoder.isPresent()) {
                return Optional.empty();
            }
            decodedPrimatives.add(primitiveDecoder.get());
        }
        return Optional.of(new StructDecoder(decodedPrimatives));
    }

    public StructDecoder(ArrayList<DataDecoder> decoded) {
        decodedPrimitives = decoded;
    }

    @Override
    public Optional<ArrayList<Optional>> decodeToRaw(byte[] payload) {
        ArrayList<Optional> rawOptionals = new ArrayList<>();
        for(DataDecoder decoder : decodedPrimitives) {
            rawOptionals.add(decoder.decodeToRaw(payload));
            payload = adjustPayload(payload, ((PrimitiveDecoder) decoder).typeSize);
        }
        return Optional.of(rawOptionals);
    }

    
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public Optional<String> decodeToString(byte[] payload) {
        ArrayList<String> decodedStrings = new ArrayList<>();
        for(int index = 0; index < decodedPrimitives.size(); index++) {
            PrimitiveDecoder decoder = (PrimitiveDecoder) decodedPrimitives.get(index);
            byte[] dedicatedPayload = getDedicatedPayload(payload, decoder.typeSize);
            if (index == decodedPrimitives.size() - 1) {
                if (dedicatedPayload.length < payload.length) {
                    decodedStrings.add("incorrectlyFormattedPayload: null");
                    break;
                }
            }
            Optional<String> decoded = decoder.decodeToString(dedicatedPayload);
            if (decoded.isPresent()) {
                decodedStrings.add(decoded.get());
            } else {
                break;
            }
            payload = adjustPayload(payload, decoder.typeSize);
        }
        return Optional.of(String.join(",", decodedStrings));
    }

    // Splice function that return an array of bytes under specified
    private byte[] getDedicatedPayload(byte[] payload, int typeSize){
        if (payload.length < typeSize) {
            return payload;
        }
        ByteBuffer bb = ByteBuffer.wrap(payload);
        byte[] newPayload = new byte[typeSize];
        bb.get(newPayload, 0, typeSize);
        return newPayload;
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
