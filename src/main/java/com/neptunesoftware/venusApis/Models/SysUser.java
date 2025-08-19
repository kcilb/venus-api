package com.neptunesoftware.venusApis.Models;

public class SysUser {
    public String loginName;
    public String username;
    public String password;
    public String role;
    public SysUser() {}
    public SysUser(String username, String password, String role, String loginName) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.loginName = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public void setRole(String role) {
        this.role = role;
    }

    public void setUsername(String username) {}
    public String getUsername() {return username;}

    public String getPassword() {return password;}
    public String getRole() {return role;}

    public String getLoginName() {return loginName;}
    public void setLoginName(String loginName) {this.loginName = loginName;}

}
