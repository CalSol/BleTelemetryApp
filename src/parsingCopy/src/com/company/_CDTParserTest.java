package com.company;


import org.junit.Assert;
import org.junit.jupiter.api.Test;


class _CDTParserTest {
    @Test
    public void Comb1() throws Exception {
        String code = "int a =21; int b =31; void test() {a++;} ";
        _CDTParser comb1 = new _CDTParser(code);
        System.out.println(comb1.ids.get(1));
        System.out.println(comb1.value.get(1));
        Assert.assertEquals(code, comb1.result);

    }

    @Test
    public void stringsTest() throws Exception{
        String code ="String hello; String bruh; String bruvs ";
        _CDTParser stringsTest = new _CDTParser(code);
        Assert.assertEquals(code, stringsTest.result);
    }

    @Test
    public void zephyrTest() throws Exception{
        String code = "struct MPPTData {\n" +
                "  uint16_t arrayVoltage_10mV;\n" +
                "  uint16_t arrayCurrent_mA;\n" +
                "  uint16_t batteryVoltage_10mV;\n" +
                "  uint16_t temperature_10mC;\n" +
                "}";
        _CDTParser stringsTest = new _CDTParser(code);
        Assert.assertEquals(code, stringsTest.result);
    }

    //Test getting information
}