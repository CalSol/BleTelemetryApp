package com.example.bottomnav;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Optional;

public class IntegerDecoder<T> extends PrimitiveDecoder {

    public IntegerDecoder(int size, String name) {
        super(size, name);
    }

    @Override
    public String decode(Integer canId, byte[] payload) {
        ByteBuffer bb = ByteBuffer.wrap(payload);
        byte[] packet = new byte[contents.packetSize];
        bb.get(packet, 0, contents.packetSize);
        rawValue = (T) new Integer(new BigInteger(packet).intValue());
        value = "" + rawValue;
        return valueToString();
    public Optional<T> getRawValue(byte[] payload) {
        return Optional.of((T) new Integer(new BigInteger(wrapPayload(payload)).intValue()));
    }
}
