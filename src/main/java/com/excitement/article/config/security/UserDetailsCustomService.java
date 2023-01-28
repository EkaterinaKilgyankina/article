package com.excitement.article.config.security;

import com.excitement.article.exception.ForbiddenException;
import com.excitement.article.repository.ClientRepository;
import com.excitement.article.repository.model.Client;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserDetailsCustomService implements UserDetailsService {
    private final ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client clientModel = clientRepository.findByUserName(username)
                .orElseThrow(ForbiddenException::new);

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(clientModel.getUserRole().name()));

        return new User(clientModel.getUserName(), clientModel.getPassword(), authorities);
    }
}
