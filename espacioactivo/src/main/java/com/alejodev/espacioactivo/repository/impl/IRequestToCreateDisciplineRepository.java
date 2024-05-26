package com.alejodev.espacioactivo.repository.impl;

import com.alejodev.espacioactivo.entity.DisciplineType;
import com.alejodev.espacioactivo.entity.RequestToCreateDiscipline;
import com.alejodev.espacioactivo.repository.IGenericRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IRequestToCreateDisciplineRepository extends IGenericRepository<RequestToCreateDiscipline, Long> {

    @Query("SELECT r FROM RequestToCreateDiscipline r " +
            "WHERE r.user.id = :userId")
    List<RequestToCreateDiscipline> findAllRequestsByUser(Long userId);


    @Query("SELECT r FROM RequestToCreateDiscipline r " +
            "WHERE r.disciplineName = :disciplineName " +
            "AND r.disciplineType = :disciplineType")
    Optional<RequestToCreateDiscipline> findRequestByNameAndType(String disciplineName, DisciplineType disciplineType);

}
