package com.mastery.simplewebapp.service;

import com.mastery.simplewebapp.dto.Employee;
import com.mastery.simplewebapp.error.exception.EntityNotFoundException;
import com.mastery.simplewebapp.service.api.IEmployeeService;
import com.mastery.simplewebapp.storage.api.IEmployeeRepository;
import com.mastery.simplewebapp.util.DetectNullOrEmptyFields;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

public class EmployeeService implements IEmployeeService {

    private final IEmployeeRepository repository;

    public EmployeeService(IEmployeeRepository repository) {
        this.repository = repository;
    }

    /**
     * Получить список всех Employee
     * @return List<Employee>
     */
    @Override
    public List<Employee> getAll() {
        return this.repository.findAll();
    }

    /**
     * Получить сотрудника по id
     * @param id идентификатор сущности
     * @return если объект есть в бд - объект типа Employee, иначе EntityNotFoundException
     */
    @Override
    public Employee getById(Long id) {
        return this.repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("entity with 'id = " + id + "' is not found"));
    }

    /**
     * добавить сотрудника
     * @param employee объект типа Employee
     * @return сохраненный объект типа Employee
     */
    @Transactional
    @Override
    public Employee addEmployee(Employee employee) {
        DetectNullOrEmptyFields.checkFields(employee);
        return this.repository.save(employee);
    }

    /**
     * Обновить данные о сотруднике
     * @param provider объект типа Employee
     * @return если объект существует в бд - обновленный объект типа Employee, иначе - EntityNotFoundException
     */
    @Transactional
    @Override
    public Employee updateEmployee(Employee provider) {
        this.repository.findById(provider.getEmployeeId()).orElseThrow(
                () -> new EntityNotFoundException("entity with 'id = " + provider.getEmployeeId() + "' is not found"));
        DetectNullOrEmptyFields.checkFields(provider);
        return this.repository.save(provider);
    }

    /**
     * Удалить сотрудника
     * @param id идентификатор employee
     * @return если объект существует в бд - удаленный объект employee, иначе - EntityNotFoundException
     */
    @Transactional
    @Override
    public Employee deleteEmployed(Long id) {
        Employee employee = this.repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("entity with 'id = " + id + "' is not found"));
        this.repository.deleteById(id);
        return employee;
    }
}
