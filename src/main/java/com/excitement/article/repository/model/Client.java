package com.excitement.article.repository.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

@Data
public class Client {
    private @Id Long id;
    private String userName;
    private String password;
    private Role userRole;
}
