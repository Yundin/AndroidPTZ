package com.yundin.androidptz.onvif;

public class OnvifResponse {

    public int code;
    public String body;
    public String status;

    public OnvifResponse(int code, String body, String status) {
        this.code = code;
        this.body = body;
        this.status = status;
    }
}
