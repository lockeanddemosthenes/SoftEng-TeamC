package edu.wpi.teamC.entities.requests;

import edu.wpi.teamC.entities.ExternalAddress;

import java.sql.SQLException;

public class CovidSurvey {
    int surveyID;
    String patientName;
    String userName;
    String positiveTest;
    String symptoms;
    String closeContact;
    String selfIsolate;
    String feelGood;
    String receivedVaccine;
    String entryType;
    String assignTo;
    String isClear;

    public CovidSurvey() {};

    public CovidSurvey(String patientName, String userName, String positiveTest, String symptoms, String closeContact, String selfIsolate, String feelGood, String receivedVaccine) {
        this.patientName = patientName;
        this.userName = userName;
        this.positiveTest = positiveTest;
        this.symptoms = symptoms;
        this.closeContact = closeContact;
        this.selfIsolate = selfIsolate;
        this.feelGood = feelGood;
        this.receivedVaccine = receivedVaccine;
        this.assignTo = "hgsmith";
    }

    public CovidSurvey(int surveyID, String patientName, String userName, String positiveTest, String symptoms, String closeContact, String selfIsolate, String feelGood, String receivedVaccine,  String assignTo, String entryType, String isClear) {
        this.surveyID = surveyID;
        this.patientName = patientName;
        this.userName = userName;
        this.positiveTest = positiveTest;
        this.symptoms = symptoms;
        this.closeContact = closeContact;
        this.selfIsolate = selfIsolate;
        this.feelGood = feelGood;
        this.receivedVaccine = receivedVaccine;
        this.entryType = entryType;
        this.assignTo = assignTo;
        this.isClear = isClear;
    }


    public int getSurveyID() {
        return surveyID;
    }

    public void setSurveyID(int surveyID) {
        this.surveyID = surveyID;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPositiveTest() {
        return positiveTest;
    }

    public void setPositiveTest(String positiveTest) {
        this.positiveTest = positiveTest;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getCloseContact() {
        return closeContact;
    }

    public void setCloseContact(String closeContact) {
        this.closeContact = closeContact;
    }

    public String getSelfIsolate() {
        return selfIsolate;
    }

    public void setSelfIsolate(String selfIsolate) {
        this.selfIsolate = selfIsolate;
    }

    public String getFeelGood() {
        return feelGood;
    }

    public void setFeelGood(String feelGood) {
        this.feelGood = feelGood;
    }

    public String getReceivedVaccine() {
        return receivedVaccine;
    }

    public void setReceivedVaccine(String receivedVaccine) {
        this.receivedVaccine = receivedVaccine;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public String getAssignTo() {
        return assignTo;
    }

    public void setAssignTo(String assignTo) {
        this.assignTo = assignTo;
    }

    public String getIsClear() {
        return isClear;
    }

    public void setIsClear(String isClear) {
        this.isClear = isClear;
    }
}
