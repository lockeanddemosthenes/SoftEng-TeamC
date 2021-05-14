package edu.wpi.teamC.entities.mapEditor;

import java.util.HashMap;
import java.util.LinkedList;

public class BreadthFirst implements ISearch{

    public MultiFloorPath search(Node startNode, Node endNode,boolean isEmergency) {
        Path path = new Path();
        HashMap<Integer, LinkedList<Node>> nodeAtDepth = new HashMap<>();
        HashMap<Node, Node> cameFrom = new HashMap<>();
        LinkedList<Node> l = new LinkedList<Node>();
        LinkedList<Node> visited = new LinkedList<>();
        Node current = startNode;
        l.add(startNode);

        boolean foundGoal = false;
        int i = 0;
        nodeAtDepth.put(i,l);
        while (!nodeAtDepth.get(i).isEmpty()) {
            LinkedList<Node> depthN = new LinkedList<>();
            for (Node n : nodeAtDepth.get(i)) {
                current = n;
                if (current.equals(endNode)) {
                    foundGoal = true;
                    break;
                }
                for (Edge e : n.getEdges()) {
                    if (!visited.contains(e.getDestinationNode())) {
                        boolean emergencyRoute=(e.getDestinationNode().getNodeType().equals("EMER"));
                        boolean noEmergencyRoute=e.getDestinationNode().getNodeType().equals("EXIT");
                        if((emergencyRoute&&isEmergency)||(noEmergencyRoute&&!isEmergency)||(!noEmergencyRoute&&!emergencyRoute)) {
                            visited.add(e.getDestinationNode());
                            depthN.add(e.getDestinationNode());
                            cameFrom.put(e.getDestinationNode(), n);
                        }
                    }
                }

            }
            if(foundGoal)
                break;
            i++;
            nodeAtDepth.put(i,depthN);

        }
        Node trackBack = current;//now to extract the path, goal=current nominally
        if (foundGoal) {          //if the path is valid from start-> finish
            while (trackBack != startNode) {//until the start is reached
                path.addNode(trackBack);   //we check where each node came from, gives fastest path from goal to
                //start

                trackBack = cameFrom.get(trackBack);
            }
            path.addNode(startNode);
            path = path.reverse();          //now reverse the path so that the path goes start->goal
            return new MultiFloorPath(path);
        }
        return new MultiFloorPath(path);
    }
}
