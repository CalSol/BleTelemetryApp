package com.example.bottomnav;

public interface DataDecoder {
    static DataDecoder getPrimativeDecoder(PayLoadDataType dataType) {
        switch (dataType) {
            case Integer:
                return new IntegerDecoder();
            case Float:
                return new FloatDecoder();
            case Double:
                return new DoubleDecoder();
            case Long:
                return new LongDecoder();
            case uint8_t:
                return new IntegerDecoder();
            case uint16_t:
                return new IntegerDecoder();
            default:
                return null;
        }
    }

    static String getPrimativeValue(PayLoadDataType dataType, DataDecoder decoder) {
        switch (dataType) {
            case Integer:

                return "int: " + ((IntegerDecoder) decoder).value;
            case Float:
                return "float: " + ((FloatDecoder) decoder).value;
            case Double:
                return "double: " + ((DoubleDecoder) decoder).value;
            case Long:
                return "long: " + ((LongDecoder) decoder).value;
            case uint8_t:
                return "uint8_t: " + ((IntegerDecoder) decoder).value;
            case uint16_t:
                return "uint16_t: " + ((IntegerDecoder) decoder).value;
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

    String decode(Integer canId, byte[] payload);

    int getPacketSize();

}
