package com.yundin.androidptz.onvif;

public class DevicePreset {

    private String name;
    private String token;

    public DevicePreset(String name, String token) {
        this.name = name;
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }
}
