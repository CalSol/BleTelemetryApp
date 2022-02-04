package com.company;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

class _CDTParserTest {
    @Test
    public void testGetID() throws Exception {
        String code = "const uint32_t CAN_PETALS_POS = 0x42;";
        _CDTParser test = new _CDTParser(code);
        Assert.assertEquals("0x42", test.getID("CAN_PETALS_POS"));
    }

    @Test
    public void testMapping() throws Exception {
        String code = "const uint32_t CAN_PETALS_POS = 0x42;\n" +
                "const uint32_t CAN_WHEELS_POS = 0x43;\n" +
                "const uint32_t CAN_TIRES_POS = 0x44;\n" +
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

        Assert.assertEquals(15, (int) test.repo.get(name1));
        Assert.assertEquals(52, (int) test.repo.get(name2));
        Assert.assertEquals(89, (int) test.repo.get(name3));

        int locationCANPETALPOS = test.repo.get(name1) + name1.length() + 3;
        int locationNANWHEELSPOS = test.repo.get(name2) + name2.length() + 3;
        int locationCANTIRESPOS = test.repo.get(name3) + name3.length() + 3;

        Assert.assertEquals(locationCANPETALPOS, (int) test.repo.get(id1));
        Assert.assertEquals(locationNANWHEELSPOS, (int) test.repo.get(id2));
        Assert.assertEquals(locationCANTIRESPOS, (int) test.repo.get(id3));
    }

    @Test
    public void testRepo2() throws Exception {
        String code = "struct PetalsPos {\n" +
                "  uint8_t accelPos;\n" +
                "  uint8_t brakePos;\n" +
                "}";
        _CDTParser test = new _CDTParser(code);

        String name1 = "PetalsPos";
        String name2 = "accelPos";
        String name3 = "brakePos";

        Assert.assertEquals(true, test.repo.containsKey(name1));
        Assert.assertEquals(true, test.repo.containsKey(name2));
        Assert.assertEquals(true, test.repo.containsKey(name3));
    }
}