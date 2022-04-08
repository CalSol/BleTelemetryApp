package com.example.bottomnav.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.bottomnav.R;
import com.example.bottomnav.databinding.FragmentNotificationsBinding;

import org.w3c.dom.Text;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private FragmentNotificationsBinding binding;

    //Array of strings
    String[] CAN_receiver = new String[]{"petals", "bms", "dashboard", "petals", "petals",
            "dashboard", "bms", "dashboard", "lights", "petals", "bms"};

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] CAN_receiver = new String[]{"petals", "bms", "dashboard", "petals", "petals",
                "dashboard", "bms", "dashboard", "lights", "petals", "bms"};

        // Creates an Adapter that adapts array CAN_receiver to display
        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.activity_listview, CAN_receiver);

        // A listView is created and adapted
        ListView listView = view.findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}