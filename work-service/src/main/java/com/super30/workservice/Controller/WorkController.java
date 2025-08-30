package com.super30.workservice.Controller;

import com.super30.workservice.Entity.Work;
import com.super30.workservice.Repository.WorkRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/work")
public class WorkController {

    private final WorkRepository workRepository;
    private final RestTemplate restTemplate;

    @PostMapping
    public ResponseEntity<?> assignWork(@Valid @RequestBody Work work, BindingResult result) {

        if (result.hasErrors()) {
            return buildValidationErrorResponse(result);
        }

        try {

            String url = "http://EMPLOYEE-SERVICE/api/employee/" + work.getEmployeeId();
            restTemplate.getForObject(url, Object.class);
            Work savedWork = workRepository.save(work);
            return ResponseEntity.ok(savedWork);
        } catch (HttpClientErrorException.BadRequest e) {

            Map<String, String> error = new HashMap<>();
            error.put("employeeId", "Employee with ID " + work.getEmployeeId() + " not found.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (RestClientException e) {

            Map<String, String> error = new HashMap<>();
            error.put("error", "Error communicating with employee-service: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
        }
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Work>> getWorksByEmployeeId(@PathVariable Long employeeId) {

        try {

            String url = "http://EMPLOYEE-SERVICE/api/employee/" + employeeId;
            restTemplate.getForObject(url, Object.class);
            List<Work> works = workRepository.findAllByEmployeeId(employeeId);
            if (works.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(works);
        } catch (HttpClientErrorException.BadRequest e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
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
