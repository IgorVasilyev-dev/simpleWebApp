package com.mastery.simplewebapp.dao.api;

import com.mastery.simplewebapp.dto.Employee;

import java.util.List;
import java.util.Optional;

public interface IDao<T> {

    /**
     * Получить объект T из БД по Id
     * @param id параметр запроса
     * @return если объект существует в бд - Optional.of(employee), иначе - Optional.empty()
     */
   Optional<T> getById(Long id);

    /**
     * Получить список объектов Employee из БД
     * @return List<Employee>
     */
    List<Employee> getAll();

    /**
     * Добавить объект employee в БД
     * @param employee объект типа Employee
     * @return сохраненный объект employee
     */
    Employee addEmployee(Employee employee);

    /**
     * Обновить объект в БД
     * @param employee объект типа Employee
     * @return если объект существует в бд - Optional.of(employee), иначе - Optional.empty()
     */
    Optional<T> updateEmployee(Employee employee);

    /**
     * Удалить объект из БД
     * @param id параметр запроса
     */
    void deleteEmployee(Long id);

}
