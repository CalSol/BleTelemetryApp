package com.example.bottomnav;


import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

public class ParseTest {

    @Test
    public void doesNotExist() throws Exception {
        Parse test = new Parse("doesNotExist.txt");

        assertEquals(null, test.getConst("CAN_ID"));
        assertEquals(null, test.getConst("ducky"));
    }

    @Test
    public void simpleCheck() throws Exception {
        Parse test = new Parse("simpleCheck.txt");
        HashMap<String, Contents> repository = test.constRepo;

        assertEquals(true, repository.containsKey("CAN_ID"));
        assertEquals(true, repository.containsKey("CAN_ID2"));
        assertEquals(true, repository.containsKey("CAN_ID3"));

        assertEquals("const", test.getConst("CAN_ID").typeQualifer);
        assertEquals("const", test.getConst("CAN_ID2").typeQualifer);
        assertEquals("const", test.getConst("CAN_ID3").typeQualifer);

        assertEquals("uint16_t", test.getConst("CAN_ID").type);
        assertEquals("uint16_t", test.getConst("CAN_ID2").type);
        assertEquals("uint16_t", test.getConst("CAN_ID3").type);

        assertEquals("0x16", test.getConst("CAN_ID").value);
        assertEquals("0x32", test.getConst("CAN_ID2").value);
        assertEquals("0x64", test.getConst("CAN_ID3").value);
    }

    @Test
    public void simpleCheck2() throws Exception {
        Parse test = new Parse("simpleCheck2.txt");
        HashMap<String, Contents> repository = test.constRepo;
        assertEquals("const", test.getConst("CAN_ID1").typeQualifer);
        assertEquals("const", test.getConst("CAP_ID2").typeQualifer);
        assertEquals("const", test.getConst("CAN_ID3").typeQualifer);
        assertEquals("uint16_t", test.getConst("CAN_ID1").type);
        assertEquals("uint32_t", test.getConst("CAP_ID2").type);
        assertEquals("uint32_t", test.getConst("CAN_ID3").type);
        assertEquals("0x16", test.getConst("CAN_ID1").value);
        assertEquals("0x18", test.getConst("CAP_ID2").value);
        assertEquals("0x64", test.getConst("CAN_ID3").value);
    }

    @Test
    public void structs() throws Exception {
        Parse test = new Parse("structs.txt");
        ArrayList<StructContents> contents = test.getStruct("ChargerControlStruct");

        assertEquals("voltage_be", contents.get(0).name);
        assertEquals("current_be", contents.get(1).name);
        assertEquals("control", contents.get(2).name);
        assertEquals("reserved1", contents.get(3).name);
        assertEquals("reserved2", contents.get(4).name);
        assertEquals("reserved3", contents.get(5).name);
        assertEquals("uint16_t", contents.get(0).type);
        assertEquals("uint16_t", contents.get(1).type);
        assertEquals("uint8_t", contents.get(2).type);
        assertEquals("uint8_t", contents.get(3).type);
        assertEquals("uint8_t", contents.get(4).type);
        assertEquals("uint8_t", contents.get(5).type);
    }

    @Test
    public void structNConst() throws Exception {
        Parse test = new Parse("structNConst.txt");
        ArrayList<StructContents> contents = test.getStruct("ChargerControlStruct");

        assertEquals("voltage_be", contents.get(0).name);
        assertEquals("current_be", contents.get(1).name);
        assertEquals("control", contents.get(2).name);
        assertEquals("reserved1", contents.get(3).name);
        assertEquals("reserved2", contents.get(4).name);
        assertEquals("reserved3", contents.get(5).name);

        assertEquals("uint16_t", contents.get(0).type);
        assertEquals("uint16_t", contents.get(1).type);
        assertEquals("uint8_t", contents.get(2).type);
        assertEquals("uint8_t", contents.get(3).type);
        assertEquals("uint8_t", contents.get(4).type);
        assertEquals("uint8_t", contents.get(5).type);

        assertEquals("const", test.getConst("CAN_ID").typeQualifer);
        assertEquals("uint16_t", test.getConst("CAN_ID").type);
        assertEquals("0x16", test.getConst("CAN_ID").value);

        assertEquals("const", test.getConst("CAN_ID3").typeQualifer);
        assertEquals("uint16_t", test.getConst("CAN_ID3").type);
        assertEquals("0x64", test.getConst("CAN_ID3").value);
    }

    @Test
    public void parseData() throws Exception {
        Parse test = new Parse("parseData.txt");
        HashMap<String, Contents> repository = test.constRepo;
        ArrayList<StructContents> struct1 = test.getStruct("ChargerControlStruct");
        ArrayList<StructContents> struct2 = test.getStruct("ChargerControlStruct");

        assertEquals(true, repository.containsKey("CAN_HEART_BMS"));
        assertEquals(true, repository.containsKey("CAN_BMS_FAN_SETPOINT"));
        assertEquals(true, repository.containsKey("CAN_CHARGER_STATUS"));

        assertEquals("const", test.getConst("CAN_HEART_BMS").typeQualifer);
        assertEquals("const", test.getConst("CAN_BMS_FAN_SETPOINT").typeQualifer);
        assertEquals("const", test.getConst("CAN_CHARGER_STATUS").typeQualifer);

        assertEquals("uint16_t", test.getConst("CAN_HEART_BMS").type);
        assertEquals("uint16_t", test.getConst("CAN_BMS_FAN_SETPOINT").type);
        assertEquals("uint16_t", test.getConst("CAN_CHARGER_STATUS").type);

        assertEquals("0x040", test.getConst("CAN_HEART_BMS").value);
        assertEquals("0x560", test.getConst("CAN_BMS_FAN_SETPOINT").value);
        assertEquals("0x18FF50E5", test.getConst("CAN_CHARGER_STATUS").value);

        assertEquals("voltage_be", struct1.get(0).name);
        assertEquals("current_be", struct1.get(1).name);
        assertEquals("control", struct1.get(2).name);
        assertEquals("reserved1", struct1.get(3).name);
        assertEquals("reserved2", struct1.get(4).name);
        assertEquals("reserved3", struct1.get(5).name);
        assertEquals("uint16_t", struct1.get(0).type);
        assertEquals("uint16_t", struct1.get(1).type);
        assertEquals("uint8_t", struct1.get(2).type);
        assertEquals("uint8_t", struct1.get(3).type);
        assertEquals("uint8_t", struct1.get(4).type);
        assertEquals("uint8_t", struct1.get(5).type);

        assertEquals("voltage_be", struct2.get(0).name);
        assertEquals("current_be", struct2.get(1).name);
        assertEquals("control", struct2.get(2).name);
        assertEquals("reserved1", struct2.get(3).name);
        assertEquals("reserved2", struct2.get(4).name);
        assertEquals("reserved3", struct2.get(5).name);
        assertEquals("uint16_t", struct2.get(0).type);
        assertEquals("uint16_t", struct2.get(1).type);
        assertEquals("uint8_t", struct2.get(2).type);
        assertEquals("uint8_t", struct2.get(3).type);
        assertEquals("uint8_t", struct2.get(4).type);
        assertEquals("uint8_t", struct2.get(5).type);

    }
}
