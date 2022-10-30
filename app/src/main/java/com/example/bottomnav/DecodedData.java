package com.example.bottomnav;

/** DecodedData stores the decoded contents after decoding with decoders. Decoders first check if
 * data is appropraite before calling DecodeData. Primitive and struct decoders make an Optional of
 * DecodedData.
*/
public abstract class DecodedData {
    abstract String dataToString();
}
