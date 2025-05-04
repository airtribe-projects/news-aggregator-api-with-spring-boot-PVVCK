package com.example.newsaggregator.service;


import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.newsaggregator.entity.User;
import com.example.newsaggregator.jwt.JWTUtil;
import com.example.newsaggregator.login.LoginRequest;
import com.example.newsaggregator.login.RegisterRequest;
import com.example.newsaggregator.repo.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {
	
	@Autowired
	private JWTUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Register user
    @Transactional(rollbackOn = Exception.class)
    public void registerUser(RegisterRequest registerRequest) throws BadRequestException {
        // Check if user already exists
        Optional<User> existingUser = userRepository.findByUsername(registerRequest.getUsername());
        if (existingUser.isPresent()) {
            throw new BadRequestException("User already exists with username: " + registerRequest.getUsername());
        }

        // Encrypt password
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());

        // Create and save user
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(encodedPassword);

        userRepository.save(user);
    }

 // Login User
    @Transactional(rollbackOn = Exception.class)
	
    public String loginUser(LoginRequest request) throws BadRequestException {
        User user = userRepository.findByUsername(request.getUsername())
                     .orElseThrow(() -> new BadRequestException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid credentials");
        }

        return jwtUtil.generateToken(user.getUsername());
    }




    // Get current user preferences
    public List<String> getCurrentUserPreferences() {
        // For simplicity, assume the logged-in user is fetched from the security context or session
        String loggedInUsername = "someUser"; // Replace with actual logged-in user context
        User user = userRepository.findByUsername(loggedInUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return (List<String>) user.getPreferences();
    }

    // Update user preferences
    public void updateUserPreferences(List<String> preferences) {
        // For simplicity, assume the logged-in user is fetched from the security context or session
        String loggedInUsername = "someUser"; // Replace with actual logged-in user context
        User user = userRepository.findByUsername(loggedInUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update the preferences and save the user
        user.setPreferences((Set<String>) preferences);
        userRepository.save(user);
    }
}

