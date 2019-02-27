package com.yundin.androidptz;

import java.io.Serializable;

public class SpOnvifDevice implements Serializable {
    public String login;
    public String password;
    public String address;

    public SpOnvifDevice(String login, String password, String address){
        this.login = login;
        this.password = password;
        this.address = address;
    }
}
