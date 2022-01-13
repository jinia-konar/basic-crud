package com.example.basiccrud.dtos;

import lombok.Data;

@Data
public class RmqUpdateDto {

    public RmqUpdateDto(){}

    public RmqUpdateDto(EmployeeDto employeeDto, String searchId) {
        this.employeeDto = employeeDto;
        this.searchId = searchId;
    }

    private EmployeeDto employeeDto;
    private String searchId;
}
