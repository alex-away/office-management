package com.super30.employeeservice.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    @NotBlank(message = "name is required")
    @Size(min = 2, max = 100, message = "name should have at least 2 characters")
    private String name;

    @NotBlank(message = "email is required")
    @Email(message = "email should be valid")
    private String email;

    @NotNull(message = "age is required")
    @Min(value = 18, message = "age should be greater than or equal to 18")
    @Max(value = 100, message = "age should be less than or equal to 100")
    private int age;

    @NotBlank(message = "phone is required")
    @Pattern(regexp = "^\\d{10}$", message = "phone should be a 10-digit number")
    private String phone;
}
