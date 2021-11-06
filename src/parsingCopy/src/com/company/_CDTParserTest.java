package com.company;

import org.junit.jupiter.api.Test;

class _CDTParserTest {
    @Test
    public void Comb1() {
        try {
            new _CDTParser("int a; void test() {a++;}");
        } catch (Exception var2) {
            System.out.println("What is the purpose of the this exception?");
        }
    }

    @Test
    public void stringsTest() {
        try {
            new _CDTParser("String hello; String bruh; String bruvs");
        } catch (Exception var2) {
            System.out.println("What is the purpose of the this exception?");
        }
    }

    @Test
    public void objectsTest() {
        try {
            new _CDTParser("public static void test() {a++;}; private void test1(){a--;};" +
                    "public static int (int num) {num*num;}");
        } catch (Exception var2) {
            System.out.println("What is the purpose of the this exception?");
        }
    }

    //Having trouble here, need to identify C code, not java code, and interpret it into Java
    @Test
    public void leTest2() {
        try {
            new _CDTParser("#define CAN_HEART_BMS 0x040;#define CAN_HEART_CUTOFF 0x041;");
        } catch (Exception var2) {
            System.out.println("What is the purpose of the this exception?");
        }
    }
}