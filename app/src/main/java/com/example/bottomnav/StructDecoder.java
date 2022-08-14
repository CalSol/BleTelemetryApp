package com.example.bottomnav;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class StructDecoder implements DataDecoder {
    private HashMap<String, DataDecoder> decodedPrimatives = new HashMap<>();
    public ArrayList<VariableContents> variables;
    private int size;

    public StructDecoder(ArrayList<VariableContents> con) {
        variables = con;
        size = variables.size();
    }

    @Override
    public String valueToString() {
        StringBuilder message = new StringBuilder();
        for (int i = 0; i < variables.size(); i++) {
            String varName = variables.get(i).name;
            message.append(decodedPrimatives.get(varName).valueToString());
            message.append("\n");
        }
        return message.toString();
    }

    @Override
    public Object valueToRaw() {
        return null;
    }

    @Override
    public Optional<String> decode(Integer canId, byte[] payload) {
        for (VariableContents variable : variables) {
            Optional<DataDecoder> decoder = DataDecoder.createPrimitiveDecoder(variable);
            if (decoder.isPresent()) {
                decoder.get().decode(canId, payload);
                decodedPrimatives.put(variable.name, decoder.get());
                payload = adjustPayload(payload, ((PrimitiveDecoder) decoder.get()).getPacketSize());
            }
        }
        Optional<String> option = Optional.of(valueToString());;
        if(option.isPresent()){
            return option;
        }
        else{
            return Optional.empty();
        }
    }

    // Splicing function for each variable
    public byte[] adjustPayload(byte[] payload, int packetSize) {
        int newSize = payload.length - packetSize;
        ByteBuffer bb = ByteBuffer.wrap(payload);
        if ((payload.length / packetSize) > 1) {
            byte[] newPayload = new byte[newSize];
            bb.get(newPayload, 0, packetSize);
            bb.get(newPayload, 0, newSize);
            return newPayload;
        }
        return payload;
    }

    public PrimitiveDecoder getPrimitiveDecoder(int i) {
        return (PrimitiveDecoder) decodedPrimatives.get(variables.get(i).name);
    }

    @Override
    public String getValueStringAt(int i) {
        return ((PrimitiveDecoder) decodedPrimatives.get(variables.get(i).name)).getValueStringAt(0);
    }

    @Override
    public String getVarNameAt(int i) {
        return ((PrimitiveDecoder) decodedPrimatives.get(variables.get(i).name)).getVarNameAt(0);
    }

    @Override
    public int getSize() {
        return size;
    }
}
