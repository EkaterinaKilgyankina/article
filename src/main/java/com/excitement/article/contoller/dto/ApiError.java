package com.excitement.article.contoller.dto;

import lombok.Data;

@Data
public class ApiError {
    private ErrorCode code;
    private String message;
}
