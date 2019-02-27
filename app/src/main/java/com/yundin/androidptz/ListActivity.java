package com.yundin.androidptz;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
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
        adapter = new DeviceListAdapter(name -> {
            // TODO
        });
        recyclerView.setAdapter(adapter);

        sp = getSharedPreferences("deviceData", MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String devices = sp.getString("devices", "");
        List<SpOnvifDevice> deviceList = gson.fromJson(devices, new TypeToken<List<SpOnvifDevice>>(){}.getType());

        adapter.replaceDevices(deviceList);
    }
}
