package com.inf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inf.dto.AdminProfileDTO;
import com.inf.dto.ResidentProfileDTO;
import com.inf.service.UserService;

@RestController
@RequestMapping("auth/profile")
@CrossOrigin(origins = {"http://localhost:3000"}) // Ensure CORS allows requests from frontend
public class ProfileController {

    @Autowired
    private UserService userService;

    // Complete admin profile
    @PostMapping("/complete/admin/{userId}")
    public String completeAdminProfile(@PathVariable Long userId, @RequestBody AdminProfileDTO adminProfileDTO) {
        return userService.completeFirstLoginAdmin(userId, adminProfileDTO);
    }

    // Complete resident profile (note: 'userId' should be in request body or extracted from token)
    @PostMapping("/complete/resident")
    public String completeResidentProfile(@RequestBody ResidentProfileDTO residentProfileDTO) {
        // In case 'userId' is not passed in path, retrieve from token or session
        Long userId = 1L; // For demo purposes, use a dummy value or implement JWT extraction
        return userService.completeFirstLoginResident(userId, residentProfileDTO);
    }
}
