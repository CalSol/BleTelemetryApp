package com.example.bottomnav;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class IntegerDecoder<T> extends PrimitiveDecoder {

    public IntegerDecoder(int byteSize, String dataType, ConstContents contents) {
        super(byteSize, dataType, contents);
    }

    @Override
    public String decode(Integer canId, byte[] payload) {
        ByteBuffer bb = ByteBuffer.wrap(payload);
        byte[] packet = new byte[packetSize];
        bb.get(packet, 0, packetSize);
        rawValue = (T) new Integer(new BigInteger(packet).intValue() & 0xff);
        value = "" + payloadDataType + ": " + rawValue;
        return value;
    }

    @Override
    public String valueToString() {
        return value;
    }

    @Override
    public T valueToRaw() {
        return rawValue;
    }

    public int getPacketSize() {
        return packetSize;
    }
}
