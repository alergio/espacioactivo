package com.alejodev.espacioactivo.repository;

import com.alejodev.espacioactivo.entity.Discipline;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDisciplineRepository extends JpaRepository<Discipline, Long> {
}
