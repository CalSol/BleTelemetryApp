package com.example.bottomnav;

import static com.example.bottomnav.Translation.payload;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DecodedFloat implements DecodedData {
    float value;

    public DecodedFloat() {
        decode();
    }

    @Override
    public void decode() {
        ByteBuffer bb = ByteBuffer.wrap(payload);
        int offset = 4;
        int newSize = payload.length - offset;
        byte[] packet = new byte[4];
        bb.get(packet, 0, offset);
        value = ByteBuffer.wrap(packet).order(ByteOrder.LITTLE_ENDIAN).getFloat();
        if ((payload.length / offset) > 1) {
            byte[] newPayload = new byte[newSize];
            bb.get(newPayload, offset, newSize).array();
            payload = newPayload;
        }
    }
}
