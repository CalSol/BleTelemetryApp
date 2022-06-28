package com.example.bottomnav;

import java.util.Optional;

public interface DataDecoder<T> {
    static Optional<DataDecoder> getPrimativeDecoder(VariableContents contents) {
        switch (contents.payloadDataType) {
            case "float":
                return Optional.of(new FloatDecoder<Float>(4, contents));
            case "double":
                return Optional.of(new DoubleDecoder<Double>(8, contents));
            case "int":
                return Optional.of(new IntegerDecoder<Integer>(1, contents));
            case "int8_t":
                return Optional.of(new IntegerDecoder<Integer>(1, contents));
            case "int16_t":
                return Optional.of(new IntegerDecoder<Integer>(2, contents));
            case "int32_t":
                return Optional.of(new IntegerDecoder<Integer>(4, contents));
            case "uint8_t":
                return Optional.of(new UnsignedIntegerDecoder<Integer>(1, contents, (int) 0xff));
            case "uint16_t":
                return Optional.of(new UnsignedIntegerDecoder<Integer>(2, contents, (int) 0xffff));
            case "uint32_t":
                return Optional.of(new UnsignedLongDecoder<Long>(4, contents));
            default:
                return Optional.empty();
        }
    }

    String decode(Integer canId, byte[] payload);

    String valueToString();

    VariableContents getContents();

    T valueToRaw();
}
