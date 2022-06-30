package com.example.bottomnav;

import java.math.BigInteger;
import java.nio.ByteBuffer;

public class IntegerDecoder<T> extends PrimitiveDecoder {
    public IntegerDecoder(VariableContents contents) {
        super(contents);
    }

    @Override
    public String decode(Integer canId, byte[] payload) {
        ByteBuffer bb = ByteBuffer.wrap(payload);
        byte[] packet = new byte[contents.packetSize];
        bb.get(packet, 0, contents.packetSize);
        rawValue = (T) new Integer(new BigInteger(packet).intValue());
        value = "" + rawValue;
        return valueToString();
    }
}
