package com.company;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**Not Important, ignore*/

public class Alternative {

    public static void main(String[] args) {
        FileInputStream fin = null;
        int track;

        try {
            fin = new FileInputStream("headerFile");
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }


        try {
            do {
                track = fin.read();
            } while (track != -1);
        } catch (IOException e) {
            System.out.println("Cannot read file.");
        }
    }
}
