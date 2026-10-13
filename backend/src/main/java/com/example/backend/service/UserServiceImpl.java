package com.example.backend.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend.dto.LoginRequestDTO;
import com.example.backend.dto.LoginResponseDTO;
import com.example.backend.dto.RegisterRequestDTO;
import com.example.backend.dto.RegisterResponseDTO;
import com.example.backend.entity.Role;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public RegisterResponseDTO register(RegisterRequestDTO request) {

        if (request.getFullName() == null ||
                request.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Full name is required");
        }

        if (request.getEmail() == null ||
                request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (request.getPassword() == null ||
                request.getPassword().length() < 8) {
            throw new IllegalArgumentException(
                    "Password must contain at least 8 characters");
        }

        String email = request.getEmail().trim().toLowerCase();

        if (userRepository.findByEmail(email) != null) {
            throw new IllegalArgumentException(
                    "Email is already registered");
        }

        User user = new User();
        user.setFullName(request.getFullName().trim());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Public registration always creates a CUSTOMER.
        user.setRole(Role.CUSTOMER);

        User savedUser = userRepository.save(user);

        return new RegisterResponseDTO(
                savedUser.getId(),
                savedUser.getFullName(),
                savedUser.getEmail(),
                "Registration successful");
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {

        if (request.getEmail() == null ||
                request.getEmail().trim().isEmpty() ||
                request.getPassword() == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid email or password");
        }

        String email = request.getEmail().trim().toLowerCase();

        User user = userRepository.findByEmail(email);

        if (user == null ||
                !passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword())) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid email or password");
        }

        return new LoginResponseDTO(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                "Login successful");
    }
}
