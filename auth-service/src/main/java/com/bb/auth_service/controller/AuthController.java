package com.bb.auth_service.controller;

import com.bb.auth_service.dto.AuthRequestDTO;
import com.bb.auth_service.dto.AuthResponseDTO;
import com.bb.auth_service.dto.RegisterRequestDTO;
import com.bb.auth_service.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authservice){
        this.authService=authservice;
    }
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDTO registerRequestDTO){
        String response =authService.register(registerRequestDTO);
        return ResponseEntity.ok().body(response);
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody  AuthRequestDTO authRequestDTO){
       Optional<AuthResponseDTO> authResponseDTOOptional= authService.authenticate(authRequestDTO);
       if (authResponseDTOOptional.isEmpty()){
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
       }
     AuthResponseDTO authResponseDTO=authResponseDTOOptional.get();
       return ResponseEntity.ok().body(authResponseDTO);
    }
    @GetMapping("/validate")
    public ResponseEntity<Void> validateToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return authService.validate(authHeader.substring(7))
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @PostMapping("/oauth/google")
    public ResponseEntity<AuthResponseDTO> loginWithGoogle(@RequestBody  Map<String,String> body){
        String googleToken = body.get("credential");
        Optional<AuthResponseDTO> authResponseDTOOptional= authService.authenticateWithGoogle(googleToken);
        if (authResponseDTOOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AuthResponseDTO authResponseDTO=authResponseDTOOptional.get();
        return ResponseEntity.ok().body(authResponseDTO);
    }
}

