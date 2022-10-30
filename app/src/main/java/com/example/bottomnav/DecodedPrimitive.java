package com.example.bottomnav;

public class DecodedPrimitive extends DecodedData{
    private String name;
    private String value;

    public DecodedPrimitive(String givenName, String givenValue) {
        name =  givenName;
        value = givenValue;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    // String is not expected to be empty since DecoderData decodes
    @Override
    public String dataToString() {
        return name + ": " + value;
    }
}
