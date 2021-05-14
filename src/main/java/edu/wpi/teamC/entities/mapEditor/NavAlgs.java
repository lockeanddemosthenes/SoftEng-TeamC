package edu.wpi.teamC.entities.mapEditor;


import java.util.*;

public class NavAlgs {
    static LinkedList<Node> visited;

    //A* helper
    //Uses pythagorean's theorem to estimate the cost of travel between
    //the start and goal node.
    static double projectCost(Node start, Node goal) {
        return start.distanceTo(goal);
    }

    //A* helper.  returns the node that should be traveled to next, because it
    //has the lowest cost of travel.
    static Node getMinKey(HashMap<Node, Double> nodeCost) {
        double minimum = Double.MAX_VALUE;
        Node lowestCost = new Node();
        for (Node node : nodeCost.keySet()) {
            if (nodeCost.get(node) < minimum) {
                minimum = nodeCost.get(node);
                lowestCost = node;
            }

        }
        return lowestCost;
    }
}
