package com.example.bottomnav;

public class PrimitiveDecoder<T> implements DataDecoder {
    protected int packetSize;
    protected String payloadDataType;
    protected ConstContents contents;
    protected T rawValue;
    protected String value;

    public PrimitiveDecoder(int byteSize, String dataType, ConstContents can) {
        packetSize = byteSize;
        payloadDataType = dataType;
        contents = can;
    }

    @Override
    public String decode(Integer canId, byte[] payload) {
        return null;
    }

    @Override
    public String valueToString() {
        return null;
    }

    @Override
    public T valueToRaw() {
        return null;
    }

    @Override
    public ConstContents getContents() {
        return contents;
    }
}
