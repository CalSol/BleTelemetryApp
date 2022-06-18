package com.example.bottomnav;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PrimativeDecoder<T> implements DataDecoder {
    private int packetSize;
    private String payloadDataType;
    private T rawValue;
    private String value;

    public PrimativeDecoder(int byteSize, String dataType) {
        payloadDataType = dataType;
        packetSize = byteSize;
    }

    @Override
    public String decode(Integer canId, byte[] payload) {
        ByteBuffer bb = ByteBuffer.wrap(payload);
        byte[] packet = new byte[packetSize];
        bb.get(packet, 0, packetSize);
        switch (payloadDataType) {
            case "int":
                rawValue = (T) new Integer(packet[0] & 0xff);
                value = "int: " + rawValue;
                return valueToString();
            case "float":
                rawValue = (T) new Float(ByteBuffer.wrap(packet).order(ByteOrder.LITTLE_ENDIAN).getFloat());
                value = "float: " + rawValue;
                return valueToString();
            case "long":
                rawValue = (T) new Long(ByteBuffer.wrap(packet).order(ByteOrder.LITTLE_ENDIAN).getLong());
                value = "long: " + rawValue;
                return valueToString();
            case "uint8_t":
                rawValue = (T) new Integer(packet[0] & 0xff);
                value = "uint8_t: " + rawValue;
                return valueToString();
            case "uint16_t":
                rawValue = (T) new Integer(ByteBuffer.wrap(packet).order(ByteOrder.LITTLE_ENDIAN).getInt());
                value = "uint8_t: " + rawValue;
                return valueToString();
            case "uint32_t":
                rawValue = (T) new Integer(ByteBuffer.wrap(packet).order(ByteOrder.LITTLE_ENDIAN).getInt());
                value = "uint8_t: " + rawValue;
                return valueToString();
            default:
                return null;
        }
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
