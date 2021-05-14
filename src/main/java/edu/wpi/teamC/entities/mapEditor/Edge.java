package edu.wpi.teamC.entities.mapEditor;

public class Edge {
    private String ID;//String ID of the edge
    private Node node1;//stores the start node object
    private Node node2;//stores the end node object

    private double cost;//stores the cost to move on this node.  Default is distance but other
                //factors may be considered for pathfinding in the future
    //this constructor calculates the cost to travel based on the distance travelled
    public Edge(Node n1, Node n2){
        this.node1=n1;
        this.node2=n2;
        ID=node1.getID().concat("_").concat(node2.getID());
        this.cost=calculateCost(n1,n2);
    }
    public Edge(Node n1,Node n2,String ID){
        this.node1=n1;
        this.node2=n2;
        this.ID=ID;
        if(!(n1.getFloor()==null||n2.getFloor()==null))
            this.cost=calculateCost(n1,n2);
    }

    @Override
    public String toString() {
        return ID+" connects "+node1.toString()+ " to "+node2.toString()+", cost is "+cost;
    }
    private double calculateCost(Node n1, Node n2){
        return calculateDistance(n1,n2);
    }
    //pythagorean's theorem
    private double calculateDistance(Node n1, Node n2){
        return n1.distanceTo(n2);
    }
    //reverse the edge, return it
    public Edge reverseEdge(){
        return new Edge(this.node2,this.node1,this.ID);
    }
    //return the starting node of the edge
    public Node getStartNode(){
        return this.node1;
    }
    //return the destination node of the edge
    public Node getDestinationNode(){
        return this.node2;
    }
    public double getCost() {
        return cost;
    }
    public String getID(){
        return ID;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof Edge)) {
            return false;
        }
        Edge check = (Edge) obj;
        return check.getID().equals(this.getID());
    }
}
