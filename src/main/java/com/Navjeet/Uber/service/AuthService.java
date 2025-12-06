package com.Navjeet.Uber.service;

import com.Navjeet.Uber.DTO.AuthResponse;
import com.Navjeet.Uber.DTO.LoginRequest;
import com.Navjeet.Uber.DTO.RegisterRequest;
import com.Navjeet.Uber.exception.BadRequestException;
import com.Navjeet.Uber.exception.NotFoundException;
import com.Navjeet.Uber.model.User;
import com.Navjeet.Uber.repository.UserRepository;
import com.Navjeet.Uber.util.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                       BCryptPasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public void register(RegisterRequest req) {
        String uname = req.getUsername();
        if (userRepository.existsByUsername(uname)) {
            throw new BadRequestException("username already taken");
        }

        String role = req.getRole();
        if (role == null || (!role.equals("ROLE_USER") && !role.equals("ROLE_DRIVER"))) {
            role = "ROLE_USER";
        }

        User user = new User();
        user.setUsername(uname);
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRole(role);
        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest req) {
        User user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new NotFoundException("invalid credentials"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new NotFoundException("invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        return new AuthResponse(token, user.getUsername(), user.getRole());
    }
}