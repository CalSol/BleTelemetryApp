package com.example.bottomnav;

public class DoubleDecoder implements DataDecoder {
    public int packetSize = 2;
    public double value;

    @Override
    public String decode(Integer canId, byte[] payload) {
        return null;
    }

    @Override
    public int getPacketSize() {
        return 0;
    }
}
