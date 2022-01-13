package com.example.basiccrud.controller;

import com.example.basiccrud.dtos.AllEmployeesDto;
import com.example.basiccrud.dtos.EmployeeDto;
import com.example.basiccrud.dtos.RmqUpdateDto;
import com.example.basiccrud.service.EmployeeService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    private final KafkaTemplate<String, RmqUpdateDto> kafkaTemplate;

    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService, RabbitTemplate rabbitTemplate, KafkaTemplate kafkaTemplate) {
        super();
        this.employeeService = employeeService;
        this.rabbitTemplate = rabbitTemplate;
        this.kafkaTemplate = kafkaTemplate;
    }

    static final String topicExchangeName = "spring-boot-exchange";

    // Add Emplopyee
    @PostMapping()
    public ResponseEntity<EmployeeDto> addEmployee(@RequestBody EmployeeDto employee) {
        System.out.println(employee);
        return new ResponseEntity<EmployeeDto>(employeeService.saveEmployee(employee), HttpStatus.CREATED);
    }

    // Get All Employees
    @GetMapping("/all")
    public ResponseEntity<AllEmployeesDto> getAllEmployees() {
        return new ResponseEntity<AllEmployeesDto>(employeeService.getAllEmployees(), HttpStatus.OK);
    }

    // Get Employee by Email
    @GetMapping("{searchId}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable("searchId") String searchId) {
        return new ResponseEntity<EmployeeDto>(employeeService.getEmployeeById(searchId), HttpStatus.OK);
    }

    // Update Employee by Email
    @PutMapping("{searchId}")
    public ResponseEntity<String> updateEmployeeById(@PathVariable("searchId") String searchId, @RequestBody EmployeeDto employee) {
        System.out.println("update emp: " + employee.getEmail());
        RmqUpdateDto rmqUpdateDto = new RmqUpdateDto(employee, searchId);
        //Way 3:
        kafkaTemplate.send(topicExchangeName, rmqUpdateDto);
        //Way 2: rabbitTemplate.convertAndSend(topicExchangeName, "foo.bar.baz", rmqUpdateDto);
        //Way 1: employeeService.updateEmployee(employee, searchId);
        return new ResponseEntity<String>("Update successful :D", HttpStatus.OK);
    }

    // Delete Employee by Email
    @DeleteMapping("{searchId}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable("searchId") String searchId) {
        employeeService.deleteEmployeeById(searchId);
        return new ResponseEntity<String>("Employee deleted successfully with id: " + searchId, HttpStatus.OK);
    }

    // Delete All Employees
    @DeleteMapping()
    public ResponseEntity<String> deleteAllEmployees() {
        employeeService.deleteAllEmployees();
        return new ResponseEntity<String>("Successfully deleted all employees", HttpStatus.OK);
    }
}