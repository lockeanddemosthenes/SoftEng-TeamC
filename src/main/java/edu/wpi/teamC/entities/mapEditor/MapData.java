package edu.wpi.teamC.entities.mapEditor;

import edu.wpi.teamC.repository.DBAccess;
import edu.wpi.teamC.repository.EdgeDB;
import edu.wpi.teamC.views.mapEditor.MapDisplay;
import javafx.scene.control.Button;
import javafx.scene.shape.Line;

import java.sql.*;
import java.util.LinkedList;

public class MapData {




    private boolean editMode;


    private Node parkingSpot;
    private Node lastDest;
    private boolean showEdgeMode;
    private Node addEdgeFrom;
    private Graph hospitalGraph;
    private MapDisplay mapDisplay;
    private LinkedList<Node> selection;
    private boolean isEmergency;

    protected LinkedList<String> navTypes;



    Transforms transforms;

    private String startId;
    private String endId;
    private boolean isSelectingNodes;



    private String currentFloor;
    private String currentType;
    private DBAccess dbAccess;



    private boolean addEdgeMode;

    public MapData(MapDisplay m,DBAccess dbAccess,boolean isEmergency) {
        //todo:MAKE THIS LOAD FROM DATABASE USING initHospitalGraph()
        System.out.println("constructor for mapdata");
        hospitalGraph=Graph.getInstance();
        this.dbAccess=dbAccess;
        initHospitalMapDB();
        mapDisplay=m;
        isSelectingNodes=false;
        addEdgeMode=false;
        selection=new LinkedList<>();
        this.isEmergency=isEmergency;
    }

    public void setEditMode(boolean b) {
        editMode =b;
        if(editMode)
            clearReturn();
    }
    public boolean isEditMode() {
        return editMode;
    }

    public void setImageFields(int origWidth,int origHeight, double offsetX,double offsetY, double imageWidth,double imageHeight){
        transforms=Transforms.get_Instance(origWidth,origHeight,offsetX,offsetY,imageWidth,imageHeight);
    }
    //todo:rewrite this with the new info
    public void initHospitalMapDB(){
        hospitalGraph.clear();
        for(Node n:getDBNodes()) {
            if(hospitalGraph.getNode(n.getID())==null)
                hospitalGraph.addNode(n);
        }

        for(Edge e:getDBEdges()){
            hospitalGraph.addConnection(e,true);

        }


    }

