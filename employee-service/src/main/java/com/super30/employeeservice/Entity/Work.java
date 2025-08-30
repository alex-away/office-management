package com.super30.employeeservice.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Work {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workId;

    @NotNull(message = "employee id is required")
    private Long employeeId;

    @NotBlank(message = "team name is required")
    private String teamName;

    @NotBlank(message = "task description is required")
    private String taskDescription;
}
