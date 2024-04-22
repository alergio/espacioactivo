package com.alejodev.espacioactivo.repository;

import com.alejodev.espacioactivo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

}
