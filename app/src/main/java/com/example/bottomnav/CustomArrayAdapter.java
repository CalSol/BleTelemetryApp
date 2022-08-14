package com.example.bottomnav;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CustomArrayAdapter extends RecyclerView.Adapter<CustomArrayAdapter.CustomViewHolder> {
    List<CAN_Data> data = new ArrayList<>();
    int custom_layout_id;
    public CustomArrayAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        data = objects;
        custom_layout_id = resource;
    }


    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_device, parent, false);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.getTextView().setText(data.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        public CustomViewHolder(View view){
            super(view);
            textView = (TextView) view.findViewById(R.id.list_item);
        }
        public TextView getTextView() {
            return textView;
        }
    }
}
