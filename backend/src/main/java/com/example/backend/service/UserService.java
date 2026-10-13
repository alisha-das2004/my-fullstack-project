package com.example.backend.service;

import com.example.backend.dto.LoginRequestDTO;
import com.example.backend.dto.LoginResponseDTO;
import com.example.backend.dto.RegisterRequestDTO;
import com.example.backend.dto.RegisterResponseDTO;

public interface UserService {

    RegisterResponseDTO register(RegisterRequestDTO request);

    LoginResponseDTO login(LoginRequestDTO request);
}
