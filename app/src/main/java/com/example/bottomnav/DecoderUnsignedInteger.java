package com.example.bottomnav;

import java.math.BigInteger;
import java.util.Optional;

public class DecoderUnsignedInteger<T> extends DecoderInteger {

    public DecoderUnsignedInteger(int size, String name) {
        super(size, name);
    }

    @Override
    public Optional<T> getRawValue(byte[] payload) {
        return Optional.of((T) new Integer(new BigInteger(wrapPayload(payload)).intValue() & ((1 << (8 * typeSize)) - 1)));
    }
}
