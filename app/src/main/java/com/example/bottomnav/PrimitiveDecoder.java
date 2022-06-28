package com.example.bottomnav;

public class PrimitiveDecoder<T> implements DataDecoder {
    protected int packetSize;
    protected String payloadDataType;
    protected VariableContents contents;
    protected T rawValue;
    protected String value;

    public PrimitiveDecoder(int byteSize, String dataType, VariableContents can) {
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
        return value;
    }

    @Override
    public T valueToRaw() {
        return rawValue;
    }

    @Override
    public VariableContents getContents() {
        return contents;
    }

    public int getPacketSize() {
        return packetSize;
    }
}
