package com.inf.service;

import com.inf.dto.AdminProfileDTO;
import com.inf.dto.ResidentProfileDTO;
import com.inf.model.AdminProfile;
import com.inf.model.LoginResponse;
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
        // Set role based on the input, assuming the frontend provides "ADMIN" or "RESIDENT"
        if (user.getRole() == null || (!user.getRole().equals("ADMIN") && !user.getRole().equals("RESIDENT"))) {
            throw new RuntimeException("Invalid role specified.");
        }
        user.setFirstLogin(true);  // Mark as first login
        userRepository.save(user);
        return "User registered successfully.";
    }

    // Authenticate the user and return JWT

    public LoginResponse authenticateUser(String username, String password) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String token = jwtUtils.generateToken(username, user.getRole());
            return new LoginResponse(token, user.getRole(), user.isFirstLogin());
        } catch (Exception e) {
            System.out.println("Error during authentication: " + e.getMessage());
            throw new RuntimeException("Invalid username or password.");
        }
    }

    // Update user profile (for first login setup)
    public String updateUserProfile(Long userId, User updatedUser) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setFirstLogin(false);  // User has completed their first login setup
        user.setUsername(updatedUser.getUsername());
        user.setRole(updatedUser.getRole());
        userRepository.save(user);
        return "User profile updated successfully.";
    }

    // Load user by username for authentication
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole()) // Provide role without prefix
                .build();
    }
    
    // Complete first login setup by creating specific profiles based on the role

    public String completeFirstLoginAdmin(Long userId, AdminProfileDTO profileData) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        
        if (user.isFirstLogin() && "ADMIN".equals(user.getRole())) {
            AdminProfile adminProfile = new AdminProfile();
            adminProfile.setName(profileData.getName());
            adminProfile.setPhoneNumber(profileData.getPhoneNumber());
            adminProfile.setSocietyName(profileData.getSocietyName());
            adminProfile.setSocietyAddress(profileData.getSocietyAddress());
            adminProfile.setCity(profileData.getCity());
            adminProfile.setDistrict(profileData.getDistrict());
            adminProfile.setPostalCode(profileData.getPostalCode());
            adminProfile.setUser(user);

            adminProfileRepository.save(adminProfile);
            user.setFirstLogin(false);
            userRepository.save(user);

            return "Admin profile completed successfully.";
        }
        throw new RuntimeException("Invalid request or user role.");
    }

    public String completeFirstLoginResident(Long userId, ResidentProfileDTO profileData) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isFirstLogin() && "RESIDENT".equals(user.getRole())) {
            ResidentProfile residentProfile = new ResidentProfile();
            residentProfile.setName(profileData.getName());
            residentProfile.setPhoneNumber(profileData.getPhoneNumber());
            residentProfile.setSocietyName(profileData.getSocietyName());
            residentProfile.setFlatNumber(profileData.getFlatNumber());
            residentProfile.setPostalCode(profileData.getPostalCode());
            residentProfile.setUser(user);

            residentProfileRepository.save(residentProfile);
            user.setFirstLogin(false);
            userRepository.save(user);

            return "Resident profile completed successfully.";
        }
        throw new RuntimeException("Invalid request or user role.");
    }

}
