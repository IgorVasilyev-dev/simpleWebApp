package com.mastery.simplewebapp.controller.rest;

import com.mastery.simplewebapp.dto.Employee;
import com.mastery.simplewebapp.service.api.IEmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/employees")
public class EmployeeRestServlet {

    private final IEmployeeService employeeService;

    public EmployeeRestServlet(IEmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping(produces = {"application/json"})
    protected ResponseEntity<?> getAllEmployees() {
        return ResponseEntity.ok().body(this.employeeService.getAll());
    }

    @GetMapping(value = "/{id}", produces = {"application/json"})
    protected ResponseEntity<?> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok().body(this.employeeService.getById(id));
    }

    @PostMapping(produces = {"application/json"}, consumes = {"application/json"})
    protected ResponseEntity<?> addEmployee(@RequestBody Employee employee) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.employeeService.addEmployee(employee));
    }

    @PutMapping(produces = {"application/json"}, consumes = {"application/json"})
    protected ResponseEntity<?> updateEmployee(@RequestBody Employee employee) {
        return ResponseEntity.ok().body(this.employeeService.updateEmployee(employee));
    }

    @DeleteMapping(value = "/{id}", produces = {"application/json"})
    protected ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        return ResponseEntity.ok().body(this.employeeService.deleteEmployed(id));
    }

}
