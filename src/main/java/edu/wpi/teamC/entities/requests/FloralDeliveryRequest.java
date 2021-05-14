package edu.wpi.teamC.entities.requests;

public class FloralDeliveryRequest {
    int requestID;
    String userName;
    String location;
    String patientName;
    String flowerType;
    String flowerNum;
    String notes;
    String fulfilledBy;
    String isComplete;

    public FloralDeliveryRequest(int requestID, String userName, String location, String patientName, String flowerType, String flowerNum, String notes, String fulfilledBy, String isComplete) {
        this.requestID = requestID;
        this.userName = userName;
        this.location = location;
        this.patientName = patientName;
        this.flowerType = flowerType;
        this.flowerNum = flowerNum;
        this.notes = notes;
        this.fulfilledBy = fulfilledBy;
        this.isComplete = isComplete;
    }

    public FloralDeliveryRequest(int requestID, String userName, String location, String patientName, String flowerType, String flowerNum, String notes, String fulfilledBy) {
        this.requestID = requestID;
        this.userName = userName;
        this.location = location;
        this.patientName = patientName;
        this.flowerType = flowerType;
        this.flowerNum = flowerNum;
        this.notes = notes;
        this.fulfilledBy = fulfilledBy;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getFlowerType() {
        return flowerType;
    }

    public void setFlowerType(String flowerType) {
        this.flowerType = flowerType;
    }

    public String getFlowerNum() {
        return flowerNum;
    }

    public void setFlowerNum(String flowerNum) {
        this.flowerNum = flowerNum;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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
