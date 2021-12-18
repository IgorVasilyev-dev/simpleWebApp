package com.mastery.simplewebapp.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastery.simplewebapp.dto.Employee;
import com.mastery.simplewebapp.dto.Enum.Gender;
import com.mastery.simplewebapp.service.api.IEmployeeService;
import com.mastery.simplewebapp.storage.api.IEmployeeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class EmployeeRestServletMockMvcIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    IEmployeeService lordView;

    @Autowired
    IEmployeeRepository repository;

    @AfterEach
    public void resetDb() {
        repository.deleteAll();
    }

    private static final Predicate<Employee> notNullOrEmptyFirstNameAndLastName = e ->
                                e.getFirstName() == null || e.getFirstName().isEmpty()
                                    || e.getLastName() == null || e.getLastName().isEmpty();

    @Test
    public void givenEmployees_whenGetEmployees_thenStatus200() throws Exception {
        List<Employee> employeeList = valueProviderEmployees()
                .filter(notNullOrEmptyFirstNameAndLastName.negate())
                .map(this::saveEntity)
                .collect(Collectors.toList());

        mockMvc.perform(
                        get("/employees"))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getAllEmployees"))
                .andExpect(content().json(objectMapper.writeValueAsString(employeeList)))
        ;
    }

    @Test
    public void givenId_whenGetExistingEmployee_thenStatus200andEmployeeReturned_and_thenStatus400andErrorReturns() throws Exception {
        List<Employee> employeeList = valueProviderEmployees()
                .filter(notNullOrEmptyFirstNameAndLastName.negate())
                .map(this::saveEntity).sorted(Comparator.comparing(Employee::getEmployeeId)).collect(Collectors.toList());
        long min = employeeList.get(employeeList.size() - 1).getEmployeeId();
        for (Employee employee: employeeList) {
            Long id = employee.getEmployeeId();
            Long anyId = randomIdIncludeMax(min + 10, min);

            mockMvc.perform(
                            get("/employees/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(handler().methodName("getEmployeeById"))
                    .andExpect(jsonPath("$.employeeId").value(id))
                    .andExpect(jsonPath("$.firstName").value(employee.getFirstName()))
                    .andExpect(jsonPath("$.lastName").value(employee.getLastName()));

            mockMvc.perform(
                            get("/employees/{id}", anyId))
                    .andExpect(status().isBadRequest())
                    .andExpect(handler().methodName("getEmployeeById"))
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.message").value("Not Found Entity"))
                    .andExpect(jsonPath("$.debugMessage").value("entity with 'id = " + anyId + "' is not found"));
        }

    }

    @Test
    public void givenEmployee_whenPost_thenStatus201andEmployeeReturned_and_thenStatus400andErrorReturns() throws Exception {
        List<Employee> employeeList = valueProviderEmployees().collect(Collectors.toList());
        for (Employee e: employeeList) {
            if(notNullOrEmptyFirstNameAndLastName.negate().test(e)) {
                mockMvc.perform(
                                post("/employees")
                                        .content(objectMapper.writeValueAsString(e))
                                        .contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isCreated())
                        .andExpect(handler().methodName("addEmployee"))
                        .andExpect(jsonPath("$.employeeId").isNumber())
                        .andExpect(jsonPath("$.firstName").value(e.getFirstName()))
                        .andExpect(jsonPath("$.lastName").value(e.getLastName()))
                        .andExpect(jsonPath("$.lastName").value(e.getLastName()));
            } else {
                mockMvc.perform(
                                post("/employees")
                                        .content(objectMapper.writeValueAsString(e))
                                        .contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isBadRequest())
                        .andExpect(handler().methodName("addEmployee"))
                        .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                        .andExpect(jsonPath("$.message").value("Internal Server Error"))
                        .andExpect(jsonPath("$.debugMessage").isString());
            }
        }
    }

    @Test
    public void givenEmployee_whenPut_thenStatus201andEmployeeReturned_and_thenStatus400andErrorReturns() throws Exception {
        List<Employee> employeeListExistOnDb = valueProviderEmployees().limit(5)
                .filter(notNullOrEmptyFirstNameAndLastName.negate())
                .map(this::saveEntity).sorted(Comparator.comparing(Employee::getEmployeeId)).collect(Collectors.toList());
        long lastIdOnDb = employeeListExistOnDb.get(employeeListExistOnDb.size() - 1).getEmployeeId();
        Long anyId = randomIdIncludeMax(lastIdOnDb + 10, lastIdOnDb);
        List<Employee> employeeList = valueProviderEmployees().skip(5).peek(employee -> employee.setEmployeeId(anyId)).collect(Collectors.toList());

        employeeList.addAll(employeeListExistOnDb);
        for (Employee e: employeeList) {
            if (notNullOrEmptyFirstNameAndLastName.negate().test(e) && e.getEmployeeId() <= lastIdOnDb) {
                System.out.println(e.getDepartmentId());
                mockMvc.perform(
                                put("/employees")
                                        .content(objectMapper.writeValueAsString(e))
                                        .contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isOk())
                        .andExpect(handler().methodName("updateEmployee"))
                        .andExpect(jsonPath("$.employeeId").isNumber())
                        .andExpect(jsonPath("$.firstName").value(e.getFirstName()))
                        .andExpect(jsonPath("$.lastName").value(e.getLastName()))
                        .andExpect(jsonPath("$.lastName").value(e.getLastName()));

            } else if(e.getEmployeeId() <= lastIdOnDb) {
                mockMvc.perform(
                                put("/employees")
                                        .content(objectMapper.writeValueAsString(e))
                                        .contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isBadRequest())
                        .andExpect(handler().methodName("updateEmployee"))
                        .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                        .andExpect(jsonPath("$.message").value("Internal Server Error"))
                        .andExpect(jsonPath("$.debugMessage").isString());
            } else {
                mockMvc.perform(
                                put("/employees")
                                        .content(objectMapper.writeValueAsString(e))
                                        .contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isBadRequest())
                        .andExpect(handler().methodName("updateEmployee"))
                        .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                        .andExpect(jsonPath("$.message").value("Not Found Entity"))
                        .andExpect(jsonPath("$.debugMessage").value("entity with 'id = " + e.getEmployeeId() + "' is not found"));
            }
        }
    }

    @Test
    public void giveEmployee_whenDelete_thenStatus202andReturns_and_thenStatus400andErrorReturns() throws Exception {
        List<Employee> employeeList = valueProviderEmployees()
                .filter(notNullOrEmptyFirstNameAndLastName.negate())
                .map(this::saveEntity).sorted(Comparator.comparing(Employee::getEmployeeId)).collect(Collectors.toList());
        long lastIdOnDb = employeeList.get(employeeList.size() - 1).getEmployeeId();

        for (Employee e: employeeList) {
            Long anyId = randomIdIncludeMax(lastIdOnDb + 30, lastIdOnDb);
            mockMvc.perform(
                            delete("/employees/{id}", e.getEmployeeId())
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(handler().methodName("deleteEmployee"))
                    .andExpect(content().json(objectMapper.writeValueAsString(e)));

            mockMvc.perform(
                            delete("/employees/{id}",anyId )
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(handler().methodName("deleteEmployee"))
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.message").value("Not Found Entity"))
                    .andExpect(jsonPath("$.debugMessage").value("entity with 'id = " + anyId + "' is not found"));
        }

    }

    public Stream<Employee> valueProviderEmployees() {
        return Stream.of(
                createTestEmployee("Ivan", "Smit", 1L, Gender.MALE, "IT","1989-11-11"),
                createTestEmployee("Anna", "Vasilyeva", 2L, Gender.FEMALE, null, null),
                createTestEmployee("Irina", "Hokomada", 3L, Gender.FEMALE, null, "1929-02-13"),
                createTestEmployee("Rank", "Tuz", 4L, null, "IT", "1989-11-24"),
                createTestEmployee("Odin", "Vaskevich", 5L, null, null, "1989-11-11"),
                createTestEmployee("Vasya", "Petrov", 6L, Gender.MALE, "IT", "2009-04-17"),
                createTestEmployee("Kolya", null, 7L, Gender.MALE, "IT", "2003-03-3"),
                createTestEmployee(null, "Gydrov", 8L, Gender.MALE, "IT", "2011-11-11"),
                createTestEmployee("", "Hrom", 9L, Gender.FEMALE, null, "2021-02-14"),
                createTestEmployee("Rita", "Winks", null, Gender.MALE, "IT", "2021-04-21"),
                createTestEmployee("Lex", "", 10L, null, "IT", "")
        );
    }

    private Employee saveEntity(Employee employee) {
        return repository.save(employee);
    }

    private Long randomIdIncludeMax(long max, long min) {
        return (long) ((Math.random()*(max - min) + ++min));
    }

    private Employee createTestEmployee(String firstName, String lastName, Long departmentId, Gender gender,
                                        String jobTitle, String birthDate) {
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("yyyy-MM-dd");
        java.util.Date utilDate;
        java.sql.Date sqlDate = null;
        try {
            if (birthDate != null) {
                if(!birthDate.isEmpty()) {
                    utilDate = format.parse(birthDate);
                    sqlDate = new java.sql.Date(utilDate.getTime());
                }
            }
            Employee employee = new Employee();
            employee.setFirstName(firstName);
            employee.setLastName(lastName);
            employee.setDepartmentId(departmentId);
            employee.setGender(gender);
            employee.setJobTitle(jobTitle);
            employee.setBirthDate(sqlDate);
            return employee;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}