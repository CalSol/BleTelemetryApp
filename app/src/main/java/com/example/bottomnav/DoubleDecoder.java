package com.example.bottomnav;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DoubleDecoder<T> extends IntegerDecoder {
    public DoubleDecoder(int byteSize, String dataType) {
        super(byteSize, dataType);
    }

    @Override
    public String decode(Integer canId, byte[] payload) {
        rawValue = (T) new Double(ByteBuffer.wrap(payload).order(ByteOrder.LITTLE_ENDIAN).getDouble());
        value = "" + payloadDataType + ": " + rawValue;
        return value;
    }
}
