package com.excitement.article.contoller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
public class ClientDto {
    @NotBlank
    private String userName;

    @NotBlank
    private String password;
}
