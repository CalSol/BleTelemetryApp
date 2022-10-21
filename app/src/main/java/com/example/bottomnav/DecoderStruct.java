package com.example.bottomnav;


import android.os.Build;

import androidx.annotation.RequiresApi;

import java.lang.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Optional;

public class DecoderStruct extends DecoderData {
    private ArrayList<DecoderData> decodedPrimitives;

    static Optional<DecoderData> create(ArrayList<VariableContents> variables) {
        ArrayList<DecoderData> decodedPrimatives = new ArrayList<>();
        for (VariableContents variable : variables) {
            Optional<DecoderData> primitiveDecoder = DecoderPrimitive.create(variable);
            if (!primitiveDecoder.isPresent()) {
                return Optional.empty();
            }
            decodedPrimatives.add(primitiveDecoder.get());
        }
        return Optional.of(new DecoderStruct(decodedPrimatives));
    }

    public DecoderStruct(ArrayList<DecoderData> decoded) {
        decodedPrimitives = decoded;
    }

    @Override
    public Optional<ArrayList<Optional>> decodeToRaw(byte[] payload) {
        ArrayList<Optional> rawOptionals = new ArrayList<>();
        for(DecoderData decoder : decodedPrimitives) {
            rawOptionals.add(decoder.decodeToRaw(payload));
            payload = adjustPayload(payload, ((DecoderPrimitive) decoder).typeSize);
        }
        return Optional.of(rawOptionals);
    }

    
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public Optional<String> decodeToString(byte[] payload) {
        ArrayList<String> decodedContents = new ArrayList<>();

        for(DecoderData decoder : decodedPrimitives) {
            DecoderPrimitive decoderPrimitive = (DecoderPrimitive) decoder;
            byte[] dedicatedPayload = getDedicatedPayload(payload, decoderPrimitive.typeSize);
            Optional<String> decoded = decoderPrimitive.decodeToString(dedicatedPayload);
            if (decoded.isPresent()) {
                decodedContents.add(decoded.get());
            } else {
                break;
            }
            payload = adjustPayload(payload, decoderPrimitive.typeSize);
        }

        if (payload.length != 0) {
            decodedContents.set(decodedContents.size() - 1, "incorrectlyFormattedPayload: null");
        }

        return Optional.of(String.join(",", decodedContents));
    }

    @Override
    boolean isPrimitive() {
        return false;
    }

    @Override
    boolean isStructure() {
        return true;
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
    public byte[] adjustPayload(byte[] payload, int typeSize) {
        int newSize = payload.length - typeSize;
        ByteBuffer bb = ByteBuffer.wrap(payload);
        if ((payload.length / typeSize) > 1) {
            byte[] newPayload = new byte[newSize];
            bb.get(newPayload, 0, typeSize);
            bb.get(newPayload, 0, newSize);
            return newPayload;
        } else if (newSize == 0) {
            return new byte[0];
        }
        return payload;
    }
}
