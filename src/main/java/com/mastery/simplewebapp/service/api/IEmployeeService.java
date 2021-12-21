package com.mastery.simplewebapp.service.api;

import com.mastery.simplewebapp.dto.Employee;

import java.util.List;

public interface IEmployeeService {

    /**
     * Получить список всех сотрудников
     * @return список объектов типа Employee
     */
    List<Employee> getAll();

    /**
     * Получить сотрудника по id
     * @param id идентификатор сущности
     * @return объект типа Employee
     */
    Employee getById(Long id);

    /**
     * добавить сотрудника
     * @param employee объект типа Employee
     * @return объект типа Employee
     */
    Employee addEmployee(Employee employee);

    /**
     * Обновить данные о сотруднике
     * @param provider объект типа Employee
     * @return объект типа Employee
     */
    Employee updateEmployee(Employee provider);

    /**
     * Удалить сотрудника
     * @return объект типа Employee
     */
    Employee deleteEmployed(Long id);

}
