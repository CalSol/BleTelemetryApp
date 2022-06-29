package com.example.bottomnav;

import java.math.BigInteger;
import java.nio.ByteBuffer;

public class UnsignedIntegerDecoder<T> extends IntegerDecoder{
    int sign;

    public UnsignedIntegerDecoder(VariableContents contents, int givenSign) {
        super(contents);
        sign = givenSign;
    }

    @Override
    public String decode(Integer canId, byte[] payload) {
        ByteBuffer bb = ByteBuffer.wrap(payload);
        byte[] packet = new byte[contents.packetSize];
        bb.get(packet, 0, contents.packetSize);
        rawValue = (T) new Integer(new BigInteger(packet).intValue() & sign);
        value = "" + rawValue;
        return valueToString();
    }


}
