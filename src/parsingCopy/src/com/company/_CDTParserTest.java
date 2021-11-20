package com.company;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

class _CDTParserTest {
    @Test
    public void testIdNValue() throws Exception {
        String code = "int a = 21; int b = 31;";
        _CDTParser test = new _CDTParser(code);
        Assert.assertEquals("a", test.ids.get(0));
        Assert.assertEquals("21", test.value.get(0));
    }

    @Test
    public void testRepo() throws Exception {
        String code = "int a = 21; int b = 31;";
        _CDTParser test = new _CDTParser(code);
        Assert.assertEquals("21", test.repo.get("a"));
        Assert.assertEquals("31", test.repo.get("b"));
    }

    @Test
    public void testRepo2() throws Exception{
        String code = "struct MPPTData {\n" +
                "  uint16_t arrayVoltage_10mV;\n" +
                "  uint16_t arrayCurrent_mAm;\n" +
                "  uint16_t batteryVoltage_10mV;\n" +
                "  uint16_t temperature_10mC;\n" +
                "}";
        _CDTParser test = new _CDTParser(code);
        Assert.assertEquals(true, test.repo.containsKey("arrayVoltage_10mV"));
        Assert.assertEquals(true, test.repo.containsKey("arrayCurrent_mAm"));
        Assert.assertEquals(true, test.repo.containsKey("batteryVoltage_10mV"));
        Assert.assertEquals(true, test.repo.containsKey("temperature_10mC"));
    }


    @Test
    public void testMapping() throws Exception {
        String code = "const uint32_t CAN_PETALS_POS = 0x42\n" +
                "const uint32_t CAN_WHEELS_POS = 0x43\n" +
                "const uint32_t CAN_TIRES_POS = 0x44\n" +
                "struct PetalsPos {\n" +
                "  uint8_t accelPos;\n" +
                "  uint8_t brakePos;\n" +
                "}";
        _CDTParser test = new _CDTParser(code);

        String name1 = "CAN_PETALS_POS";
        String id1 = "0x42";
        String name2 = "CAN_WHEELS_POS";
        String id2 = "0x43";
        String name3 = "CAN_TIRES_POS";
        String id3 = "0x44";

        Assert.assertEquals(15, (int) test.mapping.get(name1));
        Assert.assertEquals(52, (int) test.mapping.get(name2));
        Assert.assertEquals(89, (int) test.mapping.get(name3));

        int locationCANPETALPOS = test.mapping.get(name1) + name1.length() + 3;
        int locationNANWHEELSPOS = test.mapping.get(name2) + name2.length() + 3;
        int locationCANTIRESPOS = test.mapping.get(name3) + name3.length() + 3;

        Assert.assertEquals(locationCANPETALPOS, (int) test.mapping.get(id1));
        Assert.assertEquals(locationNANWHEELSPOS, (int) test.mapping.get(id2));
        Assert.assertEquals(locationCANTIRESPOS, (int) test.mapping.get(id3));

    }

    @Test
    public void testRepo3() throws Exception {
        String code = "const uint32_t CAN_PETALS_POS = 0x42\n" +
                "const uint32_t CAN_WHEELS_POS = 0x43\n" +
                "const uint32_t CAN_TIRES_POS = 0x44\n" +
                "const uint32_t CAN_Bre_POS = 0x44\n" +
                "struct PetalsPos {\n" +
                "  uint8_t accelPos;\n" +
                "  uint8_t brakePos;\n" +
                "}";
        _CDTParser test = new _CDTParser(code);
        String payload1 = "0x01";
        String payload2 = "0x02";
        System.out.println(test.value.get(1));
    }
}