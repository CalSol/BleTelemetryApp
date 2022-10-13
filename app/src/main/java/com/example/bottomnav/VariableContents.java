package com.example.bottomnav;

public class VariableContents {
    public String name;
    public String typeQualifier;
    public String payloadDataType;
    public String value;

    public VariableContents(String givenName, String givenTQ,
                            String givenType, String givenValue) {
        name = givenName;
        typeQualifier = givenTQ;
        payloadDataType = givenType;
        value = givenValue;
    }
}
