package com.company;

public class Main {

    public static void main(String[] args) throws Exception {
        String code = "const uint32_t CAN_PETALS_POS = 0x32;" +
                "const uint32_t CAN_PETALS_POS = 0x32;" +
                "const uint32_t CAN_MOTOR_POS = 0x32;" +
                "const uint64_t CAN_MOTOR_POS = 0x64;" +
                "const uint64_t CAN_MOTOR_POS = 0x64;" +
                "const uint128_t CAN_MOTOR_POS = 0x128;";

	    Parse ok = new Parse(code);
	    ok.showNameValue();
    }
}
