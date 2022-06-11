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
        int packetSize = 1;
        int newSize = payload.length - packetSize;
        byte[] packet = new byte[packetSize];
        bb.get(packet, 0, packetSize);
        value = packet[0] & 0xff;
        if ((payload.length / packetSize) > 1) {
            byte[] newPayload = new byte[newSize];
            bb.get(newPayload, 0, newSize).array();
            payload = newPayload;
        }
    }
}
