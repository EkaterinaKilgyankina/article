package com.excitement.article.contoller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequest {
    @NotBlank
    private String userName;
    @NotBlank
    private String password;
}
