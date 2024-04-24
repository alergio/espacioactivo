package com.alejodev.espacioactivo.repository.impl;

import com.alejodev.espacioactivo.entity.Role;
import com.alejodev.espacioactivo.entity.RoleType;
import com.alejodev.espacioactivo.repository.IGenericRepository;

import java.util.Optional;

public interface IRoleRepository extends IGenericRepository<Role, Long> {

    Optional<Role> findByName(RoleType name);

}
