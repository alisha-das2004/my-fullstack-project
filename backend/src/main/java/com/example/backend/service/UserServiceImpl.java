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
import com.example.backend.security.JwtService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public RegisterResponseDTO register(RegisterRequestDTO request) {

        // Check whether the request body exists.
        if (request == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Registration details are required");
        }

        // Validate full name.
        if (request.getFullName() == null ||
                request.getFullName().trim().isEmpty()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Full name is required");
        }

        // Validate email.
        if (request.getEmail() == null ||
                request.getEmail().trim().isEmpty()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email is required");
        }

        // Validate password.
        if (request.getPassword() == null ||
                request.getPassword().length() < 8) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Password must contain at least 8 characters");
        }

        String email = request.getEmail()
                .trim()
                .toLowerCase();

        // Check whether the email is already registered.
        if (userRepository.findByEmail(email) != null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email is already registered");
        }

        // Create a new user.
        User user = new User();

        user.setFullName(request.getFullName().trim());
        user.setEmail(email);

        // Store the encoded password, not the original password.
        user.setPassword(
                passwordEncoder.encode(request.getPassword()));

        // Public registration must not allow users to choose ADMIN.
        user.setRole(Role.CUSTOMER);

        // Save the user in the database.
        User savedUser = userRepository.save(user);

        // Return the response without exposing the password.
        return new RegisterResponseDTO(
                savedUser.getId(),
                savedUser.getFullName(),
                savedUser.getEmail(),
                "Registration successful");
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {

        // Check whether the login request exists.
        if (request == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid email or password");
        }

        // Validate email and password.
        if (request.getEmail() == null ||
                request.getEmail().trim().isEmpty() ||
                request.getPassword() == null ||
                request.getPassword().isEmpty()) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid email or password");
        }

        String email = request.getEmail()
                .trim()
                .toLowerCase();

        // Find the user using their email.
        User user = userRepository.findByEmail(email);

        // Check the user and password.
        if (user == null ||
                !passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword())) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid email or password");
        }

        // Generate a JWT after successful login.
        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name());

        // Return user details and the token.
        return new LoginResponseDTO(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                "Login successful",
                token);
    }
}
