package com.yundin.androidptz.device_list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yundin.androidptz.R;
import com.yundin.androidptz.model.SpOnvifDevice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.DeviceListViewHolder> {

    private List<SpOnvifDevice> data = new ArrayList<>();
    private DeviceClickListener onItemClicklistener;
    private DeleteClickListener onDeleteClicklistener;

    public DeviceListAdapter(DeviceClickListener listener, DeleteClickListener deleteClickListener) {
        this.onItemClicklistener = listener;
        this.onDeleteClicklistener = deleteClickListener;
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

        holder.name.setText(item.name);
        holder.address.setText(item.address);

        holder.itemView.setOnClickListener(v -> onItemClicklistener.onClick(item));
        holder.deleteButton.setOnClickListener(v -> onDeleteClicklistener.onClick(item));
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
        Button deleteButton;

        public DeviceListViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }

    interface DeviceClickListener {
        void onClick(SpOnvifDevice device);
    }

    interface DeleteClickListener{
        void onClick(SpOnvifDevice device);
    }
}
