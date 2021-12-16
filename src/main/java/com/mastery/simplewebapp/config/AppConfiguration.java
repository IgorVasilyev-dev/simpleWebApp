package com.mastery.simplewebapp.config;

import com.mastery.simplewebapp.service.EmployeeService;
import com.mastery.simplewebapp.service.api.IEmployeeService;
import com.mastery.simplewebapp.storage.api.IEmployeeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.mastery.simplewebapp.config")
public class AppConfiguration {

    @Bean
    IEmployeeService employeeService(IEmployeeRepository repository) {
        return new EmployeeService(repository);
    }

}
