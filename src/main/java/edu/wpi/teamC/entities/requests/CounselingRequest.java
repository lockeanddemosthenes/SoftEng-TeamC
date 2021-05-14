package edu.wpi.teamC.entities.requests;

public class CounselingRequest {
    int requestID;
    String userName;
    String patientName;
    String counselingType;
    String location;
    String otherInfo;
    String fulfilledBy;
    String isComplete;


    public CounselingRequest(int requestID, String userName, String patientName, String counselingType, String location, String otherInfo, String fulfilledBy, String isComplete) {
        this.requestID = requestID;
        this.userName = userName;
        this.patientName = patientName;
        this.counselingType = counselingType;
        this.location = location;
        this.otherInfo = otherInfo;
        this.fulfilledBy = fulfilledBy;
        this.isComplete = isComplete;
    }

    public CounselingRequest(int requestID, String userName, String patientName, String counselingType, String location, String otherInfo, String fulfilledBy) {
        this.requestID = requestID;
        this.userName = userName;
        this.patientName = patientName;
        this.counselingType = counselingType;
        this.location = location;
        this.otherInfo = otherInfo;
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

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getCounselingType() {
        return counselingType;
    }

    public void setCounselingType(String counselingType) {
        this.counselingType = counselingType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
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
