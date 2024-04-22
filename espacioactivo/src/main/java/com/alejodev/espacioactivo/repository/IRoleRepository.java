package com.alejodev.espacioactivo.repository;

import com.alejodev.espacioactivo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoleRepository extends JpaRepository<Role, Long> {
}
