package com.yundin.androidptz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.DeviceListViewHolder> {

    private List<SpOnvifDevice> data = new ArrayList<>();
    private DeviceClickListener listener;

    public DeviceListAdapter(DeviceClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public DeviceListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device, parent, false);
        return new DeviceListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceListViewHolder holder, int position) {
        SpOnvifDevice item = data.get(position);

        holder.name.setText(item.address);
        holder.address.setText(item.address);

        holder.itemView.setOnClickListener(v -> listener.onClick(item.address));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void replaceDevices(Collection<SpOnvifDevice> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    class DeviceListViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView address;

        public DeviceListViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
        }
    }

    interface DeviceClickListener {
        void onClick(String name);
    }
}
