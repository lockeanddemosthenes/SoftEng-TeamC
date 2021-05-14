package edu.wpi.teamC.entities.mapEditor;

import edu.wpi.teamC.repository.EdgeDB;
import edu.wpi.teamC.repository.NodeDB;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

public class ObjectConversion {
    public static LinkedList<Edge> edgeDBToEdge(ArrayList<EdgeDB> edgeDBList){
        Graph hospitalGraph=Graph.getInstance();
        LinkedList<Edge> edges=new LinkedList<>();
        for (EdgeDB e:edgeDBList){
            edges.add(new Edge(hospitalGraph.getNode(e.getStartNode()),hospitalGraph.getNode(e.getEndNode()),e.getEdgeID()));
        }
        return edges;
    }

    public static LinkedList<? extends Node> nodeDBToNode(ArrayList<NodeDB> listOfNodeDB) {
        LinkedList<Node> nodes=new LinkedList<>();
        for (NodeDB n:listOfNodeDB){
            nodes.add(new Node(n.getNodeID(),Integer.parseInt(n.getXCoord()),Integer.parseInt(n.getYCoord()),
                   n.getFloor(),n.getBuilding(),n.getNodeType(),n.getLongName(),n.getShortName()) );

        }
        return nodes;
    }
}
