package com.excitement.article.contoller;

import com.excitement.article.config.SecurityConfig;
import com.excitement.article.config.security.UserDetailsCustomService;
import com.excitement.article.contoller.dto.ClientDto;
import com.excitement.article.contoller.dto.CreateClientResponse;
import com.excitement.article.exception.UserNameAlreadyExistException;
import com.excitement.article.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.stream.Stream;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@ActiveProfiles("test")
@WebMvcTest(controllers = ClientController.class)
@Import(SecurityConfig.class)
class ClientControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    ClientService service;
    //for security
    @MockBean
    UserDetailsCustomService userDetailsCustomService;
    @MockBean
    PasswordEncoder passwordEncoder;

    private static Stream<Arguments> values() {
        return Stream.of(
                Arguments.of("userName"),
                Arguments.of("password")
        );
    }

    @ParameterizedTest
    @MethodSource("values")
    void validations(String fieldName) throws Exception {
        ClientDto request = createRequest();
        ReflectionTestUtils.setField(request, fieldName, null);

        mockMvc.perform(MockMvcRequestBuilders.post("/public/api/v1/clients")
                        .content(objectMapper.writeValueAsBytes(request))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                           "code":"VALIDATION_ERROR",
                           "message":"%s must not be blank"
                        }
                        """.formatted(fieldName)));
    }

    @Test
    void register() throws Exception {
        ClientDto request = createRequest();
        Mockito.when(service.register(request)).thenReturn(
                new CreateClientResponse()
                        .setUserName("qqq")
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/public/api/v1/clients")
                        .content(objectMapper.writeValueAsBytes(request))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                            "userName": "qqq"
                        }
                        """));
    }

    @Test
    void register_userNameException() throws Exception {
        ClientDto request = createRequest();
        Mockito.when(service.register(request))
                .thenThrow(new UserNameAlreadyExistException());

        mockMvc.perform(MockMvcRequestBuilders.post("/public/api/v1/clients")
                        .content(objectMapper.writeValueAsBytes(request))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                           "code":"USER_NAME_ALREADY_EXIST",
                           "message":"UserName already exist. You have to change it"
                        }
                        """));
    }

    @Test
    @WithMockUser(authorities = "USER")
    void register_forbidden() throws Exception {
        ClientDto request = createRequest();
        Mockito.when(service.register(request))
                .thenThrow(new UserNameAlreadyExistException());

        mockMvc.perform(MockMvcRequestBuilders.post("/public/api/v1/clients")
                        .content(objectMapper.writeValueAsBytes(request))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    private ClientDto createRequest() {
        return new ClientDto()
                .setUserName("userName")
                .setPassword("pwd");
    }
}
