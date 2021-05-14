package edu.wpi.teamC.entities.requests;

public class InterpreterRequest {
    int requestID;
    String userName;
    String requestName;
    String language;
    String location;
    String description;
    String fulfilledBy;
    String isComplete;

    public InterpreterRequest(int requestID, String userName, String requestName, String language, String location, String description, String fulfilledBy, String isComplete) {
        this.requestID = requestID;
        this.userName = userName;
        this.requestName = requestName;
        this.language = language;
        this.location = location;
        this.description = description;
        this.fulfilledBy = fulfilledBy;
        this.isComplete = isComplete;
    }

    public InterpreterRequest(int requestID, String userName, String requestName, String language, String location, String description, String fulfilledBy) {
        this.requestID = requestID;
        this.userName = userName;
        this.requestName = requestName;
        this.language = language;
        this.location = location;
        this.description = description;
        this.fulfilledBy = fulfilledBy;
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

    public String getRequestName() {
        return requestName;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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

    public void setIsComplete(String isComplete) {
        this.isComplete = isComplete;
    }
}
