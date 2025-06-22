package com.ecourier.userservice.controller;

import com.ecourier.userservice.constant.Role;
import com.ecourier.userservice.dto.*;
import com.ecourier.userservice.entity.User;
import com.ecourier.userservice.security.TokenBlacklist;
import com.ecourier.userservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final TokenBlacklist tokenBlacklist;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        authService.registerUser(request, Role.CUSTOMER);
        return ResponseEntity.ok("Customer registered successfully");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register-agent")
    public ResponseEntity<String> registerAgent(@RequestBody RegisterRequest request) {
        authService.registerUser(request, Role.AGENT);
        return ResponseEntity.ok("Agent registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenBlacklist.blacklistToken(token);
            return ResponseEntity.ok("Logged out successfully");
        }
        return ResponseEntity.badRequest().body("Invalid token");
    }

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('AGENT')")
    @PutMapping("/update-profile")
    public ResponseEntity<String> updateProfile(@RequestBody UpdateProfileRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        authService.updateProfile(email, request);
        return ResponseEntity.ok("Profile updated successfully");
    }

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('AGENT')")
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        authService.changePassword(userDetails.getUsername(), request);
        return ResponseEntity.ok("Password changed successfully");
    }

    @GetMapping("/info/{email}")
    public UserInfoDto getUserInfo(@PathVariable String email) {
        User user = authService.getUserInfo(email);

        UserInfoDto dto = new UserInfoDto();
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name());
        return dto;
    }
}
