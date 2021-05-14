package edu.wpi.teamC.entities.mapEditor;

import java.util.LinkedList;

public class DepthFirst implements ISearch{

    static LinkedList<Node> visited;

    public MultiFloorPath search(Node start, Node goal,boolean isEmergency) {
        visited = new LinkedList<>();
        return new MultiFloorPath(DepthFirstSearch(start, goal, new Path(), 0.0,isEmergency));
    }

    //todo: make this code neater
    //DFS searches for a given node, starting at a certain node.
    //this is a recursive algorithm.
    public Path DepthFirstSearch(Node current, Node goal, Path path, Double cost,boolean isEmergency) {
        visited.add(current);
        path.addNode(current);
        //begin to return the path here if the goal is reached
        if (current == goal) {
            return path;
        }
        //if the node has edges
        if (current.hasEdges()) {
            //get the edges as a list, loop through the edges
            LinkedList<Edge> edges = current.getEdges();
            for (Edge edge : edges) {

                //choosing the next node in the list
                Node next = edge.getDestinationNode();
                //if the node has not yet been visited
                boolean emergencyRoute = (next.getNodeType().equals("EMER"));
                boolean noEmergencyRoute = next.getNodeType().equals("EXIT");
                if ((emergencyRoute && isEmergency) || (noEmergencyRoute && !isEmergency) || (!noEmergencyRoute && !emergencyRoute)) {
                    if (!visited.contains(next)) {
                        //if the correct path has been found it is time to exit
                        if (path.contains(goal))
                            break;
                        //if the correct path has not been found, keep looking
                        path = DepthFirstSearch(next, goal, path, edge.getCost(), isEmergency);
                    }
                }
            }
        }
        //this code runs when all edges of a node have been checked
        //if the path has been found, return it
        if (path.contains(goal))
            return path;
        //if the path has not been found, this node isnt in the path
        path.removeNode(current);
        return path;
    }
}
