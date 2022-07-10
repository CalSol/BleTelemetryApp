package com.example.bottomnav.ui.table;



import java.security.cert.PKIXRevocationChecker;
import java.util.Arrays;
import java.util.Optional;

public class CAN_Data {
    int id;
    byte[] data;
    int id_size;
    public CAN_Data(int id, byte[] data,int size) {
        this.id = id;
        this.data = data;
        id_size = size;
    }

    public static Optional<CAN_Data> decode(String raw) {
        try {
            int id;
            int len;

            int index = -1;
            if (raw.charAt(0) == 't' || raw.charAt(0) == 'r') {
                id = Integer.parseInt(raw.substring(1, 4), 16);
                index = 4;
            } else if (raw.charAt(0) == 'T' || raw.charAt(0) == 'R') {
                id = Integer.parseInt(raw.substring(1, 9), 16);
                index = 9;
            } else {
                return null;
            }

            len = Integer.parseInt(raw.substring(index, index + 1), 16);
            byte[] data = new byte[len];

            for (int x = 0; x < len; x += 1) {

                data[x] = (byte) Integer.parseInt(raw.substring(2 * x + index + 1, 2 * x + 2 + index + 1), 16);
            }
            return Optional.of(new CAN_Data(id, data, index - 1));
        }catch(NumberFormatException e){
            return Optional.empty();

        }
    }

    public int getId(){
        return id;
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public String toString() {
        return  id +
                ":"+ Arrays.toString(data) +
                '}';
    }

    public int getId_size() {
        return id_size;
    }
}
