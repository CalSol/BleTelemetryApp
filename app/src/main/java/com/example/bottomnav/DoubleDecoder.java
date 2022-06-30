package com.example.bottomnav;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DoubleDecoder<T> extends PrimitiveDecoder{

    public DoubleDecoder(VariableContents contents) {
        super(contents);
    }

    @Override
    public String decode(Integer canId, byte[] payload) {
        ByteBuffer bb = ByteBuffer.wrap(payload);
        byte[] packet = new byte[contents.packetSize];
        bb.get(packet, 0, contents.packetSize);
        rawValue = (T) new Double(ByteBuffer.wrap(payload).order(ByteOrder.LITTLE_ENDIAN).getDouble());
        value = "" + rawValue;
        return value;
    }
}
