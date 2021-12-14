package com.mastery.simplewebapp.dao.api;

import com.mastery.simplewebapp.dto.Employee;

import java.util.List;
import java.util.Optional;

public interface IDao<T> {

   Optional<T> getById(Long id);

    List<Employee> getAll();

    Employee addEmployee(Employee employee);

    Optional<T> updateEmployee(Employee employee);

    void deleteEmployee(Long id);

}
