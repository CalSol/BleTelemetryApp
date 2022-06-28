package com.example.bottomnav;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class FloatDecoder<T> extends PrimitiveDecoder {

    public FloatDecoder(int byteSize, VariableContents con) {
        super(byteSize, con);
    }

    @Override
    public String decode(Integer canId, byte[] payload) {
        ByteBuffer bb = ByteBuffer.wrap(payload);
        byte[] packet = new byte[packetSize];
        bb.get(packet, 0, packetSize);
        rawValue = (T) new Float(ByteBuffer.wrap(packet).order(ByteOrder.LITTLE_ENDIAN).getFloat());
        value = "" + contents.name + ": " + rawValue;
        return value;
    }
}
