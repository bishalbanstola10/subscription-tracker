package com.bb.auth_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AuthRequestDTO {
    @NotNull(message = "Email is required")
    @Email(message = "Email should be valid.")
    private String email;

    @NotNull(message = "password is required")
    @Size(min=8,message="Password should be at least 8 characters long.")
    private String password;

    public @NotNull(message = "Email is required") @Email(message = "Email should be valid.") String getEmail() {
        return email;
    }

    public void setEmail(@NotNull(message = "Email is required") @Email(message = "Email should be valid.") String email) {
        this.email = email;
    }

    public @NotNull(message = "password is required") @Size(min = 8, message = "Password should be at least 8 characters long.") String getPassword() {
        return password;
    }

    public void setPassword(@NotNull(message = "password is required") @Size(min = 8, message = "Password should be at least 8 characters long.") String password) {
        this.password = password;
    }
}
