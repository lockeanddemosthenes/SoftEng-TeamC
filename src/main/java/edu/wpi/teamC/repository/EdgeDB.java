package edu.wpi.teamC.repository;

import edu.wpi.teamC.repository.GraphDB;

import java.util.ArrayList;

public class EdgeDB {
    public String edgeID;
    public String startNode;
    public String endNode;
    private ArrayList<GraphDB> graphs;

    public EdgeDB(String edgeID, String startNode, String endNode){
        this.edgeID = edgeID;
        this.startNode = startNode;
        this.endNode = endNode;
        this.graphs = new ArrayList<GraphDB>();
    }

    public EdgeDB(){}

    public String getEdgeID() {
        return edgeID;
    }

    public void setEdgeID(String edgeID) {
        this.edgeID = edgeID;
    }

    public String getStartNode() {
        return startNode;
    }

    public void setStartNode(String startNode) {
        this.startNode = startNode;
    }

    public String getEndNode() {
        return endNode;
    }

    public void setEndNode(String endNode) {
        this.endNode = endNode;
    }

    public ArrayList<GraphDB> getGraphs() {
        return graphs;
    }
    public void addGraph(GraphDB graph) {
        if (!graphs.contains(graph)) {
            graphs.add(graph);
        }
        if (!graph.getEdges().contains(this)) {
            graph.addEdge(this);
        }
    }
}
