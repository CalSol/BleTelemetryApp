package com.example.bottomnav.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.bottomnav.R;
import com.example.bottomnav.databinding.FragmentNotificationsBinding;

import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);

        final TextView textView = binding.textNotifications;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
        adapter = new ArrayAdapter<>(getActivity(),
                R.layout.listview_layout, CAN_receiver);
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        return view;
    }

    private void displayData(String data) {
        if (data != null) {
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
                DataDecoder decoder = option.get();
                Optional<String> decoded = decoder.decodeToString(newData.getData());
                if (decoded.isPresent()) {
                    Log.i(TAG, CAN_receiver.toString());
                    for (DataDecoder.Decoded entry : DataDecoder.parseStringResult(decoded.get())) {
                        put(newData, CAN_receiver, entry.getName(), entry.getValue());
                    }
                    sort(CAN_receiver);
                    adapter.notifyDataSetChanged();
                }
                else {
                    Log.i(TAG, "DATA MISMATCH");
                }
            }
        }
    }

    private ArrayList<String> getNameValue(String input) {
        Pattern pattern = Pattern.compile("(\\S+):\\s*(\\S+)");
        Matcher matcher = pattern.matcher(input);
        ArrayList<String> nameValue = new ArrayList<>();
        if (matcher.find()) { // associate ID to struct
            nameValue.add(matcher.group(1));
            nameValue.add(matcher.group(2));
        } return nameValue;
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
        });
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}