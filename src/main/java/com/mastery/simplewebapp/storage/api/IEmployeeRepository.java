package com.mastery.simplewebapp.storage.api;

import com.mastery.simplewebapp.dto.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEmployeeRepository extends JpaRepository<Employee, Long> {

}
