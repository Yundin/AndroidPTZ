package com.yundin.androidptz.onvif.request;

import be.teletask.onvif.models.OnvifType;

public interface OnvifRequest {

    String getXml();

    OnvifType getType();
}
