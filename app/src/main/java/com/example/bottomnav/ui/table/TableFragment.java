package com.example.bottomnav.ui.table;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bottomnav.R;
import com.example.bottomnav.*;
import com.example.bottomnav.bluetoothlegatt.DeviceControlActivity;
import com.example.bottomnav.bluetoothlegatt.SampleGattAttributes;
import com.example.bottomnav.databinding.FragmentNotificationsBinding;
import com.example.bottomnav.ui.table.CAN_Data;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableFragment extends Fragment {
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

    private TableViewModel notificationsViewModel;
    private FragmentNotificationsBinding binding;


    //Array of strings
    ArrayList<CAN_Data> CAN_receiver = new ArrayList<CAN_Data>();
    ArrayAdapter<String> adapter;


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
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override

        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (com.example.bottomnav.bluetoothlegatt.BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                try {
                    displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };






    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_table, container, false);
        return view;
    }

    private void displayData(String data) throws Exception {
        if (data != null) {
            Parse parser = Parse.parseTextFile("decode.h");
            String name;
            String val;
//            CAN_Data newData = CAN_Data.decode(data);
//            String listData = parser.decode(newData.getId(), newData.getData());
//            DataDecoder decoder = parser.getDecoder(newData.getId()).get();
//
//
//            name = decoder.getVarNameAt(0);
//            val = decoder.getValueStringAt(0);
//            CAN_receiver.add(newData);
//

            adapter.notifyDataSetChanged();
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

        // Creates an Adapter that ad(apts array CAN_receiver to display

        // Creates new button logic with counter as final one-element array
        // A listView is created and adapted
        RecyclerView gridView = view.findViewById(R.id.lister);
        //Log.i(TAG, gridView);
        gridView.setLayoutManager(new GridLayoutManager(this.getActivity(), 2));
        CustomArrayAdapter customAdapter = new CustomArrayAdapter(this.getActivity(), R.layout.listview_layout, CAN_receiver);
        gridView.setAdapter(customAdapter);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}