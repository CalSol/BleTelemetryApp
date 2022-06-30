package com.example.bottomnav;

import java.util.Optional;

public interface DataDecoder<T> {

    static Optional<DataDecoder> createPrimitiveDecoder(VariableContents contents) {
        switch (contents.payloadDataType) {
            case "int":
                contents.setPacketSize(1);
                return Optional.of(new IntegerDecoder<Integer>(contents));
            case "int8_t":
                contents.setPacketSize(1);
                return Optional.of(new IntegerDecoder<Integer>(contents));
            case "int16_t":
                contents.setPacketSize(2);
                return Optional.of(new IntegerDecoder<Integer>(contents));
            case "int32_t":
                contents.setPacketSize(4);
                return Optional.of(new IntegerDecoder<Integer>(contents));
            case "uint8_t":
                contents.setPacketSize(1);
                return Optional.of(new UnsignedIntegerDecoder<Integer>(contents, 0xff));
            case "uint16_t":
                contents.setPacketSize(2);
                return Optional.of(new UnsignedIntegerDecoder<Integer>(contents,  0xffff));
            case "uint32_t":
                contents.setPacketSize(4);
                return Optional.of(new UnsignedLongDecoder<Long>(contents));
            case "float":
                contents.setPacketSize(4);
                return Optional.of(new FloatDecoder<Float>(contents));
            case "double":
                contents.setPacketSize(8);
                return Optional.of(new DoubleDecoder<Double>(contents));
            default:
                return Optional.empty();
        }
    }

    String decode(Integer canId, byte[] payload);

    String valueToString();

    T valueToRaw();

    String getValueStringAt(int i);

    String getVarNameAt(int i);

    int getSize();
}
