package com.alejodev.espacioactivo.repository;

import com.alejodev.espacioactivo.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IActivityRepository extends JpaRepository<Activity, Long> {
}
