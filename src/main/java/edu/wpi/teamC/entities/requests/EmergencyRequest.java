package edu.wpi.teamC.entities.requests;

import edu.wpi.teamC.views.LoginController;

public class EmergencyRequest {

    int requestID;
    String username;
    String name;
    String location;
    String assignTo;
    String summary;
    String isComplete;
    String emergencyType;

    public EmergencyRequest(int requestID,String username, String name, String location, String assignTo, String summary, String isComplete, String emergencyType) {
        this.requestID = requestID;
        this.username = username;
        this.name = name;
        this.location = location;
        this.assignTo = assignTo;
        this.summary = summary;
        this.isComplete = isComplete;
        this.emergencyType = emergencyType;
    }

    public String getEmergencyType() {
        return emergencyType;
    }

    public void setEmergencyType(String emergencyType) {
        this.emergencyType = emergencyType;
    }

    public int getRequestID() {
        return requestID;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getAssignTo() {
        return assignTo;
    }

    public void setAssignTo(String assignTo) {
        this.assignTo = assignTo;
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
