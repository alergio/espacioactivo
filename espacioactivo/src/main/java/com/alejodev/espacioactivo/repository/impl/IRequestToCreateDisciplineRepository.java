package com.alejodev.espacioactivo.repository.impl;

import com.alejodev.espacioactivo.entity.RequestToCreateDiscipline;
import com.alejodev.espacioactivo.repository.IGenericRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRequestToCreateDisciplineRepository extends IGenericRepository<RequestToCreateDiscipline, Long> {

    @Query("SELECT r FROM RequestToCreateDiscipline r " +
            "WHERE r.user.email = :userName")
    List<RequestToCreateDiscipline> findAllRequestsByUser(String userName);

}
