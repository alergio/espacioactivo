package com.alejodev.espacioactivo.repository.impl;

import com.alejodev.espacioactivo.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ITokenRepository extends JpaRepository<Token, Long> {

    @Query("""
            SELECT t FROM Token t INNER JOIN User u
            ON t.user.id = u.id
            WHERE t.user.id = :userId AND t.loggedOut = false
            """)
    List<Token> findAllTokenByUser(Long userId);

    Optional<Token> findByToken(String token);

}