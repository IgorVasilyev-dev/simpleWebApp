package com.mastery.simplewebapp.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastery.simplewebapp.dto.Employee;
import com.mastery.simplewebapp.service.api.IEmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeRestServlet.class)
public class EmployeeRestServletMockMvcUnitTest {

    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private IEmployeeService employeeService;

    @BeforeEach
    public void setUp(WebApplicationContext context) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    private final List<Employee> employeeList = Arrays.asList(
            createTestEmployee(1, "Igor", "Ivanov"),
            createTestEmployee(2, "Anna", "Vasilyeva")
    );


    @Test
    public void giveListEmployees_whenGet_thenStatus200andReturnResults() throws Exception {

        Mockito.when(this.employeeService.getAll()).thenReturn(employeeList);

        mockMvc.perform(
                        get("/employees"))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getAllEmployees"))
                .andExpect(content().json(objectMapper.writeValueAsString(employeeList)));
    }

    @Test
    public void giveEmployeeById_whenGet_thenStatus200andReturns() throws Exception {

        Employee employee = employeeList.get(1);
        Mockito.when(this.employeeService.getById((Mockito.anyLong()))).thenReturn(employee);

        mockMvc.perform(
                        get("/employees/{id}", Mockito.anyLong())
                                .content(objectMapper.writeValueAsString(employee))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getEmployeeById"))
                .andExpect(content().json(objectMapper.writeValueAsString(employee)));
    }

    @Test
    public void givenEmployee_whenPost_thenStatus201andLordReturned() throws Exception {
        Employee employee = employeeList.get(0);
        Mockito.when(this.employeeService.addEmployee(Mockito.any())).thenReturn(employee);

        mockMvc.perform(
                        post("/employees")
                                .content(objectMapper.writeValueAsString(employee))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(handler().methodName("addEmployee"))
                .andExpect(content().json(objectMapper.writeValueAsString(employee)));
    }

    @Test
    public void giveLord_whenPut_thenStatus200andReturns() throws Exception {

        Employee employee = employeeList.get(1);
        Mockito.when(this.employeeService.updateEmployee(Mockito.any())).thenReturn(employee);

        mockMvc.perform(
                        put("/employees")
                                .content(objectMapper.writeValueAsString(employee))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("updateEmployee"))
                .andExpect(content().json(objectMapper.writeValueAsString(employee)));
    }

    @Test
    public void giveLord_whenDelete_thenStatus200andReturns() throws Exception {

        Employee employee = employeeList.get(1);
        Mockito.when(this.employeeService.deleteEmployed(Mockito.anyLong())).thenReturn(employee);

        mockMvc.perform(
                        delete("/employees/{id}", Mockito.anyLong())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("deleteEmployee"))
                .andExpect(content().json(objectMapper.writeValueAsString(employee)));
    }

    private Employee createTestEmployee(long id, String firstName, String lastName) {
        Employee employee = new Employee();
        employee.setEmployeeId(id);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        return employee;
    }
}