package com.inf.controller;

import com.inf.model.LoginResponse;
import com.inf.model.User;
import com.inf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {"http://localhost:3000"})
public class AuthController {

    @Autowired
    private UserService userService;

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            String response = userService.registerUser(user);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Authenticate the user and return JWT
//    @PostMapping("/login")
//    public ResponseEntity<?> authenticateUser(@RequestBody User loginRequest) {
//        try {
//            String token = userService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());
//            return ResponseEntity.ok(token);
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(401).body(e.getMessage());
//        }
//    }
    
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody User loginRequest) {
        LoginResponse response = userService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());
        return ResponseEntity.ok(response);
    }
    
    
}
