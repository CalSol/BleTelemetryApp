package com.example.bottomnav;

import java.nio.ByteBuffer;
import java.util.Optional;

public class PrimitiveDecoder<T> implements DataDecoder {
    protected int packetSize;
    protected String variableName;

    public PrimitiveDecoder(int size, String name) {
        packetSize = size;
        variableName = name;
    }

    @Override
    public Optional<T> decodeToRaw(byte[] payload) {
        if (payload.length < packetSize) {
            return Optional.empty();
        }
        return getRawValue(payload);
    }

    @Override
    public Optional<String> decodeToString(byte[] payload) {
        Optional<T> rawOptional = decodeToRaw(payload);
        if (rawOptional.isPresent()) {
            return Optional.of(variableName + ": " + rawOptional.get());
        } else {
            return Optional.empty();
        }
    }

    public Optional<T> getRawValue(byte[] payload) {
        return null;
    }

    public byte[] wrapPayload(byte[] payload) {
        ByteBuffer bb = ByteBuffer.wrap(payload);
        byte[] packet = new byte[packetSize];
        bb.get(packet, 0, packetSize);
        return packet;
    }
}
