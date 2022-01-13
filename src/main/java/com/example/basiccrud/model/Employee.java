package com.example.basiccrud.model;

import javax.persistence.*;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
@Entity
@Table(name = "employees")
public class Employee implements Serializable {

    public Employee(){}

    public Employee(String firstName, String lastName, String email, String searchId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.searchId = searchId;
    }

    @Id
    private String id = UUID.randomUUID().toString(); //TODO convert it into string if want this from user, why UUID type
    // not working and giving error "Row was updated or deleted by another transaction (or unsaved-value mapping was incorrect)"

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "searchID") //TODO: Can we ask uuid instead of this from client
    private String searchId;

    @Override
    public String toString() {
        return this.firstName + " " + this.lastName + " " + this.id + " " + this.searchId;
    }
}
