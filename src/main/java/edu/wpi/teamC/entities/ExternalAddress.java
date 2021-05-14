package edu.wpi.teamC.entities;

public class ExternalAddress {

    private int addressID;
    private String street;
    private String town;
    private String state;
    private int zipCode;

    public ExternalAddress(String street, String town, String state, int zipCode){
        this.state = street;
        this.town = town;
        this.state = state;
        this.zipCode = zipCode;
    }

    public int getAddressID() {
        return addressID;
    }

    public void setAddressID(int addressID) {
        this.addressID = addressID;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

}
