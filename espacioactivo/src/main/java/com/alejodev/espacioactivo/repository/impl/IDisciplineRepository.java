package com.alejodev.espacioactivo.repository.impl;

import com.alejodev.espacioactivo.entity.Discipline;
import com.alejodev.espacioactivo.repository.IGenericRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDisciplineRepository extends IGenericRepository<Discipline, Long> {
}
