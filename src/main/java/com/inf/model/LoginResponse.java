package com.inf.model;

//LoginResponse.java

public class LoginResponse {
 private String token;
 private String role;
 private boolean firstLogin;

 public LoginResponse(String token, String role, boolean firstLogin) {
     this.token = token;
     this.role = role;
     this.firstLogin = firstLogin;
 }

 // Getters and Setters
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
}
