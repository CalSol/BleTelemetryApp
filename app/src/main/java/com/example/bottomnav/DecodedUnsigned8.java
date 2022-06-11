package com.example.bottomnav;

import static com.example.bottomnav.Translation.payload;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DecodedUnsigned8 implements DecodedData<Integer>{
    int value;

    public DecodedUnsigned8() {
        decode();
    }

    @Override
    public void decode() {
        ByteBuffer bb = ByteBuffer.wrap(payload);
        int offset = 2;
        int newSize = payload.length - offset;
        byte[] packet = new byte[2];
        bb.get(packet, 0, offset);
        value = ByteBuffer.wrap(bb.array()).order(ByteOrder.LITTLE_ENDIAN).getInt();
        if ((payload.length / offset) > 1) {
            payload = bb.get(new byte[newSize], offset, newSize).array();
        }
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
