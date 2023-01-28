package com.excitement.article.repository;

import com.excitement.article.repository.model.Client;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ClientRepository extends CrudRepository<Client, Long> {

    Optional<Client> findByUserName(String userName);
}
