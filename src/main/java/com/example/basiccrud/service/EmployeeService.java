package com.example.basiccrud.service;

import com.example.basiccrud.dtos.AllEmployeesDto;
import com.example.basiccrud.dtos.EmployeeDto;

public interface EmployeeService {

    //add Employee
    EmployeeDto saveEmployee(EmployeeDto employee);

    //getAllEmployees
    AllEmployeesDto getAllEmployees();

    //get employee by Id
    EmployeeDto getEmployeeById(String searchId);

    //update employee by Id
    EmployeeDto updateEmployee(EmployeeDto employee, String searchId);

    //Delete employee by Id
    void deleteEmployeeById(String searchId);

    //Delete all employees
    void deleteAllEmployees();

}
