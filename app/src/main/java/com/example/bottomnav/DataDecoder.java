package com.example.bottomnav;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class DataDecoder<T> {

    abstract Optional<T> decodeToRaw(byte[] payload);

    abstract Optional<String> decodeToString(byte[] payload);
}
