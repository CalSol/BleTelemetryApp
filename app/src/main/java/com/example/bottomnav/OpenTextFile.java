package com.example.bottomnav;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
public class OpenTextFile {
    Optional<char[]> code;

    public OpenTextFile(String fileName) throws IOException {
        /** Source: https://www.oreilly.com/content/how-to-convert-an-inputstream-to-a-string/ */
        InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
        if (is != null) {
            ByteArrayOutputStream barOutStream = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int length;
            while ((length = is.read(buf)) != -1) {
                barOutStream.write(buf, 0, length);
            }
            code = Optional.of(barOutStream.toString().toCharArray());
        } else {
            code = Optional.empty();
        }
    }

 }
