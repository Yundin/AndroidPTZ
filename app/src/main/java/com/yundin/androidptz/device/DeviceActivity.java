package com.yundin.androidptz.device;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.google.gson.Gson;
import com.yundin.androidptz.R;
import com.yundin.androidptz.model.SpOnvifDevice;
import com.yundin.androidptz.onvif.DevicePreset;
import com.yundin.androidptz.onvif.OnvifDevice;
import com.yundin.androidptz.onvif.OnvifExecutor;
import com.yundin.androidptz.onvif.request.ContinuousMoveRequest;
import com.yundin.androidptz.onvif.request.GetCapabilitiesRequest;
import com.yundin.androidptz.onvif.request.GetPresetsRequest;
import com.yundin.androidptz.onvif.request.GetProfilesRequest;
import com.yundin.androidptz.onvif.request.OnvifRequest;
import com.yundin.androidptz.onvif.request.RequestCallback;
import com.yundin.androidptz.utils.StartPointSeekBar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import io.github.controlwear.virtual.joystick.android.JoystickView;
import okhttp3.logging.HttpLoggingInterceptor;


public class DeviceActivity extends AppCompatActivity {

    private OnvifDevice device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OnvifExecutor.loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        Intent intent = getIntent();
        SpOnvifDevice spOnvifDevice = (SpOnvifDevice) intent.getSerializableExtra("device");

        device = new OnvifDevice(spOnvifDevice.address, spOnvifDevice.login, spOnvifDevice.password);
        OnvifExecutor.getDeviceCapabilities(device);
        OnvifExecutor.requestCallback = new RequestCallback() {
            @Override
            public void onResponse(OnvifRequest request, String body) {
                if (request instanceof GetCapabilitiesRequest) {
                    OnvifExecutor.getProfiles(device);
                } else if (request instanceof GetProfilesRequest) {
                    OnvifExecutor.getPresets(device);
                } else if (request instanceof GetPresetsRequest) {
                    ArrayList<DevicePreset> presets = parsePresets(body);
                }
            }

            @Override
            public void onFailure(OnvifRequest request, IOException e) {

            }
        };

        configureJoystick();
        configureSeekBar();
    }

    private ArrayList<DevicePreset> parsePresets(String responseBody) {
        ArrayList<DevicePreset> presets = new ArrayList<>();
        try {

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(new StringReader(responseBody));
            int eventType = xpp.getEventType();


            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG && xpp.getName().equalsIgnoreCase("preset")) {
                    String token = xpp.getAttributeValue(null, "token");

                    xpp.next();
                    xpp.next();

                    String name = xpp.getText();

                    presets.add(new DevicePreset(name, token));
                }
                eventType = xpp.next();
            }
        } catch (Exception e) { /**/ }
        return presets;
    }

    private void configureJoystick() {
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
    }

    @SuppressLint("ClickableViewAccessibility")
    private void configureSeekBar() {
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
