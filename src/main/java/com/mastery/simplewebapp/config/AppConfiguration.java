package com.mastery.simplewebapp.config;

import com.mastery.simplewebapp.dao.EmployeeDAO;
import com.mastery.simplewebapp.dao.api.IDao;
import com.mastery.simplewebapp.dto.Employee;
import com.mastery.simplewebapp.service.EmployeeService;
import com.mastery.simplewebapp.service.api.IEmployeeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.mastery.simplewebapp.config")
public class AppConfiguration {

    @Bean
    IDao<Employee> employeeDAO(DataSource dataSource) {
        return new EmployeeDAO(dataSource);
    }

    @Bean
    IEmployeeService employeeService(IDao<Employee> employeeDAO) {
        return new EmployeeService(employeeDAO);
    }

}
