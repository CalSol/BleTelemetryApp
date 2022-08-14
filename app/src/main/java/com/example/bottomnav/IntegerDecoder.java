package com.example.bottomnav;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Optional;

public class IntegerDecoder<T> extends PrimitiveDecoder {
    public IntegerDecoder(VariableContents contents) {
        super(contents);
    }

    @Override
    public Optional<String> decode(Integer canId, byte[] payload) {
        ByteBuffer bb = ByteBuffer.wrap(payload);
        byte[] packet = new byte[contents.packetSize];
        bb.get(packet, 0, contents.packetSize);
        rawValue = (T) new Integer(new BigInteger(packet).intValue());


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
