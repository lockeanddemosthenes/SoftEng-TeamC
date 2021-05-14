package edu.wpi.teamC.entities.requests;

public class MedicineDeliveryRequest {
    int requestID;
    String userName;
    String patientID;
    String medication;
    String dosage;
    String deliveryTime;
    String deliveryDate;
    String instructions;
    String location;
    String fulfilledBy;
    String isComplete;

    public MedicineDeliveryRequest(int requestID, String userName, String patientID, String medication, String dosage, String deliveryTime, String deliveryDate, String instructions, String location, String fulfilledBy, String isComplete) {
        this.requestID = requestID;
        this.userName = userName;
        this.patientID = patientID;
        this.medication = medication;
        this.dosage = dosage;
        this.deliveryTime = deliveryTime;
        this.deliveryDate = deliveryDate;
        this.instructions = instructions;
        this.location = location;
        this.fulfilledBy = fulfilledBy;
        this.isComplete = isComplete;
    }

    public MedicineDeliveryRequest(int requestID, String userName, String patientID, String medication, String dosage, String deliveryTime, String deliveryDate, String instructions, String location, String fulfilledBy) {
        this.requestID = requestID;
        this.userName = userName;
        this.patientID = patientID;
        this.medication = medication;
        this.dosage = dosage;
        this.deliveryTime = deliveryTime;
        this.deliveryDate = deliveryDate;
        this.instructions = instructions;
        this.location = location;
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

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getMedication() {
        return medication;
    }

    public void setMedication(String medication) {
        this.medication = medication;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
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

    public String getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(String isComplete) {
        this.isComplete = isComplete;
    }
}
