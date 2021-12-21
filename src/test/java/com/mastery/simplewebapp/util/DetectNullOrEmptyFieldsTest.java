package com.mastery.simplewebapp.util;

import com.mastery.simplewebapp.dto.Employee;
import com.mastery.simplewebapp.dto.Enum.Gender;
import com.mastery.simplewebapp.error.exception.SQLConstraintException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DetectNullOrEmptyFieldsTest {

    private static final Predicate<Employee> notNullOrEmptyFirstNameAndLastName = e ->
            e.getFirstName() == null || e.getFirstName().isEmpty()
                    || e.getLastName() == null || e.getLastName().isEmpty();

    /**
     * Тест на проверку полей помеченных @NotNullOrEmptyData на null or empty
     * Если обязательный поля являются null or empty выбрасывает RuntimeException SQLConstraintException
     * @param employee тестируемый экземпляр класса Employee
     */
    @ParameterizedTest
    @DisplayName("Проверка полей помеченных @NotNullOrEmptyData на null or empty")
    @MethodSource("valueProviderEmployees")
    void checkFields(Employee employee) {
        if(notNullOrEmptyFirstNameAndLastName.negate().test(employee)) {
            DetectNullOrEmptyFields.checkFields(employee);
        } else {
            Throwable thrown = assertThrows(SQLConstraintException.class, () -> DetectNullOrEmptyFields.checkFields(employee));
            assertNotNull(thrown.getMessage());
        }

    }

    public static Stream<Employee> valueProviderEmployees() {
        return Stream.of(
                createTestEmployee("Ivan", "Smit", 1L, Gender.MALE, "IT","1989-11-11"),
                createTestEmployee("Anna", "Vasilyeva", 2L, Gender.FEMALE, null, null),
                createTestEmployee("Irina", "Hokomada", 3L, Gender.FEMALE, null, "1929-02-13"),
                createTestEmployee("", "Tuz", 4L, null, "IT", "1989-11-24"),
                createTestEmployee("Odin", null, 5L, null, null, "1989-11-11"),
                createTestEmployee("Vasya", "Petrov", 6L, Gender.MALE, "IT", "2009-04-17"),
                createTestEmployee("Kolya", null, 7L, Gender.MALE, "IT", "2003-03-3"),
                createTestEmployee(null, "Gydrov", 8L, Gender.MALE, "IT", "2011-11-11"),
                createTestEmployee("", "Hrom", 9L, Gender.FEMALE, null, "2021-02-14"),
                createTestEmployee("Rita", "Winks", null, Gender.MALE, "IT", "2021-04-21"),
                createTestEmployee("Lex", "", 10L, null, "IT", "")
        );
    }

    /**
     * Создать экземпляр Employee и заполнить данными
     * @return объект Employee
     */
    private static Employee createTestEmployee(String firstName, String lastName, Long departmentId, Gender gender,
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