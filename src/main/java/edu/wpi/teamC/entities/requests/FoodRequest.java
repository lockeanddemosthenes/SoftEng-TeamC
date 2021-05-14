package edu.wpi.teamC.entities.requests;

public class FoodRequest {
    int requestID;
    String username;
    String name;
    String food;
    String side;
    String drink;
    String location;
    String assignTo;
    String isComplete;
    String date;
    String time;
    String specialInstructions;

    public FoodRequest(int requestID, String username, String name, String food, String side, String drink, String location, String assignTo, String isComplete, String date, String time, String specialInstructions) {
        this.requestID = requestID;
        this.username = username;
        this.name = name;
        this.food = food;
        this.side = side;
        this.drink = drink;
        this.location = location;
        this.assignTo = assignTo;
        this.isComplete = isComplete;
        this.date = date;
        this.time = time;
        this.specialInstructions = specialInstructions;
    }

    public FoodRequest(int requestID, String username, String name, String food, String side, String drink, String location, String assignTo, String isComplete, String date, String time) {
        this.requestID = requestID;
        this.username = username;
        this.name = name;
        this.food = food;
        this.side = side;
        this.drink = drink;
        this.location = location;
        this.assignTo = assignTo;
        this.isComplete = isComplete;
        this.date = date;
        this.time = time;
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

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getDrink() {
        return drink;
    }

    public void setDrink(String drink) {
        this.drink = drink;
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

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }
}
