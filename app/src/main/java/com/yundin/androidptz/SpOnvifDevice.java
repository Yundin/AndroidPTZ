package com.yundin.androidptz;

import java.io.Serializable;

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
}
