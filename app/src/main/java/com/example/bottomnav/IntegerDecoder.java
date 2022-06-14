package com.example.bottomnav;

import java.nio.ByteBuffer;

public class IntegerDecoder implements DataDecoder {
    public int packetSize = 1;
    public int value;

    @Override
    public String decode(Integer canId, byte[] payload) {
        ByteBuffer bb = ByteBuffer.wrap(payload);
        byte[] packet = new byte[packetSize];
        bb.get(packet, 0, packetSize);
        value = packet[0] & 0xff;
        return "" + value;
    }

    @Override
    public int getPacketSize() {
        return packetSize;
    }
}
