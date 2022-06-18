package com.example.bottomnav;

import java.util.Optional;

public interface DataDecoder<T> {
    static Optional<DataDecoder> getPrimativeDecoder(String dataType) {
        switch (dataType) {
            case "int":
                return Optional.of(new PrimativeDecoder<Integer>(1, dataType));
            case "float":
                return Optional.of(new PrimativeDecoder<Float>(4, dataType));
            case "long":
                return Optional.of(new PrimativeDecoder<Long>(8, dataType));
            case "uint8_t":
                return Optional.of(new PrimativeDecoder<Integer>(1, dataType));
            case "uint16_t":
                return Optional.of(new PrimativeDecoder<Integer>(2, dataType));
            case "uint32_t":
                return Optional.of(new PrimativeDecoder<Integer>(4, dataType));
            default:
                return Optional.empty();
        }
    }

    String decode(Integer canId, byte[] payload);

    String valueToString();

    T valueToRaw();
}
