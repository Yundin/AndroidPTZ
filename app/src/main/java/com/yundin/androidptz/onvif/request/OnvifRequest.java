package com.yundin.androidptz.onvif.request;

public interface OnvifRequest {

    String getXml();

    default RequestType getType() {
        return RequestType.SERVICE;
    }
}
