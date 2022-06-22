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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.bottomnav.R;

import com.example.bottomnav.bluetoothlegatt.DeviceControlActivity;
import com.example.bottomnav.bluetoothlegatt.SampleGattAttributes;
import com.example.bottomnav.databinding.FragmentNotificationsBinding;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    ArrayList<String> CAN_receiver = new ArrayList<String>();
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
           /* if (com.example.bottomnav.bluetoothlegatt.BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                //change ui
            } else if (com.example.bottomnav.bluetoothlegatt.BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                //change ui
            } else if (com.example.bottomnav.bluetoothlegatt.BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                //display services if want to
            } */
            if (com.example.bottomnav.bluetoothlegatt.BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {

                displayData(intent.getStringExtra(com.example.bottomnav.bluetoothlegatt.BluetoothLeService.EXTRA_DATA));
            }
        }
    };




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
            CAN_Data newData = CAN_Data.decode(data);
            CAN_receiver.add(newData.toString());
            adapter.notifyDataSetChanged();

            //display data in textview here
        }
    }
    private final ExpandableListView.OnChildClickListener servicesListClickListner =
            new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                            int childPosition, long id) {
                    if (mGattCharacteristics != null) {
                        final BluetoothGattCharacteristic characteristic =
                                mGattCharacteristics.get(groupPosition).get(childPosition);
                        final int charaProp = characteristic.getProperties();
                        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                            // If there is an active notification on a characteristic, clear
                            // it first so it doesn't update the data field on the user interface.
                            if (mNotifyCharacteristic != null) {
                                mBluetoothLeService.setCharacteristicNotification(
                                        mNotifyCharacteristic, false);
                                mNotifyCharacteristic = null;
                            }
                            mBluetoothLeService.readCharacteristic(characteristic);
                        }
                        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                            mNotifyCharacteristic = characteristic;
                            mBluetoothLeService.setCharacteristicNotification(
                                    characteristic, true);
                        }
                        return true;
                    }
                    return false;
                }
            };



    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(com.example.bottomnav.bluetoothlegatt.BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(com.example.bottomnav.bluetoothlegatt.BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(com.example.bottomnav.bluetoothlegatt.BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(com.example.bottomnav.bluetoothlegatt.BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Intent intent = this.getActivity().getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
        Intent gattServiceIntent = new Intent(this.getActivity(), com.example.bottomnav.ui.notifications.BluetoothLeService.class);
        System.out.println(this.getActivity().bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE));

        this.getActivity().registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }

        Log.e(TAG, "bind");
        EditText editText = view.findViewById(R.id.editText);
        Button button = view.findViewById(R.id.addButton);
        // Creates an Adapter that adapts array CAN_receiver to display

        // Creates new button logic with counter as final one-element array

        View.OnClickListener onClickListener = v -> { // lambda function
            CAN_receiver.add(editText.getText().toString());
        };
        button.setOnClickListener(onClickListener);
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