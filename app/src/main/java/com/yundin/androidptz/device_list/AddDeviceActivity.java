package com.yundin.androidptz.device_list;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.yundin.androidptz.R;
import com.yundin.androidptz.model.SpOnvifDevice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class AddDeviceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        EditText nameEditText = findViewById(R.id.name);
        EditText addressEditText = findViewById(R.id.address);
        EditText loginEditText = findViewById(R.id.login);
        EditText passwordEditText = findViewById(R.id.password);
        Button addButton = findViewById(R.id.add_button);

        SharedPreferences sp = getSharedPreferences("authData", MODE_PRIVATE);
        SharedPreferences.Editor spEditor = sp.edit();

        String login = sp.getString("login", "");
        String password = sp.getString("password", "");
        if(!login.equals("")){
            loginEditText.setText(login);
        }
        if(!password.equals("")){
            passwordEditText.setText(password);
        }

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = loginEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String name = nameEditText.getText().toString();
                String address = addressEditText.getText().toString();

                saveData(login, password, name, address);

                finish();
            }
        });
    }

    private void saveData(String login, String password, String name, String address){
        SharedPreferences sp = getSharedPreferences("deviceData", MODE_PRIVATE);
        SharedPreferences.Editor spEditor = sp.edit();

        List<SpOnvifDevice> deviceList = getDevicesFromSP(sp);
        if(!deviceList.isEmpty()){
            deviceList.add(new SpOnvifDevice(login, password, name, address));
            setDevicesToSp(spEditor, deviceList);
        }else{
            deviceList.add(new SpOnvifDevice(login, password, name, address));
            setDevicesToSp(spEditor, deviceList);
        }
    }

    private List<SpOnvifDevice> getDevicesFromSP(SharedPreferences sp){
        String devices = sp.getString("devices", "");
        if(devices.equals("")) return new ArrayList<SpOnvifDevice>();
        Gson gson = new Gson();
        SpOnvifDevice deviceArray[] = gson.fromJson(devices, SpOnvifDevice[].class);
        return new ArrayList<SpOnvifDevice>(Arrays.asList(deviceArray));
    }

    private void setDevicesToSp(SharedPreferences.Editor spEditor, List<SpOnvifDevice> deviceList){
        Gson gson = new Gson();
        String deviceListJsonString = gson.toJson(deviceList);
        spEditor.putString("devices", deviceListJsonString);
        spEditor.apply();
    }
}
