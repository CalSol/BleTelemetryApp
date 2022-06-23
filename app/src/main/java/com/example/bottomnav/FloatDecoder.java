package com.example.bottomnav;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class FloatDecoder<T> extends IntegerDecoder {

    public FloatDecoder(int byteSize, String dataType) {
        super(byteSize, dataType);
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
