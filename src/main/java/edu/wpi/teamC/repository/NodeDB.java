package edu.wpi.teamC.repository;

import edu.wpi.teamC.entities.mapEditor.mapEditorSystem;
import javafx.scene.control.TreeItem;
import edu.wpi.teamC.repository.GraphDB;

import java.util.ArrayList;

public class NodeDB {
    public String nodeID;
    public String xCoord;
    public String yCoord;
    public String floor;
    public String building;
    public String nodeType;
    public String longName;
    public String shortName;
    private ArrayList<GraphDB> graphs;

    public NodeDB(String nodeID, String xCoord, String yCoord, String floor, String building, String nodeType, String longName, String shortName) {
        this.nodeID = nodeID;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.floor = floor;
        this.building = building;
        this.nodeType = nodeType;
        this.longName = longName;
        this.shortName = shortName;
        this.graphs = new ArrayList<GraphDB>();
    }

    public NodeDB() {
    }

    public String getNodeID() {
        return nodeID;
    }

    public String getXCoord() {
        return xCoord;
    }

    public String getYCoord() {
        return yCoord;
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

    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
    }

    public void setxCoord(String xCoord) {
        this.xCoord = xCoord;
    }

    public void setyCoord(String yCoord) {
        this.yCoord = yCoord;
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

    public ArrayList<GraphDB> getGraphs() {
        return graphs;
    }
    public void addGraph(GraphDB graph) {
        if (!graphs.contains(graph)) {
            graphs.add(graph);
        }
        if (!graph.getNodes().contains(this)) {
            graph.addNode(this);
        }
    }

    @Override
    public String toString() {
        return this.getShortName();
    }
}


