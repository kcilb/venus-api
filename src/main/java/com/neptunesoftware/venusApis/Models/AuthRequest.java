package com.neptunesoftware.venusApis.Models;

import java.io.Serializable;

public class AuthRequest implements Serializable {

    public String username;
    public String password;
    public AuthRequest(){}
    public AuthRequest(String username, String password){
        this.username = username;
        this.password = password;
    }
}
