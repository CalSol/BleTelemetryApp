package com.example.bottomnav;

import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class DecoderData<T> {

    abstract Optional<T> decodeToRaw(byte[] payload);

    abstract Optional<ArrayList<DecodedContents>> decodeToString(byte[] payload);

    abstract boolean isPrimitive();

    abstract boolean isStructure();
}
