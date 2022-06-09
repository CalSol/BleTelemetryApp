package com.example.bottomnav;

class CAN_Data {
    int id;
    int data;
    int len;

    public CAN_Data(int id, int data, int len) {
        this.id = id;
        this.data = data;
        this.len = len;
    }

    public static CAN_Data decode(String raw) {
        int id = -1;
        int len;
        int data;
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
        data = Integer.parseInt(raw.substring(index + 1, index + 1 + 2 * len), 16);
        return new CAN_Data(id, data, len);
    }

    public Integer getId(){
        return id;
    }

    public Integer getData() {
        return data;
    }

    public Integer getLen() {
        return len;
    }

    public void setData(Integer data) {
        this.data = data;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setLen(Integer len) {
        this.len = len;
    }

}
