package com.alejodev.espacioactivo.repository.impl;

import com.alejodev.espacioactivo.entity.Discipline;
import com.alejodev.espacioactivo.entity.DisciplineType;
import com.alejodev.espacioactivo.repository.IGenericRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IDisciplineRepository extends IGenericRepository<Discipline, Long> {

    @Query("""
        SELECT d FROM Discipline d
        WHERE d.name = :disciplineName AND d.type = :disciplineType
        """)
    Optional<Discipline> findIfExistsDiscipline(String disciplineName, DisciplineType disciplineType);

}
