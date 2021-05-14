package edu.wpi.teamC.entities.requests;

public class InternalPatientRequest {
     int requestID;
     String userName;
     String patientID;
     String location;
     String destination;
     String transportType;
     String time;
     String transportReason;
     String isComplete;
     String fulfilledBy;

    public InternalPatientRequest(int requestID, String userName, String patientID, String location, String destination, String transportType, String time, String transportReason, String isComplete, String fulfilledBy) {
        this.requestID = requestID;
        this.userName = userName;
        this.patientID = patientID;
        this.location = location;
        this.destination = destination;
        this.transportType = transportType;
        this.time = time;
        this.transportReason = transportReason;
        this.isComplete = isComplete;
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

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTransportReason() {
        return transportReason;
    }

    public void setTransportReason(String transportReason) {
        this.transportReason = transportReason;
    }

    public String getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(String isComplete) {
        this.isComplete = isComplete;
    }

    public String getFulfilledBy() {
        return fulfilledBy;
    }

    public void setFulfilledBy(String fulfilledBy) {
        this.fulfilledBy = fulfilledBy;
    }
}
