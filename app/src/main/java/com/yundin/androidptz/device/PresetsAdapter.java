package com.yundin.androidptz.device;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yundin.androidptz.R;
import com.yundin.androidptz.onvif.DevicePreset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PresetsAdapter extends RecyclerView.Adapter<PresetsAdapter.PresetViewHolder> {

    private final List<DevicePreset> data = new ArrayList<>();
    private OnPresetClickListener listener;

    public PresetsAdapter(OnPresetClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public PresetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_preset, parent, false);
        return new PresetViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PresetViewHolder holder, int position) {
        DevicePreset item = data.get(position);

        holder.name.setText(item.getName());

        holder.itemView.setOnClickListener(v -> listener.onClick(item));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void replacePresets(Collection<DevicePreset> presets) {
        data.clear();
        data.addAll(presets);
        notifyDataSetChanged();
    }

    void addPreset(DevicePreset preset) {
        data.add(preset);
        notifyItemInserted(data.size() - 1);
    }

    class PresetViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        PresetViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
        }
    }

    interface OnPresetClickListener {
        void onClick(DevicePreset preset);
    }
}
