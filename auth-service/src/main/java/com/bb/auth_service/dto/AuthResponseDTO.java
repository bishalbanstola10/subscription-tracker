package com.bb.auth_service.dto;

public class AuthResponseDTO {

    private String token;
    private String userId;
    private String email;

    public String getToken() {
        return token;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public void setToken(String token){this.token=token;}

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
