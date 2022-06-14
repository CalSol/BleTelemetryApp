package com.example.bottomnav;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class FloatDecoder implements DataDecoder {
    int packetSize = 4;
    float value;
    public PayLoadDataType payLoadDataType = PayLoadDataType.Float;

    @Override
    public String decode(Integer canId, byte[] payload) {
        ByteBuffer bb = ByteBuffer.wrap(payload);
        byte[] packet = new byte[4];
        bb.get(packet, 0, packetSize);
        value = ByteBuffer.wrap(packet).order(ByteOrder.LITTLE_ENDIAN).getFloat();
        return "float: " + value;
    }

    @Override
    public int getPacketSize() {
        return packetSize;
    }
}
