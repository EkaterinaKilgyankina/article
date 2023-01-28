package com.excitement.article.service;

import com.excitement.article.contoller.dto.ClientDto;
import com.excitement.article.contoller.dto.CreateClientResponse;
import com.excitement.article.exception.UserNameAlreadyExistException;
import com.excitement.article.mapper.ClientMapper;
import com.excitement.article.repository.ClientRepository;
import com.excitement.article.repository.model.Client;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {
    @InjectMocks
    ClientService service;
    @Mock
    ClientRepository clientRepository;
    @Mock
    ClientMapper clientMapper;

    @Test
    void register() {
        ClientDto dto = new ClientDto()
                .setUserName("iName")
                .setPassword("pwd");
        Client client = new Client()
                .setUserName("rName");
        Mockito.when(clientRepository.findByUserName(dto.getUserName()))
                .thenReturn(Optional.empty());
        Mockito.when(clientMapper.toModel(dto)).thenReturn(client);
        Mockito.when(clientRepository.save(client)).thenAnswer(e -> e.getArgument(0));

        CreateClientResponse result = service.register(dto);

        Assertions.assertThat(result)
                .matches(e -> e.getUserName().equals("rName"));
    }

    @Test
    void register_userNameAlreadyExists() {
        ClientDto dto = new ClientDto()
                .setUserName("iName")
                .setPassword("pwd");
        Mockito.when(clientRepository.findByUserName(dto.getUserName()))
                .thenReturn(Optional.of(new Client()));

        Assertions.assertThatThrownBy(() -> service.register(dto))
                .isInstanceOf(UserNameAlreadyExistException.class);
    }
}
