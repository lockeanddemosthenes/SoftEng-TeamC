package edu.wpi.teamC.entities.mapEditor;

import java.util.HashMap;
import java.util.LinkedList;

public class mapEditorSystem {
    private Edge edge =null;
    private Graph graph = null;
    private Node node = null;

    public mapEditorSystem() {

    }

    //might need three functions for this? (one for edge/graph/node)
    public boolean checkIfValid() {
        if(edge == null || graph == null || node == null) {
            System.out.println("Edge/graph/node is not initialized.");
            return false;
        }
        else {
            return true;
        }
    }

    public void setEdge(Node n1, Node n2,double cost) {
        edge = new Edge(n1,n2);
        edge = new Edge(n1,n2);
    }

    public Graph getGraph(LinkedList<Node> listNodes) {
        return Graph.getInstance();
    }

    public void setNode(String n, int xcoord, int ycoord, String floor, String building, String nodeType, String longName, String shortName, String ID) {
        node = new Node(n,xcoord,ycoord,floor,building,nodeType,longName,shortName);
        node = new Node(ID);
        node = new Node();
    }

    //example (may be incorrect)
    public void addNode(Node newNode) {
        if(checkIfValid() == true) {
            graph.addNode(newNode);
        }
    }

    public Node getNode(String ID) {
        return graph.getNode(ID);
    }

    public Node getNodeByLongName(String Name) throws Exception {
        return graph.getNodeByLongName(Name);
    }

    public LinkedList<Node> getListNodes() {
        return graph.getListNodes();
    }

    public LinkedList<Node> getListNodesByFloor(String floor) {
        return graph.getListNodesByFloor(floor);
    }

    public LinkedList<String> getFloors() {
        return graph.getFloors();
    }

    public void addConnection(Edge newEdge, boolean isBidirectional) {
        graph.addConnection(newEdge, isBidirectional);
    }

    //public MultiFloorPath depthFirstSearchStart(Node start, Node goal) {
    //    return NavAlgs.depthFirstSearchStart(start, goal);
    //}


    //public MultiFloorPath AStar(Node start, Node goal) {
    //    return NavAlgs.AStar(start, goal);
    //}


    public Edge reverseEdge() {
        return edge.reverseEdge();
    }

    public Node getStartNode() {
        return edge.getStartNode();
    }

    public Node getDestinationNode() {
        return edge.getDestinationNode();
    }

    public double getCost() {
        return edge.getCost();
    }

    public double distanceTo(Node node2) {
        return node.distanceTo(node2);
    }

    public boolean equalsLongName(Node n) {
        return node.equalsLongName(n);
    }

    public void addEdge(Edge e) {
        node.addEdge(e);
    }

    public void removeEdge(Edge e) {
        node.removeEdge(e);
    }

    public boolean hasEdges() {
        return node.hasEdges();
    }

    public LinkedList<Edge> getEdges() {
        return node.getEdges();
    }

    public void setEdges(LinkedList<Edge> edges) {
        node.setEdges(edges);
    }

    public String getFloor() {
        return node.getFloor();
    }

    public String getBuilding() {
        return node.getBuilding();
    }

    public String getNodeType() {
        return node.getNodeType();
    }

    public String getLongName() {
        return node.getLongName();
    }

    public String getShortName() {
        return node.getShortName();
    }

    public String getID() {
        return node.getID();
    }

    public int getXcoord() {
        return node.getXcoord();
    }

    public int getYcoord() {
        return node.getYcoord();
    }
}
