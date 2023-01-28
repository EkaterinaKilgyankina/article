package com.excitement.article.contoller;


import com.excitement.article.contoller.dto.ClientDto;
import com.excitement.article.contoller.dto.CreateClientResponse;
import com.excitement.article.service.ClientService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ClientController {
    private final ClientService service;

    @PostMapping(
            path = "/public/api/v1/clients",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public CreateClientResponse register(@Valid @RequestBody ClientDto request) {
        return service.register(request);
    }
}
