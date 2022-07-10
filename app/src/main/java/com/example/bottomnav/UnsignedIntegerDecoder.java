package com.example.bottomnav;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Optional;

public class UnsignedIntegerDecoder<T> extends IntegerDecoder{
    int sign;

    public UnsignedIntegerDecoder(VariableContents contents, int givenSign) {
        super(contents);
        sign = givenSign;
    }

    @Override
    public Optional<String> decode(Integer canId, byte[] payload) {
        ByteBuffer bb = ByteBuffer.wrap(payload);
        byte[] packet = new byte[contents.packetSize];
        if(payload.length < contents.packetSize){
            return Optional.empty();
        }
        bb.get(packet, 0, contents.packetSize);
        rawValue = (T) new Integer(new BigInteger(packet).intValue() & sign);
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
