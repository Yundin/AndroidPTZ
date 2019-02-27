package com.yundin.androidptz.onvif.request;

import java.io.IOException;

import okhttp3.Response;

public interface RequestCallback {

    void onResponse(OnvifRequest request, Response response);

    void onFailure(OnvifRequest request, IOException e);
}
