package com.tfxsoftware.memserver.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tfxsoftware.memserver.auth.dto.SignInDto;
import com.tfxsoftware.memserver.auth.dto.SignInResponse;
import com.tfxsoftware.memserver.auth.dto.SignUpDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Void> register(@RequestBody @Valid SignUpDto dto) {
        authService.signUp(dto);
        // Use 201 Created for successful signups
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/signin")
    public ResponseEntity<SignInResponse> signIn(@RequestBody @Valid SignInDto dto) {
        SignInResponse response = authService.signIn(dto);
        // Use 201 Created for successful signups
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
