package com.example.bottomnav;

import java.util.Optional;

public abstract class DecoderData<T> {

    abstract int getTypeSize();

    abstract Optional<T> decodeToRaw(byte[] payload);

    abstract Optional<DecodedData> decodeToData(byte[] payload);

}
