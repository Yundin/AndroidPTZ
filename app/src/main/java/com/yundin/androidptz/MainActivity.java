package com.yundin.androidptz;

import androidx.appcompat.app.AppCompatActivity;
import be.teletask.onvif.DiscoveryManager;
import be.teletask.onvif.OnvifManager;
import be.teletask.onvif.listeners.DiscoveryListener;
import be.teletask.onvif.listeners.OnvifMediaProfilesListener;
import be.teletask.onvif.listeners.OnvifMediaStreamURIListener;
import be.teletask.onvif.listeners.OnvifResponseListener;
import be.teletask.onvif.listeners.OnvifServicesListener;
import be.teletask.onvif.models.Device;
import be.teletask.onvif.models.OnvifDevice;
import be.teletask.onvif.models.OnvifMediaProfile;
import be.teletask.onvif.models.OnvifServices;
import be.teletask.onvif.responses.OnvifResponse;
import io.github.controlwear.virtual.joystick.android.JoystickView;

import android.os.Bundle;
import android.util.Log;

import com.yundin.androidptz.model.ContinuousMoveRequest;

import org.jetbrains.annotations.NotNull;

import java.util.List;

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
