package com.example.backend.controller;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
		LoginResponseDTO  result =  userService.login(request);
		
		ResponseCookie cookie = ResponseCookie.from("accessToken", result.getToken())
				.httpOnly(true)
				.secure(false)
				.path("/")
				.maxAge(15*60)
				.sameSite("Lax")
				.build();
		
		result.setToken(null);
		
		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, cookie.toString())
				.body(result);
		
	}
	@PostMapping("/logout")
	public ResponseEntity<String> logout(){
		ResponseCookie cookie = ResponseCookie.from("accessToken","")
				.httpOnly(true)
				.secure(false)
				.path("/")
				.maxAge(0)
				.sameSite("Lax")
				.build();
		return ResponseEntity.ok()
				.header("Set-Cookie", cookie.toString())
				.body("Logout Successful");
	}
	
}
