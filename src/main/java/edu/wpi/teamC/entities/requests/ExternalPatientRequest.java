package edu.wpi.teamC.entities.requests;

public class ExternalPatientRequest
 {

  private int serviceID;
  private String userEmail;
  private String patientName;
  private String reason;
  private String location;
  private int addressID;
  private String type;
  private String assignTo;
  private String isComplete;
  private String date;
  private String time;

    public ExternalPatientRequest(int serviceID, String userEmail, String patientName, String reason, String location, int addressID, String type, String assignTo, String isComplete, String date, String time){
     this.serviceID = serviceID;
     this.userEmail = userEmail;
     this.patientName = patientName;
     this.reason = reason;
     this.location = location;
     this.addressID = addressID;
     this.type = type;
     this.assignTo = assignTo;
     this.isComplete = isComplete;
     this.date = date;
     this.time = time;
    }

  public int getServiceID() {
   return serviceID;
  }

  public void setServiceID(int serviceID) {
   this.serviceID = serviceID;
  }

  public String getUserEmail() {
   return userEmail;
  }

  public void setUserEmail(String userEmail) {
   this.userEmail = userEmail;
  }

  public String getPatientName() {
   return patientName;
  }

  public void setPatientName(String patientName) {
   this.patientName = patientName;
  }

  public String getReason() {
   return reason;
  }

  public void setReason(String reason) {
   this.reason = reason;
  }

  public String getLocation() {
   return location;
  }

  public void setLocation(String location) {
   this.location = location;
  }

  public int getAddressID() {
   return addressID;
  }

  public void setAddressID(int addressID) {
   this.addressID = addressID;
  }

  public String getType() {
   return type;
  }

  public void setType(String type) {
   this.type = type;
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

  public String getDate() {
   return date;
  }

  public void setDate(String date) {
   this.date = date;
  }

  public String getTime() {
   return time;
  }

  public void setTime(String time) {
   this.time = time;
  }
 }
