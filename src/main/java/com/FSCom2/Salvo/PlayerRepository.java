package com.FSCom2.Salvo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface PlayerRepository extends JpaRepository<Player,Long> {
    Player findByEmail(String email);

    List<Player> findByFirstName(String firstName);
    List<Player> findByLastName(String lastName);
    Player findByPass(String pass);
}
