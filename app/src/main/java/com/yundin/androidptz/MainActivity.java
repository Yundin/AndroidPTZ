package com.yundin.androidptz;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.yundin.androidptz.onvif.OnvifDevice;
import com.yundin.androidptz.onvif.OnvifExecutor;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final StartPointSeekBar seekBar = (StartPointSeekBar) findViewById(R.id.seek_bar);
        seekBar.setOnSeekBarChangeListener(new StartPointSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onOnSeekBarValueChange(StartPointSeekBar bar, double value) {
                Log.d("LOGTA1", "seekbar value:" + value);
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
//        OnvifManager onvifManager = new OnvifManager();
//        onvifManager.setOnvifResponseListener(new OnvifResponseListener() {
//            @Override
//            public void onResponse(@NotNull OnvifDevice onvifDevice, @NotNull OnvifResponse response) {
//                int i = 42;
//            }
//
//            @Override
//            public void onError(@NotNull OnvifDevice onvifDevice, int errorCode, String errorMessage) {
//                int i = 42;
//            }
//        });
//        OnvifDevice device = new OnvifDevice("192.168.15.54:8080");
//        device.setUsername("vladislavyundin");
//        device.setPassword("MSQBdvrTOtZhWDNw");
//        device.setUsername("student");
//        device.setPassword("student");
        OnvifDevice device = new OnvifDevice("http://192.168.15.43:80", "admin", "Supervisor");
        OnvifExecutor.getDeviceCapabilities(device);
        OnvifExecutor.getProfiles(device);
//        OnvifExecutor.sendRequest(device, new ContinuousMoveRequest(0, 0, 0, "protoken_1"));


//        JoystickView v = findViewById(R.id.joystick);
//        v.setOnMoveListener(new JoystickView.OnMoveListener() {
//            @Override
//            public void onMove(int angle, int strength) {
//                float x = (float) Math.cos(Math.toRadians(angle)) * strength / 100f;
//                float y = (float) Math.sin(Math.toRadians(angle)) * strength / 100f;
//                onvifManager.sendOnvifRequest(device, new ContinuousMoveRequest(x, y, 0));
//            }
//        }, 500);
//
//        // force logging
//        Field f;
//        try {
//            f = onvifManager.getClass().getDeclaredField("executor");
//            f.setAccessible(true);
//            OnvifExecutor executor = (OnvifExecutor) f.get(onvifManager);
//
//            f = executor.getClass().getDeclaredField("credentials");
//            f.setAccessible(true);
//            Credentials credentials = (Credentials) f.get(executor);
//
//            DigestAuthenticator authenticator = new DigestAuthenticator(credentials);
//            Map<String, CachingAuthenticator> authCache = new ConcurrentHashMap<>();
//            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//
//            OkHttpClient client = new OkHttpClient.Builder()
//                    .connectTimeout(10000, TimeUnit.SECONDS)
//                    .writeTimeout(100, TimeUnit.SECONDS)
//                    .readTimeout(10000, TimeUnit.SECONDS)
//                    .authenticator(new CachingAuthenticatorDecorator(authenticator, authCache))
//                    .addInterceptor(new AuthenticationCacheInterceptor(authCache))
//                    .addInterceptor(logging)
//                    .build();
//
//            f = executor.getClass().getDeclaredField("client");
//            f.setAccessible(true);
//            f.set(executor, client);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        onvifManager.getMediaProfiles(device, new OnvifMediaProfilesListener() {
//            @Override
//            public void onMediaProfilesReceived(OnvifDevice device, List<OnvifMediaProfile> mediaProfiles) {
//                onvifManager.getMediaStreamURI(device, mediaProfiles.get(0), new OnvifMediaStreamURIListener() {
//                    @Override
//                    public void onMediaStreamURIReceived(OnvifDevice device, OnvifMediaProfile profile, String uri) {
//                        int i = 42;
//                    }
//                });
//            }
//        });
    }
}
