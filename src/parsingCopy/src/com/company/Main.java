package com.company;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws Exception {
        String code = "const uint32_t CAN_PETALS_POS = 0x42;\n" +
                "const uint32_t CAN_WHEELS_POS = 0x43;\n" +
                "const uint32_t CAN_TIRES_POS = 0x44;\n";

        Parse p = new Parse(code);
        System.out.println(p.contents.get("const uint32_t"));

        HashMap pl = p.contents.get("const uint32_t");

        System.out.println(pl.size());

        for (Object key : pl.keySet()) {
            System.out.println(pl.get(key));
            System.out.println(key);
        }

    }
}

