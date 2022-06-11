package com.example.bottomnav;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class DecodedStruct extends DecodedData {
    public ArrayList<DecodedData> decodedValues = new ArrayList<>();

    public DecodedStruct(Integer canID, byte[] payload, ArrayList<StructContents> variables) {

    }

    public void DecodeStruct(Integer canID, byte[] payload, ArrayList<StructContents> variables) {
        ByteBuffer bb = ByteBuffer.wrap(payload);
        int index = 1;

        PayLoadDataType payLoadDataType = PayLoadDataType.valueOf(variables.get(index).type);


    }

    private byte[] getBytes(ByteBuffer bb, int index, int num) {
        byte[] temp = new byte[num];
        bb.get(temp, index, num);
        return temp;
    }
}
