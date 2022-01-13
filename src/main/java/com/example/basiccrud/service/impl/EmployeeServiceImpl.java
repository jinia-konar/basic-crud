package com.example.basiccrud.service.impl;

import com.example.basiccrud.dtos.AllEmployeesDto;
import com.example.basiccrud.dtos.EmployeeDto;
import com.example.basiccrud.model.Employee;
import com.example.basiccrud.repository.EmployeeRepo;
import com.example.basiccrud.service.EmployeeService;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    // No need to auto wired beacuse if this is passed in constructor so this will
    // be automatically used as constructor
    private EmployeeRepo employeeRepo;
    private HashOperations hashOperations;
    private RedisTemplate redisTemplate;

    private String redisKey = "EMPLOYEE";

    public EmployeeServiceImpl(EmployeeRepo employeeRepo, RedisTemplate redisTemplate) {
        this.employeeRepo = employeeRepo;
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public EmployeeDto saveEmployee(EmployeeDto employee) {
        Employee emp = employeeRepo.save(setNewEmployeeEntity(employee));
        hashOperations.put(redisKey, emp.getSearchId(), emp);
        redisTemplate.expire(redisKey, 60, TimeUnit.SECONDS);
        System.out.println(emp);
        return setNewEmployeeDto(emp);
    }

    @Override
    public AllEmployeesDto getAllEmployees() {

        Map<String, Employee> employeeMap = hashOperations.entries(redisKey);

        List<EmployeeDto> employeeDtoList = new ArrayList<EmployeeDto>();
        if (employeeMap.isEmpty()) {
            List<Employee> employeeList = employeeRepo.findAll();
            employeeList.forEach((emp) -> {
                System.out.println(emp);
                employeeDtoList.add(setNewEmployeeDto(emp));
            });
            System.out.println("It is from DB");
        /*for (Employee employee : employeeRepo.findAll()) {
            System.out.println(employee);
            employeeDtoList.add(setEmployeeDto(employee));
        }*/
        } else {
            employeeMap.forEach((key, value) -> {
                employeeDtoList.add(setNewEmployeeDto(value));
            });
            System.out.println("It is from redis");
        }

        AllEmployeesDto allEmployeesDto = new AllEmployeesDto(employeeDtoList);
        return allEmployeesDto;
    }

    @Override
    public EmployeeDto getEmployeeById(String searchId) {
        Employee redisEmp = (Employee) hashOperations.get(redisKey, searchId);
        if (redisEmp != null) {
            System.out.println("It is from redis");
            return setNewEmployeeDto(redisEmp);
        }
        else {
            Employee emp = employeeRepo.findEmployeeBySearchId(searchId);/*.orElseThrow(() ->
                new ResourceNotFoundExcp("Employee", "id", id));*/
            System.out.println("It is from DB");
            return setNewEmployeeDto(emp);
        }
    }

    @Override
    public EmployeeDto updateEmployee(EmployeeDto employee, String searchId) {
        System.out.println("parameter value: " + employee + ", " +  searchId);
        Employee existingEmployee = employeeRepo.findEmployeeBySearchId(searchId);/*.orElseThrow(() ->
                new ResourceNotFoundExcp("Employee", "id", id));*/
        System.out.println("original value: " + existingEmployee + ", " +  existingEmployee.getSearchId());

        String _firstName = employee.getFirstName() == null ? existingEmployee.getFirstName() : employee.getFirstName();
        String _lastName = employee.getLastName() == null ? existingEmployee.getLastName() : employee.getLastName();
        String _email = employee.getEmail() == null ? existingEmployee.getEmail() : employee.getEmail();
        String _searchID = _firstName.concat("-").concat(_lastName);

        existingEmployee.setFirstName(_firstName);
        existingEmployee.setLastName(_lastName);
        existingEmployee.setEmail(_email);
        existingEmployee.setSearchId(_searchID);
        System.out.println("Updated value: " + existingEmployee + ", " +  existingEmployee.getSearchId());

        //TODO: Need to optimize this
        //deleteEmployeeById(searchId);
        System.out.println("DB value: " + existingEmployee + ", " +  existingEmployee.getSearchId());
        hashOperations.delete(redisKey, searchId);
        EmployeeDto emp = saveEmployee(setNewEmployeeDto(existingEmployee)); /*employeeRepo.save(existingEmployee);*/
        return emp;
    }

    @Override
    public void deleteEmployeeById(String searchId) {
        employeeRepo.findEmployeeBySearchId(searchId);/*.orElseThrow(() ->
                new ResourceNotFoundExcp("Employee", "id", id));*/
        employeeRepo.deleteEmployeeBySearchId(searchId);
        hashOperations.delete(redisKey, searchId);
    }

    @Override
    public void deleteAllEmployees() {
        employeeRepo.deleteAll();
    }

    private Employee setNewEmployeeEntity(EmployeeDto employeeDto) {
        Employee empEntity = new Employee();
        empEntity.setFirstName(employeeDto.getFirstName());
        empEntity.setLastName(employeeDto.getLastName());
        empEntity.setEmail(employeeDto.getEmail());
        empEntity.setSearchId(employeeDto.getFirstName()+"-"+employeeDto.getLastName());

        return empEntity;
    }

    private EmployeeDto setNewEmployeeDto(Employee employee) {
        EmployeeDto empDto = new EmployeeDto();
        System.out.println("employeeDto value: " + employee + ", " +  employee.getSearchId());
        empDto.setFirstName(employee.getFirstName());
        empDto.setLastName(employee.getLastName());
        empDto.setEmail(employee.getEmail());
        System.out.println("employeeDto after value: " + empDto);

        return empDto;
    }
}
