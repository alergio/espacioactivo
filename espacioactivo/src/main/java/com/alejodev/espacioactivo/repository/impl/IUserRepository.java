package com.alejodev.espacioactivo.repository.impl;

import com.alejodev.espacioactivo.entity.User;
import com.alejodev.espacioactivo.repository.IGenericRepository;

import java.util.Optional;

public interface IUserRepository extends IGenericRepository<User, Long> {
    Optional<User> findByEmail(String email);

}
