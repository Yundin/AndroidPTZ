package com.yundin.androidptz.device_list;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

        String login = sp.getString("login", "");
        String password = sp.getString("password", "");
        if(!login.equals("")){
            loginEditText.setText(login);
        }
        if(!password.equals("")){
            passwordEditText.setText(password);
        }

        loginEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                loginEditText.setSelection(loginEditText.getText().length());
            }
        });
        passwordEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                passwordEditText.setSelection(passwordEditText.getText().length());
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = loginEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String name = nameEditText.getText().toString();
                String address = addressEditText.getText().toString();
                if(name.equals("")) name = address;
                if(address.matches("http(s)?://([\\w-]+\\.)+[\\w-]+(:\\d+)?(/[\\w- ;,./?%&=]*)?")){
                    saveData(login, password, name, address);
                    finish();
                } else {
                    addressEditText.setError("Введите адрес в формате http(s)://ip:port");
                }

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
        SpOnvifDevice[] deviceArray = gson.fromJson(devices, SpOnvifDevice[].class);
        return new ArrayList<SpOnvifDevice>(Arrays.asList(deviceArray));
    }

    private void setDevicesToSp(SharedPreferences.Editor spEditor, List<SpOnvifDevice> deviceList){
        Gson gson = new Gson();
        String deviceListJsonString = gson.toJson(deviceList);
        spEditor.putString("devices", deviceListJsonString);
        spEditor.apply();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains(Math.round(ev.getRawX()), Math.round(ev.getRawY()))) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
