package com.company;

import java.io.File;
import static com.company.Utils.*;

public class Repository {
    public static final File CWD = new File(System.getProperty("user.dir"));
    public static final File CONTENTS = join(CWD, "contents");

    //Makes a new directory for parser, also contains directory of the contents
    public static void makeInitDir() {
        if (!join(CWD, ".gitlet").exists()) {
            CWD.mkdir();
            CONTENTS.mkdir();
        }
    }
}

