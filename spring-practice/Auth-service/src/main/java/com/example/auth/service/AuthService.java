package com.example.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.auth.dto.AuthRequest;
import com.example.auth.dto.RegisterRequest;
import com.example.auth.entity.User;
import com.example.auth.repo.UserRepo;

@Service
public class AuthService {
	@Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private UserRepo repo;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public String authLogin(AuthRequest request) {
    	   Authentication authentication =
	                authenticationManager.authenticate(

	                        new UsernamePasswordAuthenticationToken(
	                                request.getUsername(),
	                                request.getPassword()));

	        if(authentication.isAuthenticated()) {

	            return jwtService.generateToken(
	                    request.getUsername());
	        }

	        throw new RuntimeException("Invalid Credentials");
    }
    public User register(RegisterRequest request) {

        if (repo.existsByPassword(passwordEncoder.encode(request.getPassword()))) {
            throw new RuntimeException("Username already exists.");
        }

        User user = new User();

        user.setUsername(request.getUsername());

        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        user.setRole(request.getRole());

        return repo.save(user);
        
        

       
    }

	

}
