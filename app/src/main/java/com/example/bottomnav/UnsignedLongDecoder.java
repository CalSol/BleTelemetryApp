package com.example.bottomnav;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Optional;

public class UnsignedLongDecoder<T> extends IntegerDecoder {
    Long sign = (long) 0xffffffffL;

    public UnsignedLongDecoder(int size, String name) {
        super(size, name);
    }

    @Override
    public String decode(Integer canId, byte[] payload) {
        ByteBuffer bb = ByteBuffer.wrap(payload);
        byte[] packet = new byte[contents.packetSize];
        bb.get(packet, 0, contents.packetSize);
        rawValue = (T) new Long(new BigInteger(packet).longValue() & sign);
        value = "" + rawValue;
        return valueToString();
    public Optional<T> getRawValue(byte[] payload) {
        return Optional.of((T) new Long(new BigInteger(wrapPayload(payload)).longValue() & sign));
    }
}
