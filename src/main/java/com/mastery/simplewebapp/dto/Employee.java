package com.mastery.simplewebapp.dto;

import com.mastery.simplewebapp.dto.Enum.Gender;
import com.mastery.simplewebapp.util.annotahion.NotNullOrEmptyData;

import java.sql.Date;
import java.util.Objects;

public class Employee {

    private Long employeeId;

    @NotNullOrEmptyData
    private String firstName;

    @NotNullOrEmptyData
    private String lastName;

    private Long departmentId;

    private String jobTitle;

    private Gender gender;

    private Date birthDate;

    public Employee() {
    }

    public Employee(Long employeeId, String firstName, String lastName, Long departmentId, String jobTitle, Gender gender, Date birthDate) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.departmentId = departmentId;
        this.jobTitle = jobTitle;
        this.gender = gender;
        this.birthDate = birthDate;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(employeeId, employee.employeeId) && Objects.equals(firstName, employee.firstName) && Objects.equals(lastName, employee.lastName) && Objects.equals(departmentId, employee.departmentId) && Objects.equals(jobTitle, employee.jobTitle) && gender == employee.gender && Objects.equals(birthDate, employee.birthDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId, firstName, lastName, departmentId, jobTitle, gender, birthDate);
    }
}