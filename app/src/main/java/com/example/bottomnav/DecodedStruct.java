package com.example.bottomnav;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class DecodedStruct<T> extends DecodedData {

    private ArrayList<DecodedData> members;

    public DecodedStruct(ArrayList<DecodedData> givenMembers) {
        members = givenMembers;
    }

    public ArrayList<DecodedData> getMembers() {
        return members;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    // String is not expected to be empty since DecoderData decodes
    public String dataToString() {
        ArrayList<String> toStringDecoded = new ArrayList<>();
        for(DecodedData decoded : members) {
            toStringDecoded.add(decoded.dataToString());
        }
        return String.join(", ", toStringDecoded);
    }
}
