package com.example.bottomnav;

import java.util.ArrayList;
import java.util.Optional;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface DataDecoder<T> {

    static Optional<DataDecoder> createPrimitiveDecoder(VariableContents contents) {
        switch (contents.payloadDataType) {
            case "int":
                return Optional.of(new IntegerDecoder<Integer>(1, contents.name));
            case "int8_t":
                return Optional.of(new IntegerDecoder<Integer>(1, contents.name));
            case "int16_t":
                return Optional.of(new IntegerDecoder<Integer>(2, contents.name));
            case "int32_t":
                return Optional.of(new IntegerDecoder<Integer>(4, contents.name));
            case "uint8_t":
                return Optional.of(new UnsignedIntegerDecoder<Integer>(1, 0xff, contents.name));
            case "uint16_t":
                return Optional.of(new UnsignedIntegerDecoder<Integer>(2,  0xffff, contents.name));
            case "uint32_t":
                return Optional.of(new UnsignedLongDecoder<Long>(4, contents.name));
            case "float":
                return Optional.of(new FloatDecoder<Float>(4, contents.name));
            case "double":
                return Optional.of(new DoubleDecoder<Double>(8, contents.name));
            default:
                return Optional.empty();
        }
    }

    static Optional<DataDecoder> createStructDecoder(ArrayList<VariableContents> variables) {
        ArrayList<Optional<DataDecoder>> decodedPrimatives = new ArrayList<>();
        for (VariableContents variable : variables) {
            Optional<DataDecoder> primitiveDecoder = DataDecoder.createPrimitiveDecoder(variable);
            if (!primitiveDecoder.isPresent()) {
                return Optional.empty();
            }
            decodedPrimatives.add(primitiveDecoder);
        }
        return Optional.of(new StructDecoder(decodedPrimatives));
    }

    public class Decoded {
        private String nameVariable;
        private String decodedValue;

        public Decoded(String name, String value) {
            nameVariable = name;
            decodedValue = value;
        }

        public String getName() {
            return nameVariable;
        }

        public String getValue() {
            return decodedValue;
        }
    }

    static ArrayList<Decoded> parseStringResult(String resultOfDecoder) {
        ArrayList<Decoded> nameValues = new ArrayList<>();
        String[] splitString = resultOfDecoder.split(",");
        Pattern pattern = Pattern.compile("(\\S+):\\s(\\S+)");
        for (String decoded : splitString) {
            Matcher matcher = pattern.matcher(decoded);
            if (matcher.find()) {
                String name = matcher.group(1);
                String value = matcher.group(2);
                nameValues.add(new Decoded(name, value));
            }
        } return nameValues;
    }

    Optional<T> decodeToRaw(byte[] payload);

    Optional<String> decodeToString(byte[] payload);


}
