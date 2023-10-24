package com.onurhaktan.springboot.service;

import com.onurhaktan.springboot.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    Employee saveEmployee(Employee employee);
    List<Employee> getAllEmployees();
    // If no employee matching the specified id is found in the database, instead of returning null, an empty Optional object is returned.
    // This prevents NullPointerException errors.
    Optional<Employee> getEmployeeById(long id);
    Employee updateEmployee(Employee updatedEmployee);
    void deleteEmployee(long id);
}
