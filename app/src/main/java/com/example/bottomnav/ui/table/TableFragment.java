package com.example.bottomnav.ui.table;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.bottomnav.R;
import com.example.bottomnav.bluetoothlegatt.DeviceControlActivity;
import com.example.bottomnav.bluetoothlegatt.DeviceScanActivity;


import com.example.bottomnav.databinding.FragmentTableBinding;
import com.example.bottomnav.ui.table.CAN_Data;
import com.example.bottomnav.ui.table.TableViewModel;

public class TableFragment extends Fragment {

    private TableViewModel tableViewModel;
    private FragmentTableBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tableViewModel = new ViewModelProvider(this).get(TableViewModel.class);
        binding = FragmentTableBinding.inflate(inflater, container, false);
        final TextView textView = binding.textTable;
        tableViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return binding.getRoot();
    }

    private void displayData(String data) {
        if (data != null) {
            CAN_Data newData = CAN_Data.decode(data);
            String listData = parser.decode(newData.getId(), newData.getData());
            FloatDecoder decoder = (FloatDecoder) test.getDecoder(newData.getId()).get();
            decoder.getVarName();
            decoder.getValueString();
            //display data in textview here
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}