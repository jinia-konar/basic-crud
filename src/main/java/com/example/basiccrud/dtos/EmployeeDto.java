package com.example.basiccrud.dtos;

import com.example.basiccrud.model.Employee;
import lombok.Data;

import java.util.List;

@Data
public class EmployeeDto {

    public EmployeeDto(){} //TODO: Why default constructor need here but not in AllEmployeesDto

    public EmployeeDto(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    private String firstName = null;
    private String lastName = null;
    private String email = null;
}