    //todo:return linkedList of nodes from database using interface
    private LinkedList<Node> getDBNodes() {
        Statement stmt = null;
        LinkedList<Node> nodes=new LinkedList<>();
        try {
            nodes=new LinkedList<Node>(ObjectConversion.nodeDBToNode(dbAccess.getListOfNodes()));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return nodes;
    }
    //todo:return linkedList of edges from DB using interface
    private LinkedList<Edge> getDBEdges() {
        LinkedList<Edge> edges=new LinkedList<>();
        try {
            edges=new LinkedList<>(ObjectConversion.edgeDBToEdge(dbAccess.getListofEdges()));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return edges;
    }


    public String getStartId() {
        return startId;
    }

    public String getEndId() {
        return endId;
    }



    public boolean isNavReady(){System.out.println("Nav ready: "+!(getStartId()==null ||getEndId()==null));
        return (!(getStartId()==null ||getEndId()==null));
    }
    public boolean isReturnReady(){
        System.out.println("Parking spot:"+parkingSpot+", lastDest: "+lastDest);
        return !(parkingSpot==null||lastDest==null);
    }
    public boolean setupReturn(){
        System.out.println("setupReturn");
        setEndId(parkingSpot.getID());
        setStartId(lastDest.getID());
        return true;
    }
    public void clearReturn(){
        System.out.println("cleared parking");
        parkingSpot=null;
        lastDest=null;
    }
    public MultiFloorPath navigate(String type) {

        Node startNode;
        Node endNode;

        startNode = hospitalGraph.getNode(getStartId());
        endNode = hospitalGraph.getNode(getEndId());


        if(startNode==null ||endNode==null)
            return new MultiFloorPath();
        if(startNode.getNodeType().equals("PARK")){
            parkingSpot=startNode;
            System.out.println("parkingspot set");
        }
        lastDest=endNode;
        //determine type of pathfinding
        if(type.equals("A-Star")) {
            ISearch AStar = new AStar();
            SearchContext scAStar = new SearchContext(AStar);
            return scAStar.search(startNode, endNode,isEmergency);
        } else if(type.equals("Breadth-First")) {
            ISearch BreadthFirst = new BreadthFirst();
            SearchContext scBreadth = new SearchContext(BreadthFirst);
            return scBreadth.search(startNode, endNode,isEmergency);
        } else if(type.equals("Depth-First")) {
            ISearch DepthFirst = new DepthFirst();
            SearchContext scDepth = new SearchContext(DepthFirst);
            return scDepth.search(startNode, endNode,isEmergency);
        } else if(type.equals("Best-First")) {
            AbsSearch searchBest = new BestFirstSearch();
            return searchBest.Search(startNode, endNode,isEmergency);
        } else if(type.equals("Dijkstra")) {
            AbsSearch searchDijkstra = new DjikstraAlgorithm();
            return searchDijkstra.Search(startNode, endNode,isEmergency);
        }
        else{
            System.out.println("Invalid pathfinding algorithm selected");
            ISearch AStar = new AStar();
            SearchContext scAStar = new SearchContext(AStar);
            return scAStar.search(startNode, endNode,isEmergency);
        }
    }


    public Button nodeToButton(Node n,int size){
        double startX = transforms.transformX(n.getXcoord());
        double startY = transforms.transformY(n.getYcoord());
        Button b=new Button();
        b.setId(n.getID());
        b.setPrefSize(size,size);
        b.setMaxSize(size,size);
        b.setMinSize(size,size);
        b.setLayoutX(startX-size/2);
        b.setLayoutY(startY-size/2);
        b.setStyle("-fx-background-color: Black");
        System.out.println(b.getProperties().size());
        return b;
    }
    public Line edgeToLine(Edge e){

        double startX = e.getStartNode().getXcoord();
        double startY = e.getStartNode().getYcoord();
        double endX = e.getDestinationNode().getXcoord();
        double endY = e.getDestinationNode().getYcoord();
        int scaledStartX = transforms.transformX(startX);
        int scaledStartY = transforms.transformY(startY);
        int scaledEndX = transforms.transformX(endX);
        int scaledEndY = transforms.transformY(endY);

        Line l = new Line(scaledStartX, scaledStartY, scaledEndX,scaledEndY);
        return l;
    }
    public Edge getReverseEdge(Edge e){
        try {
            return hospitalGraph.getEdgeByNodes(e.getDestinationNode(), e.getStartNode());
        }
        catch(Exception ex){
            return e;
        }
    }
    public void deleteEdge(Edge e){
        hospitalGraph.deleteEdge(e);
    }
    public void addNode(Node n){
        hospitalGraph.addNode(n);
    }

    public void clearSelection(){
        startId=null;
        endId=null;
        mapDisplay.setStartNodeId("");
        mapDisplay.setEndNodeId("");
        mapDisplay.setMainText("Please click the start node");
    }
    public void setFloor(String newFloor){
        currentFloor=newFloor;

    }
    public LinkedList<String> getFloorList(){
        return hospitalGraph.getFloors();
    }
    public LinkedList<String> getTypeList(){
        return hospitalGraph.getTypes();
    }
    public LinkedList<Node> getTypeAndFloorList(){
        return hospitalGraph.getNodeIntersection(hospitalGraph.getListNodesByFloor(currentFloor), hospitalGraph.getListNodesByType(currentType) );
    }


    public void setStartId(String Id) {
        startId = Id;
        mapDisplay.setStartNodeId(hospitalGraph.getNode(Id).getLongName());

    }
    public void setEndId(String Id) {
        endId = Id;
        mapDisplay.setEndNodeId(hospitalGraph.getNode(Id).getLongName());



    }





    public LinkedList<Node> generateButtonInfo(){
        LinkedList<Node> transformedNodes=new LinkedList<>();
        for(Node node:hospitalGraph.getListNodesByFloor(currentFloor)){
            Node transformedNode=new Node(node.getID(),transforms.transformX(node.getXcoord()),transforms.transformY(node.getYcoord()),
                    node.getFloor(),node.getBuilding(),node.getNodeType(),node.getLongName(),node.getShortName());
            transformedNodes.add(transformedNode);
        }
        return transformedNodes;

    }
    public LinkedList<Line> getAllTransformedEdgesAsLines(){
        LinkedList<Line> lines=new LinkedList<>();
        LinkedList<String> addedIDs=new LinkedList<>();
        for(Node node:hospitalGraph.getListNodesByFloor(currentFloor)){
            for(Edge e:node.getEdges()){
                if(!addedIDs.contains(e.getID())) {
                    addedIDs.add(e.getID());
                    Line l = edgeToLine(e);
                    l.setUserData(e);
                    lines.add(l);
                }
            }
        }
        return lines;
    }
    public Node getNodeByShortName(String name) throws Exception {

            return hospitalGraph.getNodeByShortName(name);
    }
    public Node getNodeById(String Id)throws Exception{
            return hospitalGraph.getNode(Id);
    }

    public LinkedList<String> getNavTypes() {
        return navTypes;
    }
    public void setAddEdgeFrom(Node addEdgeFrom) {
        this.addEdgeFrom = addEdgeFrom;
        this.addEdgeMode=true;
    }
    public void clearAddEdgeFrom(){
        this.addEdgeMode=false;
    }
    public String getCurrentFloor() {
        return currentFloor;
    }


    public Transforms getTransforms() {
        return transforms;
    }
    public boolean isShowEdgeMode() {
        return showEdgeMode;
    }

    public void setShowEdgeMode(boolean showEdgeMode) {
        this.showEdgeMode = showEdgeMode;
    }
    public boolean isAddEdgeMode() {
        return addEdgeMode;
    }

    public Edge addEdgeTo(Node endNode) {
        System.out.println(addEdgeFrom);
        System.out.println(endNode);
        Edge e=new Edge(addEdgeFrom,endNode);

        hospitalGraph.addConnection(e,true);
        dbAccess.addEdge(new EdgeDB(e.getID(),e.getStartNode().getID(),e.getDestinationNode().getID()));
        return e;
    }

    public void addToSelection(Node node) {
        selection.add(node);
        isSelectingNodes=true;
    }

    public boolean isSelectingNodes() {
        return isSelectingNodes;
    }
    public void alignSelection(){
        double xWeight=0;
        double yWeight=0;
        double xAvg=0;
        double yAvg=0;
        for(int i=0;i<selection.size();i++){
            xAvg+=(selection.get(i).getXcoord());
            yAvg+=(selection.get(i).getYcoord());
            for(int j=i;j<selection.size();j++) {
                xWeight += Math.abs((selection.get(i).getXcoord() - selection.get(j).getXcoord()));

                yWeight += Math.abs((selection.get(i).getYcoord() - selection.get(j).getYcoord()));
            }

        }
        xAvg=xAvg/ selection.size();
        yAvg=yAvg/selection.size();
        if(xWeight<yWeight){
            for (Node n:selection) {
                n.setXcoord((int)xAvg);
                dbAccess.updateNodeX(n.getXcoord(),n.getID());

            }

        }
        else{
            for (Node n:selection) {
                n.setYcoord((int)yAvg);
                dbAccess.updateNodeY(n.getYcoord(),n.getID());
            }
        }
    }

    public void stopSelecting(){
        selection.clear();
        isSelectingNodes=false;
    }

    public void setEmergency(boolean emergency) {
        isEmergency = emergency;
    }
}

