package com.example.bottomnav;

import java.util.Optional;

public class ConstContents {
    String value;
    String name;
    String typeQualifer;
    String payLoadDataType;

    public ConstContents(String iName, String iValue, String iTQ, String iType) {
        value = iValue;
        typeQualifer = iTQ;
        name = iName;
        payLoadDataType = iType;
    }
}
