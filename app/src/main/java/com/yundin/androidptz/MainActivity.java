package com.yundin.androidptz;

import android.os.Bundle;

import com.yundin.androidptz.model.ContinuousMoveRequest;

import org.jetbrains.annotations.NotNull;

import androidx.appcompat.app.AppCompatActivity;
import be.teletask.onvif.OnvifManager;
import be.teletask.onvif.listeners.OnvifResponseListener;
import be.teletask.onvif.models.OnvifDevice;
import be.teletask.onvif.responses.OnvifResponse;
import io.github.controlwear.virtual.joystick.android.JoystickView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OnvifManager onvifManager = new OnvifManager();
        onvifManager.setOnvifResponseListener(new OnvifResponseListener() {
            @Override
            public void onResponse(@NotNull OnvifDevice onvifDevice, @NotNull OnvifResponse response) {
                int i = 42;
            }

            @Override
            public void onError(@NotNull OnvifDevice onvifDevice, int errorCode, String errorMessage) {
                int i = 42;
            }
        });
        OnvifDevice device = new OnvifDevice("192.168.15.43");
        device.setUsername("vladislavyundin");
        device.setPassword("MSQBdvrTOtZhWDNw");

        JoystickView v = findViewById(R.id.joystick);
        v.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                float x = (float) Math.cos(Math.toRadians(angle)) * strength / 100f;
                float y = (float) Math.sin(Math.toRadians(angle)) * strength / 100f;
                onvifManager.sendOnvifRequest(device, new ContinuousMoveRequest(x, y, 0));
            }
        }, 500);
    }
}
