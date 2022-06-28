package com.example.bottomnav;

import java.math.BigInteger;
import java.nio.ByteBuffer;

public class UnsignedIntegerDecoder<T> extends IntegerDecoder{
    int sign;
    public UnsignedIntegerDecoder(int byteSize, VariableContents contents, int givenSign) {
        super(byteSize, contents);
        sign = givenSign;
    }

    @Override
    public String decode(Integer canId, byte[] payload) {
        ByteBuffer bb = ByteBuffer.wrap(payload);
        byte[] packet = new byte[packetSize];
        bb.get(packet, 0, packetSize);
        rawValue = (T) new Integer(new BigInteger(packet).intValue() & sign);
        value = "" + contents.name + ": " + rawValue;
        return value;
    }


}
