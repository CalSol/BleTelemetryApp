package com.example.bottomnav;

import static com.example.bottomnav.Translation.payload;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DecodedInteger implements DecodedData{
    int value;

    public DecodedInteger() {
        decode();
    }

    @Override
    public void decode() {
        ByteBuffer bb = ByteBuffer.wrap(payload);
        int offset = 2;
        int newSize = payload.length - offset;
        byte[] packet = new byte[4];
        bb.get(packet, 0, offset);
        value = ByteBuffer.wrap(bb.array()).order(ByteOrder.LITTLE_ENDIAN).getInt();
        payload = bb.get(new byte[newSize], offset, newSize).array();
    }
}