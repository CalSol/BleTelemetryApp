package com.example.bottomnav;

import java.nio.ByteBuffer;
import java.util.Optional;

/**
 * PrimititiveDeocdoder than implements extends DataDecoder abstract class
 * Each general type of decoder needs a decode to raw and decode to string
 */
public abstract class PrimitiveDecoder<T> extends DataDecoder {
    protected int typeSize;
    protected String variableName;

    static Optional<DataDecoder> create(VariableContents contents) {
        switch (contents.payloadDataType) {
            case "int8_t":
                return Optional.of(new IntegerDecoder<Integer>(1, contents.name));
            case "int16_t":
                return Optional.of(new IntegerDecoder<Integer>(2, contents.name));
            case "int32_t":
                return Optional.of(new IntegerDecoder<Integer>(4, contents.name));
            case "uint8_t":
                return Optional.of(new UnsignedIntegerDecoder<Integer>(1, 0xff, contents.name));
            case "uint16_t":
                return Optional.of(new UnsignedIntegerDecoder<Integer>(2,  0xffff, contents.name));
            case "uint32_t":
                return Optional.of(new UnsignedLongDecoder<Long>(contents.name));
            case "int":
                return Optional.of(new IntegerDecoder<Integer>(4, contents.name));
            case "float":
                return Optional.of(new FloatDecoder<Float>(contents.name));
            case "double":
                return Optional.of(new DoubleDecoder<Double>(contents.name));
            default:
                return Optional.empty();
        }
    }

    public PrimitiveDecoder(int size, String name) {
        typeSize = size;
        variableName = name;
    }

    // decodeToRaw filters out any payload length less than primitive's typeSize
    @Override
    public Optional<T> decodeToRaw(byte[] payload) {
        if (payload.length != typeSize) {
            return Optional.empty();
        }
        return getRawValue(payload);
    }

    @Override
    public Optional<String> decodeToString(byte[] payload) {
        Optional<T> rawOptional = decodeToRaw(payload);
        if (rawOptional.isPresent()) {
            return Optional.of(variableName + ": " + rawOptional.get());
        } else {
            return Optional.empty();
        }
    }

    public byte[] wrapPayload(byte[] payload) {
        ByteBuffer bb = ByteBuffer.wrap(payload);
        byte[] packet = new byte[typeSize];
        bb.get(packet, 0, typeSize);
        return packet;
    }

    abstract Optional<T> getRawValue(byte[] payload);
}
