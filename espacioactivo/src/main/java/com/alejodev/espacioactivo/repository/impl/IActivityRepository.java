package com.alejodev.espacioactivo.repository.impl;

import com.alejodev.espacioactivo.entity.Activity;
import com.alejodev.espacioactivo.repository.IGenericRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IActivityRepository extends IGenericRepository<Activity, Long> {
}
