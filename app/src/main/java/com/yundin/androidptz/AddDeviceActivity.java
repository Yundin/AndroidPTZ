package com.yundin.androidptz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddDeviceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

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
                String address = addressEditText.getText().toString();

                SharedPreferences sp = getSharedPreferences("deviceData", MODE_PRIVATE);
                SharedPreferences.Editor spEditor = sp.edit();

                String devices = sp.getString("devices", "");
                if(!devices.equals("")){
                    Gson gson = new Gson();
                    SpOnvifDevice deviceArray[] = gson.fromJson(devices, SpOnvifDevice[].class);
                    List<SpOnvifDevice> deviceList = new ArrayList<SpOnvifDevice>(Arrays.asList(deviceArray));
                    deviceList.add(new SpOnvifDevice(login, password, address));
                    String deviceListJsonString = gson.toJson(deviceList);
                    spEditor.putString("devices", deviceListJsonString);
                }else{
                    List<SpOnvifDevice> deviceList = new ArrayList<SpOnvifDevice>();
                    deviceList.add(new SpOnvifDevice(login, password, address));
                    Gson gson = new Gson();
                    String deviceListJsonString = gson.toJson(deviceList);
                    spEditor.putString("devices", deviceListJsonString);
                }
                spEditor.apply();
                finish();
            }
        });
    }
}
