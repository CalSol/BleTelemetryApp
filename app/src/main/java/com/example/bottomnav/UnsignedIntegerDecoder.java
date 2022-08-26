package com.example.bottomnav;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Optional;

public class UnsignedIntegerDecoder<T> extends IntegerDecoder{
    int sign;

    public UnsignedIntegerDecoder(int size, int givenSign, String name) {
        super(size, name);
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
    public Optional<T> getRawValue(byte[] payload) {
        return Optional.of((T) new Integer(new BigInteger(wrapPayload(payload)).intValue() & sign));
    }
}