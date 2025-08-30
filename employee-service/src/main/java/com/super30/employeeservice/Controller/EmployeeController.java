package com.super30.employeeservice.Controller;

import com.super30.employeeservice.Entity.Employee;
import com.super30.employeeservice.Entity.Work;
import com.super30.employeeservice.Repository.EmployeeRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeRepository employeeRepository;
    private final RestTemplate restTemplate;

    @GetMapping()
    public ResponseEntity<List<Employee>> getAllEmployees() {

        List<Employee> employees = employeeRepository.findAll();
        if (employees.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long employeeId) {

        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        if (employee == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(employee);
    }

    @PostMapping()
    public ResponseEntity<?> createEmployee(@Valid @RequestBody Employee employee, BindingResult result) {

        if (result.hasErrors()) {
            return buildValidationErrorResponse(result);
        }
        Employee savedEmployee = employeeRepository.save(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee);
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<?> updateEmployee(@PathVariable Long employeeId, @Valid @RequestBody Employee employee, BindingResult result) {

        if (result.hasErrors()) {
            return buildValidationErrorResponse(result);
        }
        Employee existingEmployee = employeeRepository.findById(employeeId).orElse(null);
        if (existingEmployee == null) {
            return ResponseEntity.notFound().build();
        }
        existingEmployee.setName(employee.getName());
        existingEmployee.setEmail(employee.getEmail());
        existingEmployee.setAge(employee.getAge());
        existingEmployee.setPhone(employee.getPhone());
        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long employeeId) {

        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        if (employee == null) {
            return ResponseEntity.notFound().build();
        }
        employeeRepository.delete(employee);
        return ResponseEntity.ok("");
    }

    @GetMapping("/{employeeId}/work")
    public ResponseEntity<?> getWorksByEmployeeId(@PathVariable Long employeeId) {

        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        if (employee == null) {
            return ResponseEntity.notFound().build();
        }

        String url = "http://WORK-SERVICE/api/work/employee/" + employeeId;
        try {

            Work[] works = restTemplate.getForObject(url, Work[].class);

            if (works == null || works.length == 0) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(works);
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    @PostMapping("/work")
    public ResponseEntity<?> assignWork(@Valid @RequestBody Work work, BindingResult result) {

        if (result.hasErrors()) {
            return buildValidationErrorResponse(result);
        }

        String url = "http://WORK-SERVICE/api/work";

        try {

            Work assignedWork = restTemplate.postForObject(url, work, Work.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(assignedWork);
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    private ResponseEntity<Map<String, String>> buildValidationErrorResponse(BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        for (FieldError error : result.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
