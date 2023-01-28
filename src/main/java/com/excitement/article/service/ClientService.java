package com.excitement.article.service;

import com.excitement.article.contoller.dto.ClientDto;
import com.excitement.article.contoller.dto.CreateClientResponse;
import com.excitement.article.exception.UserNameAlreadyExistException;
import com.excitement.article.mapper.ClientMapper;
import com.excitement.article.repository.ClientRepository;
import com.excitement.article.repository.model.Client;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public CreateClientResponse register(ClientDto request) {
        Optional<Client> existing = clientRepository.findByUserName(request.getUserName());

        if (existing.isPresent()) {
            throw new UserNameAlreadyExistException();
        }

        Client client = clientMapper.toModel(request);
        Client saved = clientRepository.save(client);

        return new CreateClientResponse()
                .setUserName(saved.getUserName());
    }
}
