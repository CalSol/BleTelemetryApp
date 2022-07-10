package com.example.bottomnav;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Optional;

public class UnsignedLongDecoder<T> extends IntegerDecoder {
    Long sign;

    public UnsignedLongDecoder(VariableContents contents) {
        super(contents);
        sign = (long) 0xffffffffL;
    }

    @Override
    public Optional<String> decode(Integer canId, byte[] payload) {
        ByteBuffer bb = ByteBuffer.wrap(payload);
        byte[] packet = new byte[contents.packetSize];
        bb.get(packet, 0, contents.packetSize);
        rawValue = (T) new Long(new BigInteger(packet).longValue() & sign);
        value = "" + rawValue;
        Optional<String> option = Optional.of(valueToString());;
        if(option.isPresent()){
            return option;
        }
        else{
            return Optional.empty();
        }
    }
}
