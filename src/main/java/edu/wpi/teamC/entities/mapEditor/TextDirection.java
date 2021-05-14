package edu.wpi.teamC.entities.mapEditor;

public class TextDirection {
    private String direction;
    private Node directionAt;
    private double angle;

    public TextDirection(String direction, Node directionAt,double absAngle) {
        this.direction = direction;
        this.directionAt = directionAt;
        this.angle=absAngle;

    }

    public String getDirection() {
        return direction;
    }

    public Node getDirectionAt() {
        return directionAt;
    }

    public double getAngle() {
        return angle;
    }
}
