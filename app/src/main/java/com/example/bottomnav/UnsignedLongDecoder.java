package com.example.bottomnav;

import java.math.BigInteger;
import java.util.Optional;

public class UnsignedLongDecoder<T> extends IntegerDecoder {
    Long sign = (long) 0xffffffffL;

    public UnsignedLongDecoder(String name) {
        super(4, name);
    }

    @Override
    public Optional<T> getRawValue(byte[] payload) {
        return Optional.of((T) new Long(new BigInteger(wrapPayload(payload)).longValue() & sign));
    }
}
