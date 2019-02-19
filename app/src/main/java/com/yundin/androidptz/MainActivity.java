package com.yundin.androidptz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.rvirin.onvif.onvifcamera.OnvifDevice;
import com.rvirin.onvif.onvifcamera.OnvifListener;
import com.rvirin.onvif.onvifcamera.OnvifRequest;
import com.rvirin.onvif.onvifcamera.OnvifResponse;

import static com.rvirin.onvif.onvifcamera.OnvifDeviceKt.currentDevice;

public class MainActivity extends AppCompatActivity implements OnvifListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentDevice = new OnvifDevice("IP_ADDRESS:PORT", "login", "pwd");
        currentDevice.setListener(this);
        currentDevice.getServices();
    }

    @Override
    public void requestPerformed(@NonNull OnvifResponse onvifResponse) {
        String request = "Request " + onvifResponse.getRequest().getType() + " performed.";
        Log.d("ONVIF", request);
        String succeeded = "Succeeded: " + onvifResponse.getSuccess() + ", message: " + onvifResponse.getParsingUIMessage();
        Log.d("ONVIF",succeeded);

        if (onvifResponse.getRequest().getType() == OnvifRequest.Type.GetServices) {

            currentDevice.getDeviceInformation();
        } else if (onvifResponse.getRequest().getType() == OnvifRequest.Type.GetDeviceInformation) {

            currentDevice.getProfiles();
        } else if (onvifResponse.getRequest().getType() == OnvifRequest.Type.GetProfiles) {

            currentDevice.getStreamURI();
        } else if (onvifResponse.getRequest().getType() == OnvifRequest.Type.GetStreamURI) {

            Log.d("ONVIF", "Stream URI retrieved: " + currentDevice.getRtspURI());
        }
    }
}
