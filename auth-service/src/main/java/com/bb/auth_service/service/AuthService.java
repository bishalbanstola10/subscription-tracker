package com.bb.auth_service.service;

import com.bb.auth_service.Mapper.UserMapper;
import com.bb.auth_service.dto.AuthRequestDTO;
import com.bb.auth_service.dto.AuthResponseDTO;
import com.bb.auth_service.dto.RegisterRequestDTO;
import com.bb.auth_service.exception.UserAlreadyExistsException;
import com.bb.auth_service.model.AppUser;
import com.bb.auth_service.repository.UserRepository;
import com.bb.auth_service.util.JWTUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;


@Service
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public AuthService(UserRepository userRepository,JWTUtil jwtUtil,PasswordEncoder passwordEncoder){
        this.userRepository=userRepository;
        this.jwtUtil=jwtUtil;
        this.passwordEncoder=passwordEncoder;
    }
    public String register( RegisterRequestDTO registerRequestDTO){
        if (userRepository.existsByEmail(registerRequestDTO.getEmail())){
            throw new UserAlreadyExistsException("User already exists"+registerRequestDTO.getEmail());
        }
        userRepository.save(UserMapper.toModel(registerRequestDTO,passwordEncoder));
        return "User created successfully";
    }

    public Optional<AuthResponseDTO> authenticate(AuthRequestDTO authRequestDTO){
        Optional<AuthResponseDTO> authResponseDTO = userRepository.findByEmail(authRequestDTO.getEmail())
                .filter(u -> passwordEncoder.matches(authRequestDTO.getPassword(),
                        u.getPassword()))
                .map(u ->{
                      String token = jwtUtil.generateToken(u.getEmail(), u.getRoles());
                      return UserMapper.toDTO(u, token);
                } );
        return  authResponseDTO;
    }

    public Optional<AuthResponseDTO> authenticateWithGoogle(String googleToken) {
        GoogleIdToken.Payload payload = verifyGoogleToken(googleToken);
        logger.info("payload{}",payload);
        if (payload == null) {
            return Optional.empty();
        }

        String email = payload.getEmail();
        logger.info("email {}",email);
        AppUser user = userRepository.findByEmail(email).orElseGet(() -> {
            AppUser newUser = new AppUser();
            newUser.setEmail(email);
            newUser.setPassword(""); // Google users don't use password
            newUser.setRoles(Set.of("USER"));
            return userRepository.save(newUser);
        });

        String token = jwtUtil.generateToken(user.getEmail(), user.getRoles());
        logger.info("jwt{}",token);
        return Optional.of(UserMapper.toDTO(user, token));
    }
    private GoogleIdToken.Payload verifyGoogleToken(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList("944057449950-al3b1upb654hr3msuan1dqvf44gqoq4m.apps.googleusercontent.com"))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            logger.info("idToken {}",idToken);
            return idToken != null ? idToken.getPayload() : null;
        } catch (Exception e) {
            logger.info("Exception {}",e.getMessage());
            return null;
        }
    }

    public boolean validate(String token){
        try{
            jwtUtil.validateToken(token);
            return true;
        }
        catch (JwtException e){
            return  false;
        }
    }

}
