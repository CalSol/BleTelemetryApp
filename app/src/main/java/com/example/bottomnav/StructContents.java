package com.example.bottomnav;

import java.util.ArrayList;
import java.util.HashMap;

public class StructContents {
    public HashMap<String, ConstContents> associatedCanRepo = new HashMap<>();
    private ArrayList<VariableContents> structVariables;
    private StructDecoder decoder;


    public StructContents(ArrayList<VariableContents> variables) {
        structVariables = variables;
        decoder = new StructDecoder(structVariables);
    }

    public StructContents() {
        structVariables = null;
        decoder = null;
    }

    public String decode(Integer canID, byte[] payload) {
        return "bruh";
    }
}
