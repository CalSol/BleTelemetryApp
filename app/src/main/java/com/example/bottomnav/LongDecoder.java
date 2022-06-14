package com.example.bottomnav;

public class LongDecoder implements DataDecoder {
    public int packetSize = 4;
    public long value;

    @Override
    public String decode(Integer canId, byte[] payload) {
        return null;
    }

    @Override
    public int getPacketSize() {
        return 0;
    }
}
