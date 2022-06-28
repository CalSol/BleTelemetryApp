package com.example.bottomnav;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class FloatDecoder<T> extends PrimitiveDecoder {

    public FloatDecoder(int byteSize, String dataType, ConstContents con) {
        super(byteSize, dataType, con);
    }

    @Override
    public String decode(Integer canId, byte[] payload) {
        ByteBuffer bb = ByteBuffer.wrap(payload);
        byte[] packet = new byte[packetSize];
        bb.get(packet, 0, packetSize);
        rawValue = (T) new Float(ByteBuffer.wrap(packet).order(ByteOrder.LITTLE_ENDIAN).getFloat());
        value = "" + payloadDataType + ": " + rawValue;
        return value;
    }
}
