package com.alejodev.espacioactivo.repository.impl;

import com.alejodev.espacioactivo.entity.User;
import com.alejodev.espacioactivo.repository.IGenericRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends IGenericRepository<User, Long> {
    Optional<User> findByEmail(String email);

}
