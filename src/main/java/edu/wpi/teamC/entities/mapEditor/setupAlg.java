package edu.wpi.teamC.entities.mapEditor;

import java.io.File;
import java.net.URL;
import java.util.Scanner;

public class setupAlg {
    public void initGraph() {
        Graph newGraph=Graph.getInstance();
        Scanner sc = null;

        try {
            URL resource = getClass().getClassLoader().getResource("/L1Nodes.csv");
            sc = new Scanner(new File(resource.toURI()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(sc.hasNextLine())
        sc.nextLine();
        while (sc.hasNextLine()) {
            String[] line = sc.nextLine().split(",");
            System.out.println(line[0]);
            newGraph.addNode(new Node(line[0], Integer.parseInt(line[1]),
                    Integer.parseInt(line[2]), line[3], line[4], line[5], line[6], line[7]));


        }

        sc.close();
        try {
            URL resource = getClass().getClassLoader().getResource("/L1Edges.csv");
            sc = new Scanner(new File(resource.toURI()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(sc.hasNextLine())
        sc.nextLine();
        while (sc.hasNextLine()) {

            String[] line = sc.nextLine().split(",");
            try {
                newGraph.addConnection(new Edge(newGraph.getNode(line[1]), newGraph.getNode(line[2])),true);
            }
            catch(Exception exception){
                System.out.println("Invalid edge in file, cannot find node: "+line[1]+" or "+line[2]);
            }

        }
        sc.close();



    }
}
