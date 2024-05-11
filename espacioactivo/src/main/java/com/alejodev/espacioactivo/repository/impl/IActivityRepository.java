package com.alejodev.espacioactivo.repository.impl;

import com.alejodev.espacioactivo.entity.Activity;
import com.alejodev.espacioactivo.repository.IGenericRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IActivityRepository extends IGenericRepository<Activity, Long> {

    @Query("SELECT a FROM Activity a " +
            "WHERE a.user.id = :userId")
    List<Activity> findAllActivitiesByUserId(Long userId);

}
