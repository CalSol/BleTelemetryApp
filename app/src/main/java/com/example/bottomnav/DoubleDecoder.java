package com.example.bottomnav;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Optional;

public class DoubleDecoder<T> extends PrimitiveDecoder{

    public DoubleDecoder(VariableContents contents) {
        super(contents);
    }

    @Override
    public Optional<String> decode(Integer canId, byte[] payload) {
        ByteBuffer bb = ByteBuffer.wrap(payload);
        byte[] packet = new byte[contents.packetSize];
        if(payload.length < contents.packetSize){
            return Optional.empty();
        }
        bb.get(packet, 0, contents.packetSize);
        rawValue = (T) new Double(ByteBuffer.wrap(payload).order(ByteOrder.LITTLE_ENDIAN).getDouble());
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
