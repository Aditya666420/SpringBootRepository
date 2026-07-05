package com.example.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.auth.dto.AuthRequest;
import com.example.auth.dto.RegisterRequest;
import com.example.auth.entity.User;
import com.example.auth.service.AuthService;

@RestController
	@RequestMapping("/auth")
	public class AuthController {
	
	@Autowired
	private AuthService service;

	   
	    @GetMapping("/login")
	    public ResponseEntity<String> login(@RequestBody AuthRequest req){
	    	return ResponseEntity.ok(service.authLogin(req));
	    	
	    	
	    }
	    @PostMapping("/register")
	    public ResponseEntity<User> register(@RequestBody RegisterRequest reg){
	    	return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.register(reg));
	    	
	    }
	}

