package edu.wpi.teamC.repository;

import java.util.ArrayList;

public class GraphDB  {
    private Integer graphNum;
    private String userEmail;
    private ArrayList<NodeDB> nodes;
    private ArrayList<EdgeDB> edges;

    public GraphDB(Integer graphNum, String userEmail) {
        this.graphNum = graphNum;
        this.userEmail = userEmail;
        this.nodes = new ArrayList<NodeDB>();
        this.edges = new ArrayList<EdgeDB>();
    }

    public Integer getGraphNum() {
        return graphNum;
    }

    public void setGraphNum(Integer graphNum) {
        this.graphNum = graphNum;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public ArrayList<NodeDB> getNodes() {
        return nodes;
    }

    public void addNode(NodeDB node) {
        if (!nodes.contains(node)) {
            nodes.add(node);
        }
        if (!node.getGraphs().contains(this)) {
            node.addGraph(this);
        }
    }

    public ArrayList<EdgeDB> getEdges() {
        return edges;
    }

    public void addEdge(EdgeDB edge) {
        if (!edges.contains(edge)) {
            edges.add(edge);
        }
        if (!edge.getGraphs().contains(this)) {
            edge.addGraph(this);
        }
    }

}
