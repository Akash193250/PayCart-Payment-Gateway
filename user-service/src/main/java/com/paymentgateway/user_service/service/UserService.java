package com.paymentgateway.user_service.service;

import com.paymentgateway.user_service.dto.LoginResponse;
import com.paymentgateway.user_service.util.JwtUtil;
import com.paymentgateway.user_service.dao.UserDAO;
import com.paymentgateway.user_service.dto.LoginRequest;
import com.paymentgateway.user_service.dto.RegisterRequest;
import com.paymentgateway.user_service.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil JwtUtil;

    public UserService(
            UserDAO userDAO,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil) {

        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
        this.JwtUtil = jwtUtil;
    }

    public String registerUser(RegisterRequest request) {

        String normalizedEmail = request.getEmail()
                .trim()
                .toLowerCase();

        if (userDAO.existsByEmail(normalizedEmail)) {
            throw new IllegalArgumentException(
                    "Email is already registered");
        }

        User user = new User();

        user.setFullName(request.getFullName().trim());
        user.setEmail(normalizedEmail);

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        user.setPassword(hashedPassword);
        user.setRole("USER");

        int rowsAffected = userDAO.save(user);

        if (rowsAffected == 1) {
            return "User registered successfully";
        }

        throw new IllegalStateException(
                "User registration failed");
    }

    public LoginResponse loginUser(LoginRequest request) {

        String normalizedEmail = request.getEmail()
                .trim()
                .toLowerCase();

        User user = userDAO.findByEmail(normalizedEmail);

        boolean passwordMatches = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword());

        if (!passwordMatches) {
            throw new IllegalArgumentException(
                    "Invalid email or password");
        }

        String token = JwtUtil.generateToken(
                user.getEmail(),
                user.getRole());

        return new LoginResponse(
                "Login successful",
                token,
                user.getEmail(),
                user.getRole());
    }
}
