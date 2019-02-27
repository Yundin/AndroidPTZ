package com.yundin.androidptz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.yundin.androidptz.onvif.OnvifDevice;
import com.yundin.androidptz.onvif.OnvifExecutor;
import com.yundin.androidptz.onvif.request.ContinuousMoveRequest;
import com.yundin.androidptz.onvif.request.GetCapabilitiesRequest;
import com.yundin.androidptz.onvif.request.OnvifRequest;
import com.yundin.androidptz.onvif.request.RequestCallback;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import io.github.controlwear.virtual.joystick.android.JoystickView;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;


public class MainActivity extends AppCompatActivity {

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OnvifExecutor.loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        Intent intent = getIntent();
        SpOnvifDevice spOnvifDevice = (SpOnvifDevice) intent.getSerializableExtra("device");

        OnvifDevice device = new OnvifDevice(spOnvifDevice.address, spOnvifDevice.login, spOnvifDevice.password);
        OnvifExecutor.getDeviceCapabilities(device);
        OnvifExecutor.requestCallback = new RequestCallback() {
            @Override
            public void onResponse(OnvifRequest request, Response response) {
                if (request instanceof GetCapabilitiesRequest) {
                    OnvifExecutor.getProfiles(device);
                }
            }

            @Override
            public void onFailure(OnvifRequest request, IOException e) {

            }
        };

        JoystickView v = findViewById(R.id.joystick);
        v.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                float x = (float) Math.cos(Math.toRadians(angle)) * strength / 100f;
                float y = (float) Math.sin(Math.toRadians(angle)) * strength / 100f;
                if (!device.profiles.isEmpty()) {
                    OnvifExecutor.sendRequest(device, new ContinuousMoveRequest(x, y, 0, device.profiles.get(0)));
                }
            }
        }, 500);


        final StartPointSeekBar seekBar = findViewById(R.id.seek_bar);
        seekBar.setProgress(0);
        seekBar.setOnSeekBarChangeListener(new StartPointSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onOnSeekBarValueChange(StartPointSeekBar bar, double value) {
                if (!device.profiles.isEmpty()) {
                    OnvifExecutor.sendRequest(device, new ContinuousMoveRequest(0, 0, (float) value, device.profiles.get(0)));
                }
            }
        });
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    seekBar.setProgress(0);
                    return true;
                }

                return false;
            }
        });
    }
}
