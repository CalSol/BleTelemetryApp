package com.example.bottomnav.ui.notifications;

class CAN_Data {

    Integer id;
    Integer data;
    Integer len;
    public CAN_Data(Integer id,Integer data, Integer len ){
        this.id = id;
        this.data = data;
        this.len = len;
    }
    public static CAN_Data decode(String raw){
        Integer id = -1;
        Integer len;
        Integer data;
        int index = -1;
        if(raw.substring(0,1).equals('t') || raw.substring(0,1).equals('r')){
            id = Integer.parseInt(raw.substring(1, 4), 16);
            index = 4;
        }
        else if(raw.substring(0,1).equals('T') || raw.substring(0,1).equals('R')) {
            id = Integer.parseInt(raw.substring(1, 9), 16);
            index = 9;
        }
        if(index != -1&& id != -1) {
            len = Integer.parseInt(raw.substring(index, index + 1), 16);
            data = Integer.parseInt(raw.substring(index + 1, raw.length()), 16);
            CAN_Data decoded = new CAN_Data(id, data, len);
            return decoded;
        }
        else{
            return null;
        }

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
