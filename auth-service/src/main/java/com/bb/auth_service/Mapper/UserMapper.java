package com.bb.auth_service.Mapper;

import com.bb.auth_service.dto.AuthResponseDTO;
import com.bb.auth_service.dto.RegisterRequestDTO;
import com.bb.auth_service.model.AppUser;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

public class UserMapper {

    public static AppUser toModel(RegisterRequestDTO registerRequestDTO, PasswordEncoder passwordEncoder){
        AppUser user=new AppUser();
        user.setEmail(registerRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        user.setRoles(Set.of("ROLE_USER"));
        return user;
    }
    public static AuthResponseDTO toDTO(AppUser appUser, String token) {
        AuthResponseDTO dto = new AuthResponseDTO();
        dto.setToken(token);
        dto.setUserId(appUser.getId().toString());
        dto.setEmail(appUser.getEmail());
        return dto;
    }

}
