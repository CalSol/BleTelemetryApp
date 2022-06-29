package com.example.bottomnav;

public class PrimitiveDecoder<T> implements DataDecoder {
    protected VariableContents contents;
    protected T rawValue;
    protected String value;

    public PrimitiveDecoder(VariableContents can) {
        contents = can;
    }

    @Override
    public String decode(Integer canId, byte[] payload) {
        return null;
    }

    @Override
    public String valueToString() {
        return "" + contents.name + ": " + value;
    }

    @Override
    public T valueToRaw() {
        return rawValue;
    }

    @Override
    public VariableContents getContents() {
        return contents;
    }

    @Override
    public String getVarName() {
        return contents.name;
    }

    @Override
    public String getValueString() {
        return value;
    }

    @Override
    public PrimitiveDecoder getPrimitiveDecoder(int i) {
        return this;
    }

    public int getPacketSize() {
        return contents.packetSize;
    }
}
