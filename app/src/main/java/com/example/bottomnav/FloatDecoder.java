package com.example.bottomnav;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Optional;

public class FloatDecoder<T> extends PrimitiveDecoder {
    public FloatDecoder(String name) {
        super(4, name);
    }

    @Override
    public Optional<T> getRawValue(byte[] payload) {
        return Optional.of((T) new Float(ByteBuffer.wrap(wrapPayload(payload)).order(ByteOrder.LITTLE_ENDIAN).getFloat()));
    }
}
