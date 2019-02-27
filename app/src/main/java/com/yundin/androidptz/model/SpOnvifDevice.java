package com.yundin.androidptz.model;

import java.io.Serializable;

import androidx.annotation.Nullable;

public class SpOnvifDevice implements Serializable {
    public String login;
    public String password;
    public String address;
    public String name;

    public SpOnvifDevice(String login, String password,String name, String address){
        this.login = login;
        this.password = password;
        this.address = address;
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof SpOnvifDevice)) return false;
        SpOnvifDevice device = (SpOnvifDevice) obj;
        if(login.equals(device.login) &&
                password.equals(device.password) &&
                address.equals(device.address) &&
                name.equals(device.name)) return true;
        return super.equals(device);
    }
}
