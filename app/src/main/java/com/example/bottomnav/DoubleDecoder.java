package com.example.bottomnav;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DoubleDecoder<T> extends PrimitiveDecoder{
    public DoubleDecoder(int byteSize, VariableContents contents) {
        super(byteSize, contents);
    }

    @Override
    public String decode(Integer canId, byte[] payload) {
        ByteBuffer bb = ByteBuffer.wrap(payload);
        byte[] packet = new byte[packetSize];
        bb.get(packet, 0, packetSize);
        rawValue = (T) new Double(ByteBuffer.wrap(payload).order(ByteOrder.LITTLE_ENDIAN).getDouble());
        value = "" + contents.name + ": " + rawValue;
        return value;
    }
}
