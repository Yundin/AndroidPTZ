package com.yundin.androidptz.device_list;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yundin.androidptz.R;
import com.yundin.androidptz.device.DeviceActivity;
import com.yundin.androidptz.model.SpOnvifDevice;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

public class ListActivity extends AppCompatActivity {

    private SharedPreferences sp;
    private Gson gson = new Gson();
    private DeviceListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        RecyclerView recyclerView = findViewById(R.id.recycler);
        adapter = new DeviceListAdapter(device -> {
            Intent intent = new Intent(getApplicationContext(), DeviceActivity.class);
            intent.putExtra("device", device);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        sp = getSharedPreferences("deviceData", MODE_PRIVATE);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AddDeviceActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        adapter.replaceDevices(getDevices());
    }

    private ArrayList<SpOnvifDevice> getDevices() {
        String devices = sp.getString("devices", "");
        ArrayList<SpOnvifDevice> deviceList = gson.fromJson(devices, new TypeToken<ArrayList<SpOnvifDevice>>(){}.getType());
        if (deviceList != null) {
            return deviceList;
        } else {
            return new ArrayList<>();
        }
    }
}
