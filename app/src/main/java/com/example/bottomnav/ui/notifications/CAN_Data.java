package com.example.bottomnav.ui.notifications;


import java.util.Arrays;

class CAN_Data {
    int id;
    int[] data;

    public CAN_Data(int id, int[] data) {
        this.id = id;
        this.data = data;
    }

    public static CAN_Data decode(String raw) {
        int id = -1;
        int len;

        int index = -1;
        if (raw.charAt(0) == 't' || raw.charAt(0) == 'r') {
            id = Integer.parseInt(raw.substring(1, 4), 16);
            index = 4;
        }
        else if (raw.charAt(0) == 'T' || raw.charAt(0) == 'R') {
            id = Integer.parseInt(raw.substring(1, 9), 16);
            index = 9;
        }
        else {
            return null;
        }

        len = Integer.parseInt(raw.substring(index, index + 1), 16);
        int[] data = new int[len];

        for(int x = 0;x<len;x+=1) {
            data[x] = Integer.parseInt(raw.substring(2*x +index+1, 2*x+2+index+1), 16);
        }
        return new CAN_Data(id, data);
    }

    public int getId(){
        return id;
    }

    public int[] getData() {
        return data;
    }

    @Override
    public String toString() {
        return "CAN_Data{" +
                "id=" + id +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
