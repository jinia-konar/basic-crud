package com.example.basiccrud.dtos;

import com.example.basiccrud.model.Employee;
import lombok.Data;

import javax.persistence.Entity;
import java.util.List;

@Data
public class AllEmployeesDto {

    public AllEmployeesDto(List<EmployeeDto> listOfEmployees) {
        this.listOfEmployees = listOfEmployees;
    }

    private List<EmployeeDto> listOfEmployees;
}
