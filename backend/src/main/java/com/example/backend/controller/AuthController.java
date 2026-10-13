package com.example.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.LoginRequestDTO;
import com.example.backend.dto.LoginResponseDTO;
import com.example.backend.dto.RegisterRequestDTO;
import com.example.backend.dto.RegisterResponseDTO;
import com.example.backend.service.UserService;
import com.example.backend.service.UserServiceImpl;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
         

private final UserService userService;

public AuthController(UserService userService) {
    this.userService = userService;
}

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public RegisterResponseDTO register ( @RequestBody RegisterRequestDTO request) {
		return userService.register(request);
	}
	
	@PostMapping("/login")
	public LoginResponseDTO login(@RequestBody LoginRequestDTO request) {
		return userService.login(request);
	}
	
}
