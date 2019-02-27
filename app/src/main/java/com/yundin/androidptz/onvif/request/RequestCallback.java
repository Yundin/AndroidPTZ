package com.yundin.androidptz.onvif.request;

import java.io.IOException;

public interface RequestCallback {

    void onResponse(OnvifRequest request, String body);

    void onFailure(OnvifRequest request, IOException e);
}
