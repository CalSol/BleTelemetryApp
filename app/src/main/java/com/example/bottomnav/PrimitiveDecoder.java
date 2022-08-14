package com.example.bottomnav;

import java.util.Optional;

public class PrimitiveDecoder<T> implements DataDecoder {
    protected VariableContents contents;
    protected T rawValue;
    protected String value;
    protected int size = 1;

    public PrimitiveDecoder(VariableContents can) {
        contents = can;
    }

    @Override
    public Optional<String> decode(Integer canId, byte[] payload) {
        return null;
    }

    @Override
    public int getSize() {
        return size;
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
    public String getVarNameAt(int i) {
        return contents.name;
    }

    public String getVarName() {
        return contents.name;
    }

    @Override
    public String getValueStringAt(int i) {
        return value;
    }

    public String getValueString() {
        return value;
    }

    public int getPacketSize() {
        return contents.packetSize;
    }
}
