package com.example.bottomnav;

import java.math.BigInteger;
import java.nio.ByteBuffer;

public class IntegerDecoder<T> extends PrimitiveDecoder {

    public IntegerDecoder(int byteSize, VariableContents contents) {
        super(byteSize, contents);
    }

    @Override
    public String decode(Integer canId, byte[] payload) {
        ByteBuffer bb = ByteBuffer.wrap(payload);
        byte[] packet = new byte[packetSize];
        bb.get(packet, 0, packetSize);
        rawValue = (T) new Integer(new BigInteger(packet).intValue());
        value = "" + contents.name + ": " + rawValue;
        return value;
    }

    @Override
    public String valueToString() {
        return value;
    }

    public int getPacketSize() {
        return packetSize;
    }
}
