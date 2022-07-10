package com.example.bottomnav.ui.notifications;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Context.*;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.contentcapture.DataRemovalRequest;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.bottomnav.R;
import com.example.bottomnav.*;
import com.example.bottomnav.bluetoothlegatt.DeviceControlActivity;
import com.example.bottomnav.bluetoothlegatt.SampleGattAttributes;
import com.example.bottomnav.databinding.FragmentNotificationsBinding;
import com.example.bottomnav.ui.table.CAN_Data;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class NotificationsFragment extends Fragment {
    private final static String TAG = DeviceControlActivity.class.getSimpleName();
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    private TextView mConnectionState;
    private TextView mDataField;
    private String mDeviceName;
    private String mDeviceAddress;
    private ExpandableListView mGattServicesList;
    private BluetoothLeService mBluetoothLeService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    private boolean mConnected = false;
    private BluetoothGattCharacteristic mNotifyCharacteristic;

    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";

    private NotificationsViewModel notificationsViewModel;
    private FragmentNotificationsBinding binding;


    //Array of strings
    ArrayList<String> CAN_receiver = new ArrayList<>();
    ArrayAdapter<String> adapter;
    Parse parser = Parse.parseTextFile("decode.h");
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            Log.e(TAG, "initialize Bluetooth");
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                getActivity().finish();
            }
            // Automatically connects to the device upon successful start-up initialization.

            mBluetoothLeService.connect(mDeviceAddress);
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };
    private void updateConnectionState(final int resourceId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mConnectionState.setText(resourceId);
            }
        });
    }
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override

        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
           if (com.example.bottomnav.bluetoothlegatt.BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                getActivity().getActionBar().setTitle(mDeviceName);
                getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
                //change ui
            } else if (com.example.bottomnav.bluetoothlegatt.BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;

                //change ui
            }
            if (com.example.bottomnav.bluetoothlegatt.BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
            }
        }
    };



    public NotificationsFragment() throws Exception {
            Log.e(TAG, "exception sadge");
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        adapter = new ArrayAdapter<>(getActivity(),
                R.layout.listview_layout, CAN_receiver);
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        return view;
    }

    private void displayData(String data) {
        if (data != null) {
            String name;
            String val;
            Optional<CAN_Data> newDataOpt = CAN_Data.decode(data);
            if (!newDataOpt.isPresent()) {
                Log.i(TAG, "error in decode");
                return;
            }
            CAN_Data newData = newDataOpt.get();
            Optional<DataDecoder> option = parser.getDecoder(newData.getId());
            if (!option.isPresent()) {
                put(CAN_receiver, newData);
                sort(CAN_receiver);
                adapter.notifyDataSetChanged();
            } else {
                Optional<String> listData = parser.decode(newData.getId(), newData.getData());
                if(listData.isPresent()) {
                    DataDecoder decoder = option.get();
                    Log.i(TAG, CAN_receiver.toString());

                    for (int x = 0; x < decoder.getSize(); x++) {
                        name = decoder.getVarNameAt(x);
                        val = decoder.getValueStringAt(x);
                        put(newData, CAN_receiver, name, val);
                    }
                    sort(CAN_receiver);
                    adapter.notifyDataSetChanged();
                }
                else{
                    Log.i(TAG, "DATA MISMATCH");
                }
            }


        }
    }


    public void sort(ArrayList<String> list){
        CAN_ID_COMP comp = new CAN_ID_COMP();
        list.sort(comp);

    }
    public int getIDFromString(String id){
        String[] split = id.split("]");
        String split_id = split[0];
        int CAN_id= Integer.parseInt(split_id.substring(1));
        return CAN_id;
    }
    public void put(ArrayList<String> list, CAN_Data data){
        if(list != null){
            boolean flag = false;
            for(int x = 0; x<list.size();x++){
                if(getIDFromString(list.get(x)) == (data.getId()) ){
                    list.set(x, "["+data.getId()+"]"+":"+Arrays.toString(data.getData()));
                    flag = true;
                }
            }
            if(!flag){
                list.add("["+data.getId()+"]"+":"+Arrays.toString(data.getData()));
            }
        }
    }

    public void put(CAN_Data input, ArrayList<String> list, String name,
                                 String val){
        if(list != null && input != null){
            boolean flag = false;
            for(int x = 0; x<list.size();x++){
                if(getIDFromString(list.get(x))==input.getId()  ){
                        list.set(x,"["+input.getId()+"]"+name+":"+val);
                        flag = true;
                }
            }
            if(!flag){
                list.add("["+input.getId()+"]"+name+":"+val);
            }

        }

    }



    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(com.example.bottomnav.bluetoothlegatt.BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Intent intent = this.getActivity().getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
        Intent gattServiceIntent = new Intent(this.getActivity(), BluetoothLeService.class);
        System.out.println(this.getActivity().bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE));

        this.getActivity().registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
        Log.e(TAG, "bind");
        EditText editText = view.findViewById(R.id.editText);
        Button button = view.findViewById(R.id.Scan_button);
        // Creates an Adapter that adapts array CAN_receiver to display

        // Creates new button logic with counter as final one-element array
        // A listView is created and adapted
        ListView listView = view.findViewById(R.id.listy);
        listView.setAdapter(adapter);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}