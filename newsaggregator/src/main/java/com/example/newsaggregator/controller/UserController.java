package com.example.newsaggregator.controller;

import java.time.LocalDateTime;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.newsaggregator.login.LoginRequest;
import com.example.newsaggregator.login.RegisterRequest;
import com.example.newsaggregator.response.APIResponse;
import com.example.newsaggregator.service.UserService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/user")
public class UserController {

	    @Autowired
	    private UserService userService;

	    // Register a new user
	    @PostMapping("/register")
	    public ResponseEntity<APIResponse> register(@Valid @RequestBody RegisterRequest registerRequest) throws BadRequestException {
	        userService.registerUser(registerRequest);
	        
	        APIResponse apiResponse = new APIResponse();
    		apiResponse.setSuccess(true);	
    		apiResponse.setTimestamp(LocalDateTime.now());
    		apiResponse.setData("User registered successfully");
    		apiResponse.setErrorMessage(null);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
//	        return ResponseEntity.ok();
	    }

	    @PostMapping("/login")
	    public ResponseEntity<APIResponse> login(@Valid @RequestBody LoginRequest request) throws BadRequestException {
	        String token = userService.loginUser(request);
	        
	        APIResponse apiResponse = new APIResponse();
    		apiResponse.setSuccess(true);	
    		apiResponse.setTimestamp(LocalDateTime.now());
    		apiResponse.setData(token);
    		apiResponse.setErrorMessage(null);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
//	        return ResponseEntity.ok(token);
	    }
}
