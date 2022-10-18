package com.example.bottomnav;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Optional;

public class DecoderFloat<T> extends DecoderPrimitive {
    public DecoderFloat(String name) {
        super(4, name);
    }

    @Override
    public Optional<T> getRawValue(byte[] payload) {
        return Optional.of((T) new Float(ByteBuffer.wrap(wrapPayload(payload)).order(ByteOrder.LITTLE_ENDIAN).getFloat()));
    }
}
