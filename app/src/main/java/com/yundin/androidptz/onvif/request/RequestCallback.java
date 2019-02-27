package com.yundin.androidptz.onvif.request;

import com.yundin.androidptz.onvif.OnvifResponse;

import java.io.IOException;

public interface RequestCallback {

    void onResponse(OnvifRequest request, OnvifResponse response);

    void onFailure(OnvifRequest request, IOException e);
}
