package com.example.bottomnav;

import java.util.Optional;

public interface DataDecoder<T> {
    static Optional<DataDecoder> getPrimativeDecoder(String dataType, VariableContents contents) {
        switch (dataType) {
            case "int":
                return Optional.of(new IntegerDecoder<Integer>(1, dataType, contents));
            case "float":
                return Optional.of(new FloatDecoder<Float>(4, dataType, contents));
            case "double":
                return Optional.of(new DoubleDecoder<Double>(8, dataType, contents));
            case "uint8_t":
                return Optional.of(new IntegerDecoder<Integer>(1, dataType, contents));
            case "uint16_t":
                return Optional.of(new IntegerDecoder<Integer>(2, dataType, contents));
            case "uint32_t":
                return Optional.of(new IntegerDecoder<Integer>(4, dataType, contents));
            default:
                return Optional.empty();
        }
    }

    String decode(Integer canId, byte[] payload);

    String valueToString();

    VariableContents getContents();

    T valueToRaw();
}
