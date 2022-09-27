package com.example.bottomnav;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DecodedContents {
    private String nameVariable;
    private String decodedValue;

    public DecodedContents(String name, String value) {
        nameVariable = name;
        decodedValue = value;
    }

    public String getName() {
        return nameVariable;
    }

    public String getValue() {
        return decodedValue;
    }
}
