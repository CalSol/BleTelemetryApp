package com.example.bottomnav;

import java.lang.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Optional;

public class DecoderStruct extends DecoderData {
    private ArrayList<DecoderData> decoders;
    private int typeSize;

    static Optional<DecoderData> create(int numMembers, int byteSize, ArrayList<DecoderData> decoders) {
        if (numMembers != decoders.size()) {
            return Optional.empty();
        }
        return Optional.of(new DecoderStruct(byteSize, decoders));
    }

    public DecoderStruct(int givenTypeSize, ArrayList<DecoderData> decoded) {
        typeSize = givenTypeSize;
        decoders = decoded;
    }

    @Override
    public int getTypeSize() {
        return typeSize;
    }

    @Override
    public Optional<ArrayList<DecodedData>> decodeToRaw(byte[] payload) {
        ArrayList<DecodedData> decodedArray = new ArrayList<>();
        for(DecoderData decoder : decoders) {
            byte[] dedicatedPayload = getDedicatedPayload(payload, decoder.getTypeSize());
            Optional<DecodedData> decodedData = decoder.decodeToData(dedicatedPayload);
            if (decodedData.isPresent()) {
                decodedArray.add(decodedData.get());
            } else {
                break;
            }
            payload = adjustPayload(payload, decoder.getTypeSize());
        }
        if (payload.length != 0) {
            return Optional.empty();
        }
        return Optional.of(decodedArray);
    }

    @Override
    public Optional<DecodedData> decodeToData(byte[] payload) {
        Optional<ArrayList<DecodedData>> decodedArray = decodeToRaw(payload);
        if (decodedArray.isPresent()) {
            return Optional.of(new DecodedStruct(decodedArray.get()));
        }
        return Optional.empty();
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
        if (payload.length >= typeSize) {
            byte[] newPayload = new byte[newSize];
            byte[] trash = new byte[typeSize];
            bb.get(trash, 0, typeSize);  // Offset is not index of payload
            bb.get(newPayload, 0, newSize);
            return newPayload;
        } else if (newSize == 0) {
            return new byte[0];
        }
        return payload;
    }
}
