package com.alejodev.espacioactivo.repository.impl;

import com.alejodev.espacioactivo.entity.Address;
import com.alejodev.espacioactivo.repository.IGenericRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAddress extends IGenericRepository<Address, Long> {
}
