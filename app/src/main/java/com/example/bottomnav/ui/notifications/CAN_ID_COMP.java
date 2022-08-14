package com.example.bottomnav.ui.notifications;

import java.util.Comparator;

public class CAN_ID_COMP implements Comparator<String> {
    public int getIDFromString(String id){
        String[] split = id.split("]");
        String split_id = split[0];
        int CAN_id= Integer.parseInt(split_id.substring(1));
        return CAN_id;
    }
    @Override
    public int compare(String o1, String o2) {
        int id = getIDFromString(o1);
        int id2 = getIDFromString(o2);
        if( id > id2){
           return 1;
        }
        else if(id2==id){
            return 0;
        }
        else{
            return -1;
        }
    }
}
