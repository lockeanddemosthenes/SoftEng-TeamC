package edu.wpi.teamC.entities.mapEditor;


import java.util.LinkedList;

public class Node {
    //node ID and parameters
    private String ID;  //ID is the most important, it will be used to differentiate nodes
    private int xcoord;//x coordinate of node
    private int ycoord;//y coordinate
    private String floor;//floor, not implemented yet
    private String building;//building, not implemented yet
    private String nodeType;
    private String longName;

    private Double distance = Double.MAX_VALUE;

    private LinkedList<Edge> connectedEdges;


    private String shortName;

    public Node() {
        this.ID = "";
        this.xcoord = 0;
        this.ycoord = 0;
        this.floor = "";
        this.building = "";
        this.nodeType = "";
        this.longName = "";
        this.shortName = "";
        connectedEdges = new LinkedList<>();
    }

    public Node(String n, int xcoord, int ycoord, String floor, String building, String nodeType, String longName, String shortName) {
        this.ID = n;
        this.xcoord = xcoord;
        this.ycoord = ycoord;
        this.floor = floor;
        this.building = building;
        this.nodeType = nodeType;
        this.longName = longName;
        this.shortName = shortName;
        connectedEdges = new LinkedList<>();

    }

    //constructor used to search for matching nodes
    public Node(String ID) {
        this.ID = ID;
    }

    @Override
    public String toString() {
        return getID() + " Node" + " x:" + getXcoord() + " y:" + getYcoord();
    }

    //pythagorean's theorem
    public double distanceTo(Node node2) {
        if(this.getFloor().equals(node2.getFloor()))
            return Math.sqrt(Math.pow(getXcoord() - node2.getXcoord(), 2) + Math.pow(getYcoord() - node2.getYcoord(), 2));
        else
            return 500+Math.sqrt(Math.pow(getXcoord() - node2.getXcoord(), 2) + Math.pow(getYcoord() - node2.getYcoord(), 2));
    }
    public double rawDistanceTo(Node node2) {

            return Math.sqrt(Math.pow(getXcoord() - node2.getXcoord(), 2) + Math.pow(getYcoord() - node2.getYcoord(), 2));
    }

    //use ID to determine if a node is equal to another node
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Node)) {
            return false;
        }
        Node check = (Node) obj;
        return check.getID().equals(this.getID());
    }
    public boolean equalsLongName(Node n){
        return n.getLongName().equals(this.getLongName());
    }

    public boolean equalsShortName(Node n){
        return n.getShortName().equals(this.getShortName());
    }

    public void addEdge(Edge e) {
        connectedEdges.add(e);
    }

    public void removeEdge(Edge e) {
        connectedEdges.remove(e);
    }

    public boolean hasEdges() {
        return connectedEdges!=null;
    }

    public LinkedList<Edge> getEdges() {
        return connectedEdges;
    }

    public void setEdges(LinkedList<Edge> edges) {
        this.connectedEdges = edges;
    }


    public String getFloor() {
        return floor;
    }

    public String getBuilding() {
        return building;
    }

    public String getNodeType() {
        return nodeType;
    }

    public String getLongName() {
        return longName;
    }

    public String getShortName() {
        return shortName;
    }

    public String getID() {
        return ID;
    }

    public int getXcoord() {
        return xcoord;
    }

    public int getYcoord() {
        return ycoord;
    }

    public void setXcoord(int xcoord) {
        this.xcoord = xcoord;
    }

    public void setYcoord(int ycoord) {
        this.ycoord = ycoord;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Double getDistance() { return distance; }

    public void setDistance(Double distance) { this.distance = distance; }

}