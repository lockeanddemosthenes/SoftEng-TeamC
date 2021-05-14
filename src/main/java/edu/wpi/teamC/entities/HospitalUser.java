package edu.wpi.teamC.entities;

public abstract class HospitalUser {

    String userName;
    String userEmail;
    String password;
    String firstName;
    String lastName;

    public HospitalUser(String userName, String userEmail, String password, String firstName, String lastName) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public HospitalUser() {}
}
