package edu.wpi.teamC.entities.mapEditor;

import java.util.HashMap;
import java.util.LinkedList;

public class Graph {
    private LinkedList<Node> listNodes;             //linked list which contains all of the nodes


    private static Graph graphSingleton=null;
    private Graph(){
        this.listNodes=new LinkedList<>();


    }
    private Graph(LinkedList<Node> listNodes) {
        this.listNodes = listNodes;

    }
    public static Graph getInstance(){
        if(graphSingleton==null){
            graphSingleton=new Graph();
        }
        return graphSingleton;
    }
    public static Graph getInstance(LinkedList<Node> listNodes){
        if(graphSingleton==null){
            graphSingleton=new Graph(listNodes);
        }
        return graphSingleton;
    }




    @Override
    public String toString() {
        String output = "";
        for (Node node : listNodes) {
            output = output.concat(node + "\n");
            if (node.hasEdges()) {
                LinkedList<Edge> edges = node.getEdges();


            for (Edge edge : edges) {
                output = output.concat(edge + "\n");
            }

            }

        }
        return output;
    }

    //add a given node into the graph
    //edges will need to be added after this
    public void addNode(Node newNode) {
        listNodes.add(newNode);
    }
    //retrieves a node object, given an ID as a string.
    public Node getNode(String ID){
        Node find=new Node(ID);
        if(listNodes.contains(find)) {
            for (Node search : listNodes) {
                if (find.equals(search))
                    return search;
            }
        }
       return null;
    }
    public Node getNodeByLongName(String Name)throws Exception{
        Node find=new Node("",0,0,"","","",Name,"");
        for(Node search:listNodes){
            if(find.equalsLongName(search))
                return search;
        }
        throw new Exception();
    }
    public Node getNodeByShortName(String Name)throws Exception{
        Node find=new Node("",0,0,"","","","",Name);
        for(Node search:listNodes){
            if(find.equalsShortName(search))
                return search;
        }
        throw new Exception();
    }
    public Edge getEdgeByNodes(Node start,Node end) throws Exception {
        for(Edge e:start.getEdges()){
            if(e.getDestinationNode().equals(end)){
                return e;
            }
        }
        throw new Exception();
    }
    public void deleteEdge(Edge e){
        for(Node n:getListNodes()){
            n.removeEdge(e);
        }
    }

    public LinkedList<Node> getListNodes() {
        return listNodes;
    }

    //Adds an edge into the graph.  Updates all lists.
    //Takes an edge, and a boolean for whether the edge is bidirectional
    public LinkedList<Node> getListNodesByFloor(String floor){
        LinkedList<Node> floorNodes=new LinkedList<>();
        for(Node n:getListNodes()){
            if(n.getFloor().equals(floor)){
                floorNodes.add(n);
            }
            else{
                //System.out.println(n.getFloor()+" "+floor);
            }
        }
        return floorNodes;
    }
    public LinkedList<Node> getListNodesByType(String type){
        LinkedList<Node> typeNodes=new LinkedList<>();
        for(Node n:getListNodes()){
            if(n.getNodeType().equals(type)){
                typeNodes.add(n);
            }
            else{
                //System.out.println(n.getFloor()+" "+floor);
            }
        }
        return typeNodes;
    }
    public LinkedList<Node> getNodeIntersection(LinkedList<Node> list1, LinkedList<Node> list2){
        LinkedList<Node> intersectionList=new LinkedList<>();
        for(Node item:list1){
            if( list2.contains(item)){
               intersectionList.add(item);
            }
        }
        return intersectionList;
    }
    public LinkedList<String> getFloors(){
        LinkedList<String> floorList=new LinkedList<>();
        for(Node n:getListNodes()){
            if(!floorList.contains(n.getFloor())){
                floorList.add(n.getFloor());
            }
        }
        return floorList;
    }
    public LinkedList<String> getTypes(){
        LinkedList<String> typeList=new LinkedList<>();
        for(Node n:getListNodes()){
            if(!typeList.contains(n.getNodeType())){
                typeList.add(n.getNodeType());
            }
        }
        return typeList;
    }

    public void addConnection(Edge newEdge, boolean isBidirectional) {
        Node startNode = newEdge.getStartNode();
        LinkedList<Edge> newEdges;
        if (startNode.hasEdges())
            newEdges = startNode.getEdges();
        else
            newEdges = new LinkedList<>();
        newEdges.add(newEdge);
        startNode.setEdges(newEdges);
        if (isBidirectional) {
            addConnection(newEdge.reverseEdge(), false);//if the edge is bidirectional, call the same code
                                                                    //but switch the edge nodes around
        }
    }

    public void clear() {
        listNodes=new LinkedList<Node>();
    }

    //todo: make isequal class for nodes so that the exact instance of node doesn't have
    //to be passed in
    //this is how the DFS method is called.
    //start node must be the starting node object
    //goal is the node which we aim to find

}
