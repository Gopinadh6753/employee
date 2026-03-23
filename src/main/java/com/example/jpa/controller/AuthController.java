//package com.example.jpa.controller;
//
//
//
//import com.example.jpa.model.AuthRequest;
//import com.example.jpa.model.AuthResponse;
//import com.example.jpa.util.JwtUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/auth")
//@CrossOrigin(origins = "*")
//public class AuthController {
//
//    @Autowired
//    private AuthenticationManager authManager;
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @PostMapping("/login")
//    public AuthResponse login(@RequestBody AuthRequest request) {
//        Authentication authentication = authManager.authenticate(
//                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
//        );
//        User user = (User) authentication.getPrincipal();
//        String token = jwtUtil.generateToken(user.getUsername());
//        return new AuthResponse(token);
//    }
//}
