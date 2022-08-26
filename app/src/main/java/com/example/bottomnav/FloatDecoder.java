package com.example.bottomnav;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class FloatDecoder<T> extends PrimitiveDecoder {
    public FloatDecoder(int size, String name) {
        super(size, name);
    }

    @Override
    public String decode(Integer canId, byte[] payload) {
        ByteBuffer bb = ByteBuffer.wrap(payload);
        byte[] packet = new byte[contents.packetSize];
        bb.get(packet, 0, contents.packetSize);
        rawValue = (T) new Float(ByteBuffer.wrap(packet).order(ByteOrder.LITTLE_ENDIAN).getFloat());
        value = "" + rawValue;
        return valueToString();
    public Optional<T> getRawValue(byte[] payload) {
        return Optional.of((T) new Float(ByteBuffer.wrap(wrapPayload(payload)).order(ByteOrder.LITTLE_ENDIAN).getFloat()));
    }
}
