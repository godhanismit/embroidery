package com.example.demo.controller;

import com.example.demo.dtos.AuthResponse;
import com.example.demo.dtos.LoginRequest;
import com.example.demo.dtos.SignupRequest;
import com.example.demo.util.JwtUtil;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public AuthController(
            UserRepository userRepo,
            PasswordEncoder encoder,
            JwtUtil jwtUtil
    ) {
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    // ✅ SIGNUP
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest req) {

        if (userRepo.existsByMobile(req.getMobile())) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", "Mobile already registered"));
        }

        User user = new User();
        user.setMobile(req.getMobile());
        user.setPassword(encoder.encode(req.getPassword()));

        userRepo.save(user);

        return ResponseEntity.ok(Map.of("message", "User created"));
    }

    // ✅ LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        User user = userRepo.findByMobile(req.getMobile())
                .orElseThrow(() ->
                        new RuntimeException("Invalid credentials"));

        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getMobile());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}

