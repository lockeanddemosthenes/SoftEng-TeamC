package edu.wpi.teamC.entities.requests;

public class MaintenanceRequest {
    int requestID;
    String userName;
    String name;
    String location;
    String fulfilledBy;
    String summary;
    String isComplete;


    public MaintenanceRequest(int requestID, String userName, String name, String location, String fulfilledBy, String summary, String isComplete) {
        this.requestID = requestID;
        this.userName = userName;
        this.name = name;
        this.location = location;
        this.fulfilledBy = fulfilledBy;
        this.summary = summary;
        this.isComplete = isComplete;
    }

    public MaintenanceRequest(int requestID, String userName, String name, String location, String fulfilledBy, String summary) {
        this.requestID = requestID;
        this.userName = userName;
        this.name = name;
        this.location = location;
        this.fulfilledBy = fulfilledBy;
        this.summary = summary;
        this.isComplete = "False";
    }

    public int getRequestID() {
        return requestID;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFulfilledBy() {
        return fulfilledBy;
    }

    public void setFulfilledBy(String fulfilledBy) {
        this.fulfilledBy = fulfilledBy;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(String isComplete) {
        this.isComplete = isComplete;
    }
}
