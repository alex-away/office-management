package com.super30.workservice.Repository;

import com.super30.workservice.Entity.Work;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkRepository extends JpaRepository<Work, Long> {
    List<Work> findAllByEmployeeId(Long employeeId);
}
