package com.example.bottomnav;

public interface DecodedData {
    public void decode();

    public static DecodedData decodePrimative(PayLoadDataType type) {
        switch (type) {
            case Integer:
                return new DecodedInteger();
            case Float:
                return new DecodedFloat();
            case uint8_t:
                return new DecodedUnsigned8();
            case uint16_t:
                return new DecodedUnsigned16();
            default:
                return null;
        }
    }
}
