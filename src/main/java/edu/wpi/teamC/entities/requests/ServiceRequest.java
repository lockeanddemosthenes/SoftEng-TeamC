package edu.wpi.teamC.entities.requests;

public class ServiceRequest {
    int requestID;
    String user;
    String location;
    String description;
    String fulfilledBy;
    String isComplete;

    public ServiceRequest(String username, String location, String description, String fulfilledBy, String isComplete) {
        this.user = username;
        this.location = location;
        this.description = description;
        this.fulfilledBy = fulfilledBy;
        this.isComplete = isComplete;
    }

    public ServiceRequest(String username, String location, String description, String fulfilledBy) {
        this.user = username;
        this.location = location;
        this.description = description;
        this.fulfilledBy = fulfilledBy;
        this.isComplete = "False";
    }

    public ServiceRequest() {
    }

    public ServiceRequest(int requestID, String user, String location, String description, String fulfilledBy, String isComplete) {
        this.requestID = requestID;
        this.user = user;
        this.location = location;
        this.description = description;
        this.fulfilledBy = fulfilledBy;
        this.isComplete = isComplete;
    }

    public int getRequestID() {
        return requestID;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setIsComplete(String isComplete) {
        this.isComplete = isComplete;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFulfilledBy() {
        return fulfilledBy;
    }

    public void setFulfilledBy(String fulfilledBy) {
        this.fulfilledBy = fulfilledBy;
    }

    public String getIsComplete() {
        return isComplete;
    }

    public void setComplete() {
        isComplete = "True";
    }

    public void setIncomplete() {
        isComplete = "False";
    }

}
