package edu.wpi.teamC.entities;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import java.util.ArrayList;

public class Employee extends RecursiveTreeObject<Employee> {
    String userName;
    String userEmail;
    String password;
    String firstName;
    String lastName;

    public Employee(String userName, String userEmail, String pass, String first, String last) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.password = pass;
        this.firstName = first;
        this.lastName = last;
    }

    public Employee() {
        this.userName = "";
        this.userEmail = "";
        this.password = "";
        this.firstName = "";
        this.lastName = "";
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}


