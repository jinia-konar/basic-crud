package com.example.basiccrud.repository;

import com.example.basiccrud.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface EmployeeRepo extends JpaRepository<Employee, Long> {

    @Query("SELECT e FROM Employee e WHERE e.searchId = :searchID")
    public Employee findEmployeeBySearchId(@Param("searchID") String searchId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Employee WHERE searchId = :searchID")
    public void deleteEmployeeBySearchId(@Param("searchID") String searchId);
}
