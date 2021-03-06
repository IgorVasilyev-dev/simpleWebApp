package com.mastery.simplewebapp.dao;

import com.mastery.simplewebapp.dao.api.IDao;
import com.mastery.simplewebapp.dto.Employee;
import com.mastery.simplewebapp.dto.Enum.Gender;
import com.mastery.simplewebapp.error.exception.SQLRuntimeException;
import com.mastery.simplewebapp.util.DetectNullOrEmptyFields;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeDAO implements IDao<Employee> {

    private final DataSource dataSource;

    public EmployeeDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Получить объект по Id из Бд
     * @param id параметр запроса
     * @return если объект существует в бд - Optional.of(employee), иначе - Optional.empty()
     * при перехвате SQLException генерируется SQLRuntimeException
     */
    @Override
    public Optional<Employee> getById(Long id) {
        Optional<Employee> employeeOptional = Optional.empty();
        String selectByIdQuery = "SELECT * FROM employee WHERE employee_id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(selectByIdQuery)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Employee e = new Employee();
                e.setEmployeeId(rs.getLong("employee_id"));
                e.setFirstName(rs.getString("first_name"));
                e.setLastName(rs.getString("last_name"));
                if(rs.getString("department_id") != null) {
                    e.setDepartmentId(rs.getObject("department_id", Long.class));
                }
                e.setJobTitle(rs.getString("job_title"));
                if(rs.getString("gender") != null) {
                    e.setGender(Gender.valueOf(rs.getString("gender")));
                }
                e.setBirthDate(rs.getDate("date_of_birth"));
                employeeOptional = Optional.of(e);
            }
            rs.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new SQLRuntimeException(exception);
        }
        return employeeOptional;
    }

    /**
     * Получить список из Бд
     * @return List<Employee>
     * при перехвате SQLException генерируется SQLRuntimeException
     */
    @Override
    public List<Employee> getAll() {
        List<Employee> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM employee";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(selectQuery)){

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Employee e = new Employee();
                e.setEmployeeId(rs.getLong("employee_id"));
                e.setFirstName(rs.getString("first_name"));
                e.setLastName(rs.getString("last_name"));
                if(rs.getString("department_id") != null) {
                    e.setDepartmentId(rs.getObject("department_id", Long.class));
                }
                e.setJobTitle(rs.getString("job_title"));
                if(rs.getString("gender") != null) {
                    e.setGender(Gender.valueOf(rs.getString("gender")));
                }
                e.setBirthDate(rs.getDate("date_of_birth"));
                list.add(e);
            }
            rs.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new SQLRuntimeException(exception);
        }
        return list;
    }

    /**
     * Добавить объект типа Employee в БД
     * @param employee объект типа Employee
     * @return сохраненный объект employee
     * при перехвате SQLException генерируется SQLRuntimeException
     */
    @Override
    public Employee addEmployee(Employee employee) {
        DetectNullOrEmptyFields.checkFields(employee);
        String insertQuery = "INSERT INTO employee (first_name, last_name, department_id, job_title, gender, date_of_birth) \n" +
                "values (?,?,?,?,?,?);";

        try (Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, employee.getFirstName());
            ps.setString(2, employee.getLastName());
            ps.setObject(3, employee.getDepartmentId(), Types.BIGINT);
            ps.setString(4, employee.getJobTitle());
            if(employee.getGender() == null) {
                ps.setNull(5, Types.OTHER);
            } else {
                ps.setObject(5, employee.getGender().toString(), Types.OTHER);
            }
            ps.setObject(6, employee.getBirthDate(), Types.DATE);
            if(ps.executeUpdate() != 1)  {
                con.rollback();
            } else {
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                employee.setEmployeeId(rs.getLong("employee_id"));
                rs.close();
                con.commit();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new SQLRuntimeException(exception);
        }
        return employee;
    }

    /**
     * Обновить объект в БД
     * @param provider объект типа Employee
     * @return если объект существует в бд - Optional.of(employee), иначе - Optional.empty()
     * при перехвате SQLException генерируется SQLRuntimeException
     */
    @Override
    public Optional<Employee> updateEmployee(Employee provider) {
        DetectNullOrEmptyFields.checkFields(provider);
        String updateQuery = "UPDATE employee set first_name = ?, last_name = ?, department_id = ?, job_title = ?, \n " +
                "gender = ?, date_of_birth = ? where employee_id = ?";
            try(Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(updateQuery)){

                ps.setString(1, provider.getFirstName());
                ps.setString(2, provider.getLastName());
                ps.setObject(3, provider.getDepartmentId(), Types.BIGINT);
                ps.setString(4, provider.getJobTitle());
                if(provider.getGender() == null) {
                    ps.setNull(5, Types.OTHER);
                } else {
                    ps.setObject(5, provider.getGender().toString(), Types.OTHER);
                }
                ps.setObject(6, provider.getBirthDate(), Types.DATE);
                ps.setLong(7, provider.getEmployeeId());

                if(ps.executeUpdate() != 1)  {
                    con.rollback();
                    return Optional.empty();
                }
                con.commit();
        } catch (SQLException exception) {
                exception.printStackTrace();
            throw new SQLRuntimeException(exception);
        }
        return Optional.of(provider);
    }

    /**
     * Удалить объект из БД
     * @param id параметр запроса
     * при перехвате SQLException генерируется SQLRuntimeException
     */
    @Override
    public void deleteEmployee(Long id) {
        String selectByIdQuery = "DELETE FROM employee WHERE employee_id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(selectByIdQuery)) {
            ps.setLong(1, id);
            ps.executeUpdate();
            con.commit();
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new SQLRuntimeException(exception);
        }
    }

}
