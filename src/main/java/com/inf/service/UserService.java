package com.inf.service;

import com.inf.model.AdminProfile;
import com.inf.model.ResidentProfile;
import com.inf.model.User;
import com.inf.repository.AdminProfileRepository;
import com.inf.repository.ResidentProfileRepository;
import com.inf.repository.UserRepository;
import com.inf.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private AdminProfileRepository adminProfileRepository;
    
    @Autowired
    private ResidentProfileRepository residentProfileRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    // Register a new user
    public String registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username is already taken.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        user.setFirstLogin(true);  // Mark as first login
        userRepository.save(user);
        return "User registered successfully.";
    }

    // Authenticate the user and return JWT
    public String authenticateUser(String username, String password) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            return jwtUtils.generateToken(username, user.getRole());
        } catch (Exception e) {
            System.out.println("Error during authentication: " + e.getMessage()); // Add more logging here
            throw new RuntimeException("Invalid username or password.");
        }
    }

    // Update user profile (for first login setup)
    public String updateUserProfile(Long userId, User updatedUser) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Set updated fields (assuming specific fields are being updated)
        user.setFirstLogin(false);  // User has completed their first login setup
        user.setUsername(updatedUser.getUsername());
        user.setRole(updatedUser.getRole());
        // Add any additional fields to update here

        userRepository.save(user);
        return "User profile updated successfully.";
    }

    // Load user by username for authentication
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Don't add "ROLE_" prefix here; Spring Security will handle it
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole()) // Provide role without prefix
                .build();
    }
    
    public String completeFirstLogin(Long userId, Object profileData) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        if (user.isFirstLogin()) {
            if ("ADMIN".equals(user.getRole())) {
                AdminProfile adminProfile = (AdminProfile) profileData;
                adminProfile.setUser(user);
             adminProfileRepository.save(adminProfile);
            } else if ("RESIDENT".equals(user.getRole())) {
                ResidentProfile residentProfile = (ResidentProfile) profileData;
                residentProfile.setUser(user);
                residentProfileRepository.save(residentProfile);
            }
            user.setFirstLogin(false);
            userRepository.save(user);
            return "Profile completed successfully.";
        }
        return "User has already completed the first login setup.";
    }
}



