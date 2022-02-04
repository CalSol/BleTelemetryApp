package com.company;

public class Main {

    public static void main(String[] args) throws Exception {
	    Parser p = new Parser();
        String code = "const uint32_t CAN_PETALS_POS = 0x42\n" +
                "const uint32_t CAN_WHEELS_POS = 0x43\n" +
                "const uint32_t CAN_TIRES_POS = 0x44\n" +
                "struct PetalsPos {\n" +
                "  uint8_t accelPos;\n" +
                "  uint8_t brakePos;\n" +
                "}";

	    p.parse(code);

    }
}

