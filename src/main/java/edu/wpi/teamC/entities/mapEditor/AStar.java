package edu.wpi.teamC.entities.mapEditor;

import java.util.HashMap;
import java.util.LinkedList;

public class AStar implements ISearch{

    //The A* algorithm.  Uses A* methodology to efficiently find the path between
    //nodes.
    //start:  The node that the pathfinding will start at
    //goal:  The node that the pathfinding will finish at
    public MultiFloorPath search(Node start, Node goal,boolean isEmergency) {
        HashMap<Node, Double> frontiers = new HashMap<>();
        //a hashmap of the nodes that can be traveled to in the next step.
        //contains <Node, cost to travel to said Node>
        HashMap<Node, Node> cameFrom = new HashMap<>();
        //Hashmap which contains the path by the end of the A* algorithm.
        //format is <Node, Prior Node in the A* algorithm>
        Path path = new Path();
        //path which will eventually hold the sequence of Nodes to complete the navigation
        HashMap<Node, Double> costSoFar = new HashMap<>();
        //For each node, stores the cost that it took to travel to each node.
        boolean foundGoal = false;
        //Flag that stores whether the path is valid to return or not
        Node current = start;
        frontiers.put(start, 0.0);
        costSoFar.put(start, 0.0);
        while (!frontiers.isEmpty()) {

            current = NavAlgs.getMinKey(frontiers);//explore the node of the lowest cost
            frontiers.remove(current);//do not consider this node for exploration in the future
            if (current == goal) {//If we are done, break from the loop and extract the path

                foundGoal = true;
                break;

            }

            LinkedList<Edge> edges = current.getEdges();//edges is linkedlist of edges contained in node
            for (Edge edge : edges) {

                Node next = edge.getDestinationNode();//get the node that each edge points to
                double newCost = costSoFar.get(current) + edge.getCost();//cost to get to node is cost of current+ cost of travel
                if ((!costSoFar.containsKey(next)) || (newCost < costSoFar.get(next))) {//if cost for this node is currently
                    boolean emergencyRoute=(next.getNodeType().equals("EMER"));
                    boolean noEmergencyRoute=next.getNodeType().equals("EXIT");
                    if((emergencyRoute&&isEmergency)||(noEmergencyRoute&&!isEmergency)||(!noEmergencyRoute&&!emergencyRoute)) {
                        //unknown or higher than newly discovered path

                        costSoFar.put(next, newCost); //put the new cost of travel into the next node
                        double priority = newCost + NavAlgs.projectCost(goal, next); //priority considers cost of travel AND
                        //estimated cost to get to goal(calculated with distance

                        frontiers.put(next, priority);//store the composite cost into the frontier list
                        cameFrom.put(next, current);//note that the next node was navigated to through the current node
                    }
                }
            }
        }
        Node trackBack = current;//now to extract the path, goal=current nominally
        if (foundGoal) {          //if the path is valid from start-> finish
            while (trackBack != start) {//until the start is reached
                path.addNode(trackBack);   //we check where each node came from, gives fastest path from goal to
                //start
                trackBack = cameFrom.get(trackBack);
            }
            path.addNode(start);
            path = path.reverse();          //now reverse the path so that the path goes start->goal

        }

        return new MultiFloorPath(path);
    }
}
