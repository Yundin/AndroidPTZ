package com.yundin.androidptz;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.jackandphantom.joystickview.JoyStickView;
import com.rvirin.onvif.onvifcamera.OnvifDigestInformation;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        JoyStickView v = findViewById(R.id.joystick);
        v.setOnMoveListener(new JoyStickView.OnMoveListener() {
            @Override
            public void onMove(double angle, float strength) {
                int i =  42;
            }
        });

        OnvifDigestInformation di = new OnvifDigestInformation("vladislavyundin", "MSQBdvrTOtZhWDNw", "/onvif/device_service", "Digest qop=\"auth\", realm=\"DS-2DC2204IW-DE3/W\", nonce=\"4d6b52424e7a597a4d5551364d54426a4e7a55344e6a633d\"");
        String ah = di.getAuthorizationHeader();
    }
}
