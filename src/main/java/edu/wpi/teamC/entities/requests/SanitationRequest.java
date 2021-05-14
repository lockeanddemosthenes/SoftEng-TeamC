package edu.wpi.teamC.entities.requests;

public class SanitationRequest
{

    private int serviceID;
    private String email;
    private String description;
    private String location;
    private String assignTo;
    private String isComplete;

    public SanitationRequest(int serviceID, String email, String description, String location, String assignTo, String isComplete) {
        this.serviceID = serviceID;
        this.email = email;
        this.description = description;
        this.location = location;
        this.assignTo = assignTo;
        this.isComplete = isComplete;
    }

    public int getServiceID() {
        return serviceID;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAssignTo() {
        return assignTo;
    }

    public void setAssignTo(String assignTo) {
        this.assignTo = assignTo;
    }

    public String getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(String isComplete) {
        this.isComplete = isComplete;
    }

}
