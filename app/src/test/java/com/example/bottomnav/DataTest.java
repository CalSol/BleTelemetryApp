package com.example.bottomnav;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DataTest {
    @Test
    public void randomData() {
        String data = "t056111";
        CAN_Data newData = CAN_Data.decode(data);
        System.out.println(newData.getData());
        assertEquals( 0x11 , newData.getData()[0]);
        assertEquals( 86 , newData.getId());
    }
    @Test
    public void BIGData() {
        String data = "t0568111111111111111A";
        CAN_Data newData = CAN_Data.decode(data);
        System.out.println(newData.getData());
        assertEquals( 0x11, newData.getData()[0]);
        assertEquals( 8, newData.getData().length);
    }
}
