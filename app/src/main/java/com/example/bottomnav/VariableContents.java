package com.example.bottomnav;

public class VariableContents {
    String value;
    String name;
    String typeQualifer;
    String payloadDataType;

    public VariableContents(String iName, String iValue, String iTQ, String iType) {
        value = iValue;
        name = iName;
        typeQualifer = iTQ;
        payloadDataType = iType;
    }
}
