package com.example.bottomnav;

import java.math.BigInteger;
import java.util.Optional;

public class UnsignedIntegerDecoder<T> extends IntegerDecoder{
    int sign;

    public UnsignedIntegerDecoder(int size, int givenSign, String name) {
        super(size, name);
        sign = givenSign;
    }

    @Override
    public Optional<T> getRawValue(byte[] payload) {
        return Optional.of((T) new Integer(new BigInteger(wrapPayload(payload)).intValue() & sign));
    }
}
