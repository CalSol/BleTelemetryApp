package com.example.bottomnav;

import java.math.BigInteger;
import java.nio.ByteBuffer;

public class UnsignedLongDecoder<T> extends IntegerDecoder {
    Long sign;

    public UnsignedLongDecoder(VariableContents contents) {
        super(contents);
        sign = (long) 0xffffffffL;
    }

    @Override
    public String decode(Integer canId, byte[] payload) {
        ByteBuffer bb = ByteBuffer.wrap(payload);
        byte[] packet = new byte[contents.packetSize];
        bb.get(packet, 0, contents.packetSize);
        rawValue = (T) new Long(new BigInteger(packet).longValue() & sign);
        value = "" + rawValue;
        return valueToString();
    }
}
