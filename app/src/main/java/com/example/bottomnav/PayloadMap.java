package com.example.bottomnav;
import java.util.ArrayList;

public class PayloadMap {
    public String struct;
    public ArrayList<String> canIDNames = new ArrayList<>();

    public PayloadMap(String structFullName) {
        struct = structFullName;
    }
}
