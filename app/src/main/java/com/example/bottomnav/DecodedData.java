package com.example.bottomnav;

public interface DecodedData {
    static DecodedData decodePrimative(PayLoadDataType type) {
        switch (type) {
            case Integer:
                return new DecodedInteger();
            case Float:
                return new DecodedFloat();
            case Double:
                return new DecodedDouble();
            case Long:
                return new DecodedLong();
            case uint8_t:
                return new DecodedInteger();
            case uint16_t:
                return new DecodedInteger();
            default:
                return null;
        }
    }

    static PayLoadDataType lookup(String name) {
        for (PayLoadDataType dataType : PayLoadDataType.values()) {
            if (dataType.name().equalsIgnoreCase(name)) {
                return dataType;
            }
        }
        return null;
    }

    void decode();
}
