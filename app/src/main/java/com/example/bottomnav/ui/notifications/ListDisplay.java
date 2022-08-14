package com.example.bottomnav.ui.notifications;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.bottomnav.R;

import java.util.ArrayList;

public class ListDisplay extends Activity {
    //Array of strings
    String[] CAN_receiver = new String[]{"petals", "bms", "dashboard", "petals", "petals",
            "dashboard", "bms", "dashboard", "lights", "petals", "bms"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_listview);
//
//        // Creates an Adapter that adapts array CAN_receiver to display
//        ArrayAdapter adapter = new ArrayAdapter<String>(this,
//                R.layout.activity_listview, CAN_receiver);
//
//        // A listView is created and adapted
//        ListView listView = findViewById(R.id.mobile_list);
//        listView.setAdapter(adapter);
    }









    // A kinda HashSet of CANPackets that only store 1 value
    ArrayList<Integer> index = new ArrayList<>();
    int x = 0;
    ArrayList<Double> values = new ArrayList<>();

    public void display() {
        // Just a loop
        while (x < 1000) {
            //get new CANPackets
            CANPacket packet = new CANPacket(0x02af2b17, 3.30);

            // Find position to insert packet._value into by looking through INDEX
            // If packet._id not already in INDEX, add it and then insert packet._value into VALUES
            for (int i = 0; i < index.size(); i++) {
                if (index.get(i) == packet._id) {
                    values.remove(i);
                    values.add(i, packet._value);
                } else if (i == index.size() - 1) {
                    index.add(packet._id);
                }
            }

            // Create new adapter and listView and display it
            ArrayAdapter adp = new ArrayAdapter(this, R.layout.listview_layout, index);
            ListView listView = findViewById(R.id.listy);
            listView.setAdapter(adp);
        }
    }

    class CANPacket {
        CANPacket(int id, double value) {
            _id = id;
            _value = value;
        }

        int _id;
        double _value;
    }
}
