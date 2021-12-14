package com.mastery.simplewebapp.service;

import com.mastery.simplewebapp.dao.api.IDao;
import com.mastery.simplewebapp.dto.Employee;
import com.mastery.simplewebapp.error.exception.EntityNotFoundException;
import com.mastery.simplewebapp.service.api.IEmployeeService;

import java.util.List;

public class EmployeeService implements IEmployeeService {

    private final IDao<Employee> employeeDAO;

    public EmployeeService(IDao<Employee> employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @Override
    public List<Employee> getAll() {
        return this.employeeDAO.getAll();
    }

    @Override
    public Employee getById(Long id) {
        return this.employeeDAO.getById(id).orElseThrow(
                () -> new EntityNotFoundException("entity with 'id = " + id + "' is not found"));
    }

    @Override
    public Employee addEmployee(Employee employee) {
        return this.employeeDAO.addEmployee(employee);
    }

    @Override
    public Employee updateEmployee(Employee provider) {
        return this.employeeDAO.updateEmployee(provider).orElseThrow(
                () -> new EntityNotFoundException("entity with 'id = " + provider.getEmployeeId() + "' is not found"));
    }

    @Override
    public Employee deleteEmployed(Long id) {
        Employee employee = this.employeeDAO.getById(id).orElseThrow(
                () -> new EntityNotFoundException("entity with 'id = " + id + "' is not found"));
        this.employeeDAO.deleteEmployee(id);
        return employee;
    }
}
