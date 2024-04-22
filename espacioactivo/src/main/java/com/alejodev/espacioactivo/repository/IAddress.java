package com.alejodev.espacioactivo.repository;

import com.alejodev.espacioactivo.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAddress extends JpaRepository<Address, Long> {
}
