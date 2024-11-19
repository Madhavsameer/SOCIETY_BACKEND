package com.inf.model;

//LoginResponse.java

public class LoginResponse {
 private String token;
 private String role;
 private boolean firstLogin;
 private  Long id;

    public LoginResponse(String token, String role, boolean firstLogin, Long id) {
        this.token = token;
        this.role = role;
        this.firstLogin = firstLogin;
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        this.firstLogin = firstLogin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
