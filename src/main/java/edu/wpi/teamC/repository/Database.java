package edu.wpi.teamC.repository;

import edu.wpi.teamC.entities.Employee;
import edu.wpi.teamC.entities.ExternalAddress;
import edu.wpi.teamC.entities.Patient;
import edu.wpi.teamC.entities.mapEditor.Node;
import edu.wpi.teamC.entities.requests.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class Database implements DBAccess {
    Connection conn;

    public Database(Connection conn) {
        this.conn = conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    //                  NODES ACCESS             //
    public void displayAllNodes() {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            String query = "SELECT * FROM Nodes";
            ResultSet rs = stmt.executeQuery(query);
            System.out.println("NodeID \txcoord \tycoord \tfloor \tbuilding \tnodeType \tlongName \tshortName");
            while (rs.next()) {
                //nodeID, xcoord, ycoord, floor, building, nodeType, longName, shortName
                System.out.println(rs.getString("nodeID") + " \t" + rs.getInt("xCoord")
                        + " \t" + rs.getInt("yCoord") + " \t" + rs.getString("floor")
                        + " \t" + rs.getString("building") + " \t" + rs.getString("nodeType")
                        + " \t" + rs.getString("longName") + " \t" + rs.getString("shortName"));
                System.out.println(" ");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void addNode(NodeDB node) {
        try {
            Statement stmt = conn.createStatement();
            String query = "INSERT INTO Nodes VALUES("
                    + "'" + node.getNodeID() + "',"
                    + Integer.parseInt(node.getXCoord()) + ","
                    + Integer.parseInt(node.getYCoord()) + ","
                    + "'" + node.getFloor() + "',"
                    + "'" + node.getBuilding() + "',"
                    + "'" + node.getNodeType() + "',"
                    + "'" + node.getLongName() + "',"
                    + "'" + node.getShortName() + "')";
            stmt.executeUpdate(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void updateNodeX(int x, String nodeID) {
        try {
            Statement stmt = conn.createStatement();
            String queryX = "UPDATE nodes SET xCoord = " + x + " WHERE NodeID = '" + nodeID + "'";
            stmt.executeUpdate(queryX);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void updateNodeY(int y, String nodeID) {
        try {
            Statement stmt = conn.createStatement();
            String queryY = "UPDATE nodes SET yCoord = " + y + " WHERE NodeID = '" + nodeID + "'";
            stmt.executeUpdate(queryY);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void updateNodeFloor(String f, String nodeID) {
        try {
            Statement stmt = conn.createStatement();
            String queryFloor = "UPDATE nodes SET floor = '" + f + "' WHERE NodeID = '" + nodeID + "'";
            stmt.executeUpdate(queryFloor);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void updateNodeBuilding(String b, String nodeID) {
        try {
            Statement stmt = conn.createStatement();
            String queryBuild = "UPDATE nodes SET building = '" + b + "' WHERE NodeID = '" + nodeID + "'";
            stmt.executeUpdate(queryBuild);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void updateNodeType(String t, String nodeID) {
        try {
            Statement stmt = conn.createStatement();
            String queryType = "UPDATE nodes SET nodeType = '" + t + "' WHERE NodeID = '" + nodeID + "'";
            stmt.executeUpdate(queryType);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void updateNodeLongName(String l, String nodeID) {
        try {
            Statement stmt = conn.createStatement();
            String queryLong = "UPDATE nodes SET longName = '" + l + "' WHERE NodeID = '" + nodeID + "'";
            stmt.executeUpdate(queryLong);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void updateNodeShortName(String s, String nodeID) {
        try {
            Statement stmt = conn.createStatement();
            String queryShort = "UPDATE nodes SET shortName = '" + s + "' WHERE NodeID = '" + nodeID + "'";
            stmt.executeUpdate(queryShort);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public ArrayList<NodeDB> getListOfNodes() throws SQLException {
        ArrayList<NodeDB> nodes = new ArrayList<NodeDB>();
        Statement stmt = conn.createStatement();
        String query = "SELECT * FROM Nodes";
        ResultSet rs = stmt.executeQuery(query);
        System.out.println("NodeID \txcoord \tycoord \tfloor \tbuilding \tnodeType \tlongName \tshortName");
        while (rs.next()) {
            //nodeID, xcoord, ycoord, floor, building, nodeType, longName, shortName
            NodeDB n = new NodeDB(rs.getString("nodeID"), "" + rs.getInt("xcoord") + "",
                    "" + rs.getInt("ycoord") + "", rs.getString("floor"), rs.getString("building"),
                    rs.getString("nodeType"), rs.getString("longName"), rs.getString("shortName"));
            nodes.add(n);
        }
        return nodes;
    }

    public void removeNode(String nodeID) {
        try {
            Statement stmt = conn.createStatement();
            String query = "DELETE FROM Nodes WHERE nodeID = '" + nodeID + "'";
            stmt.executeUpdate(query);
            System.out.println("Node " + nodeID + " removed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteNodes() throws SQLException {
        try {
            Statement stmt;
            stmt = conn.createStatement();
            String sql = "DROP TABLE Nodes ";
            stmt.executeUpdate(sql);
            System.out.println("Table  deleted in given database...");
            String query = "CREATE TABLE Nodes("
                    + "nodeID VARCHAR(10) NOT NULL,"
                    + "xcoord INT NOT NULL,"
                    + "ycoord INT NOT NULL,"
                    + "floor VARCHAR(2) NOT NULL,"
                    + "building VARCHAR(30) NOT NULL,"
                    + "nodeType VARCHAR(4) NOT NULL,"
                    + "longName	VARCHAR(100) NOT NULL,"
                    + "shortName VARCHAR(50) NOT NULL,"
                    + "PRIMARY KEY (nodeID))";
            stmt.executeUpdate(query);
            System.out.println("Nodes created");
        } catch (SQLException se) {
        }
    }

    public void saveNodes() throws IOException {
        File csvFile = new File("C:\\Users\\Public\\SavedNodes.csv");
        FileWriter csvWriter = new FileWriter(csvFile);
        csvWriter.append("nodeID");
        csvWriter.append(",");
        csvWriter.append("xcoord");
        csvWriter.append(",");
        csvWriter.append("ycoord");
        csvWriter.append(",");
        csvWriter.append("floor");
        csvWriter.append(",");
        csvWriter.append("building");
        csvWriter.append(",");
        csvWriter.append("nodeType");
        csvWriter.append(",");
        csvWriter.append("longName");
        csvWriter.append(",");
        csvWriter.append("shortName");
        csvWriter.append("\n");

        try {
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM Nodes";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                //nodeID, xcoord, ycoord, floor, building, nodeType, longName, shortName
                csvWriter.append(rs.getString("nodeID"));
                csvWriter.append(",");
                csvWriter.append("" + rs.getInt("xcoord") + "");
                csvWriter.append(",");
                csvWriter.append("" + rs.getInt("ycoord") + "");
                csvWriter.append(",");
                csvWriter.append(rs.getString("floor"));
                csvWriter.append(",");
                csvWriter.append(rs.getString("building"));
                csvWriter.append(",");
                csvWriter.append(rs.getString("nodeType"));
                csvWriter.append(",");
                csvWriter.append(rs.getString("longName"));
                csvWriter.append(",");
                csvWriter.append(rs.getString("shortName"));
                csvWriter.append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("CSV file saved");
        csvWriter.flush();
        csvWriter.close();
    }

    public NodeDB getCorrectNode(String nodeID) {
        Statement stmt = null;
        ArrayList<NodeDB> nodes = new ArrayList<NodeDB>();
        try {
            stmt = conn.createStatement();
            String query = "SELECT * FROM Nodes";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                nodes.add(new NodeDB(rs.getString("nodeID"), "" + rs.getInt("xCoord") + "",
                        "" + rs.getInt("yCoord") + "", rs.getString("floor"),
                        rs.getString("building"), rs.getString("nodeType"),
                        rs.getString("longName"), rs.getString("shortName")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).getNodeID().equals(nodeID)) {
                return nodes.get(i);
            }
        }
        return new NodeDB();
    }

    public Boolean databaseContainsNode(NodeDB node) throws SQLException {
        Statement stmt = conn.createStatement();
        String query = "SELECT * FROM Nodes WHERE nodeID = '" + node.getNodeID() + "'";
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            if (rs.getString("nodeID") != null) {
                return true;
            }
        }
        return false;
    }

    public void fileContainsNode(File file, NodeDB node) throws SQLException {
        ArrayList<NodeDB> nodes = new ArrayList<NodeDB>();
        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sc.nextLine();
        while (sc.hasNextLine()) {
            String[] line = sc.nextLine().split(",");
            NodeDB n = new NodeDB(line[0], line[1],
                    line[2], line[3], line[4],
                    line[5], line[6], line[7]);
            nodes.add(n);
        }
        sc.close();
    }


    public void resetNodes(File file) throws SQLException {
        ArrayList<NodeDB> nodes = getListOfNodes();
        ArrayList<String> nodeIDsFromDB = new ArrayList<String>();
        for (NodeDB node : nodes) {
            nodeIDsFromDB.add(node.getNodeID());
        }
        ArrayList<NodeDB> nodesFromFile = new ArrayList<NodeDB>();
        ArrayList<String> nodeIDsFromFile = new ArrayList<String>();
        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (IOException e) {
            //e.printStackTrace();
        }
        sc.nextLine();
        while (sc.hasNextLine()) {
            String[] line = sc.nextLine().split(",");
            NodeDB node = new NodeDB(line[0], line[1],
                    line[2], line[3], line[4],
                    line[5], line[6], line[7]);
            nodeIDsFromFile.add(node.getNodeID());
                if (databaseContainsNode(node)) {
                    updateNodeX(Integer.parseInt(node.getXCoord()), node.getNodeID());
                    updateNodeY(Integer.parseInt(node.getYCoord()), node.getNodeID());
                    updateNodeFloor(node.getFloor(), node.getNodeID());
                    updateNodeBuilding(node.getBuilding(), node.getNodeID());
                    updateNodeType(node.getNodeType(), node.getNodeID());
                    updateNodeLongName(node.getLongName(), node.getNodeID());
                    updateNodeShortName(node.getShortName(), node.getNodeID());
                } else {
                    addNode(node);
                }
        }
            for (String nodeID : nodeIDsFromDB) {
                if (!nodesFromFile.contains(nodeID)) {
                    ArrayList<EdgeDB> edgesFromNode = getListofEdgesFromNode(nodeID);
                    removeServiceRequestsWithNode(nodeID);

                    removeNode(nodeID);
                }
        }
        sc.close();
    }

    public void removeServiceRequestsWithNode(String nodeID) throws SQLException {
        try {
            Statement stmt = conn.createStatement();
            String[] requests = {"CounselingRequest", "ExternalPatientRequest", "FloralDeliveryRequest", "FoodRequest", "InternalPatientRequest", "InterpreterRequest", "MaintenanceRequest", "MedicineDeliveryRequest", "SanitationRequest"};
            String query = "SELECT * FROM ServiceRequest WHERE location = '" + nodeID + "'";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                ServiceRequest sr = new ServiceRequest(
                        rs.getInt("requestID"),
                        rs.getString("userName"),
                        rs.getString("location"),
                        rs.getString("description"),
                        rs.getString("fulfilledBy"),
                        rs.getString("isComplete")
                );
                try {
                    for (int i = 0; i < requests.length; i++) {
                        try {
                            String queryDelete = "DELETE FROM" + requests[i] + " WHERE requestID = " + sr.getRequestID();
                            stmt.executeUpdate(queryDelete);
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }

                    }
                    String queryDelete = "DELETE FROM ServiceRequest WHERE requestID = " + sr.getRequestID();
                    stmt.executeUpdate(queryDelete);
                    System.out.println("Service Request " + sr.getRequestID() + " removed");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //                         EDGES                 //

    public void addEdge(EdgeDB edge) {
        try {
            Statement stmt = conn.createStatement();
            String query = "INSERT INTO Edges VALUES("
                    + "'" + edge.getEdgeID() + "',"
                    + "'" + edge.getStartNode() + "',"
                    + "'" + edge.getEndNode() + "')";
            stmt.executeUpdate(query);
        } catch (
                SQLException throwables) {
            throwables.printStackTrace();
        }

    }


    public void updateStartNode(String id, String start) {
        try {
            Statement stmt = conn.createStatement();
            String query = "UPDATE Edges SET startNode = '" + start + "' WHERE EdgeID = '" + id + "'";
            stmt.executeUpdate(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void updateEndNode(String id, String end) {
        try {
            Statement stmt = conn.createStatement();
            String query = "UPDATE Edges SET endNode = '" + end + "' WHERE EdgeID = '" + id + "'";
            stmt.executeUpdate(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }


    // display all edges
    public void displayAllEdges() {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            String query = "SELECT * FROM Edges";
            ResultSet rs = stmt.executeQuery(query);
            System.out.println("NodeID \tstartNode \tendNode");
            while (rs.next()) {
                System.out.println(rs.getString("edgeID") + " \t" + rs.getInt("startNode")
                        + " \t" + rs.getInt("endNode"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // removeEdge
    public void removeEdge(String edgeID) {
        try {
            Statement stmt = conn.createStatement();
            String query = "DELETE FROM Edges WHERE edgeID = '" + edgeID + "'";
            stmt.executeUpdate(query);
            System.out.println("Edge " + edgeID + " removed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // getListofEdges
    public ArrayList<EdgeDB> getListofEdges() throws SQLException {
        ArrayList<EdgeDB> edges = new ArrayList<EdgeDB>();
        Statement stmt = conn.createStatement();
        String query = "SELECT * FROM Edges";
        ResultSet rs = stmt.executeQuery(query);
        System.out.println("EdgeID \tstartNode \tendNode");
        while (rs.next()) {
            EdgeDB n = new EdgeDB(rs.getString("edgeID"), "" + rs.getString("startNode") + "",
                    "" + rs.getString("endNode"));
            edges.add(n);
        }
        return edges;
    }

    public ArrayList<EdgeDB> getListofEdgesFromNode(String nodeID) throws SQLException {
        ArrayList<EdgeDB> edges = new ArrayList<EdgeDB>();
        Statement stmt = conn.createStatement();
        String query1 = "SELECT * FROM Edges WHERE startNode = '" + nodeID + "'";
        ResultSet rs1 = stmt.executeQuery(query1);
        System.out.println("EdgeID \tstartNode \tendNode");
        while (rs1.next()) {
            EdgeDB n = new EdgeDB(rs1.getString("edgeID"), "" + rs1.getString("startNode") + "",
                    "" + rs1.getString("endNode"));
            edges.add(n);
        }
        String query2 = "SELECT * FROM Edges WHERE endNode = '" + nodeID + "'";
        ResultSet rs2 = stmt.executeQuery(query2);
        System.out.println("EdgeID \tstartNode \tendNode");
        while (rs2.next()) {
            EdgeDB n = new EdgeDB(rs2.getString("edgeID"), "" + rs2.getString("startNode") + "",
                    "" + rs2.getString("endNode"));
            edges.add(n);
        }
        return edges;
    }

    public void deleteEdges() throws SQLException {
        try {
            Statement stmt;
            stmt = conn.createStatement();
            String sql = "DROP TABLE Edges ";
            stmt.executeUpdate(sql);
            System.out.println("Table  deleted in given database...");
            String query = "CREATE TABLE Edges("
                    + "edgeID VARCHAR(21) NOT NULL,"
                    + "startNode VARCHAR(10) NOT NULL,"
                    + "endNode VARCHAR(10) NOT NULL,"
                    + "PRIMARY KEY (edgeID))";
            stmt.executeUpdate(query);
            System.out.println("Edges created");
        } catch (SQLException se) {
            se.printStackTrace();
        }

    }

    public Boolean databaseContainsEdge(EdgeDB edge) throws SQLException {
        Statement stmt = conn.createStatement();
        String query = "SELECT * FROM Edges WHERE edgeID = '" + edge.getEdgeID() + "'";
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            if (rs.getString("edgeID") != null) {
                return true;
            }
        }
        return false;
    }

    public void fileContainsEdge(File file, EdgeDB edge) throws SQLException {
        ArrayList<EdgeDB> edges = new ArrayList<EdgeDB>();
        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sc.nextLine();
        while (sc.hasNextLine()) {
            String[] line = sc.nextLine().split(",");
            EdgeDB e = new EdgeDB(line[0], line[1], line[2]);
            edges.add(e);
        }
        for (EdgeDB e : edges) {
            if (edge.getEdgeID().equals(e.getEdgeID())) {

            }
        }
        sc.close();
    }

    public void resetEdges(File file) throws SQLException {
        ArrayList<EdgeDB> edges = getListofEdges();
        ArrayList<String> edgeIDsFromDB = new ArrayList<String>();
        for (EdgeDB edge : edges) {
            edgeIDsFromDB.add(edge.getEdgeID());
        }
        ArrayList<EdgeDB> edgesFromFile = new ArrayList<EdgeDB>();
        ArrayList<String> edgeIDsFromFile = new ArrayList<String>();
        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sc.nextLine();
        while (sc.hasNextLine()) {
            String[] line = sc.nextLine().split(",");
            EdgeDB edge = new EdgeDB(line[0], line[1],
                    line[2]);
            edgeIDsFromFile.add(edge.getEdgeID());
            if (databaseContainsEdge(edge)) {
                updateStartNode(edge.getEdgeID(),edge.getStartNode());
                updateEndNode(edge.getEdgeID(),edge.getEndNode());
            } else {
                addEdge(edge);
            }
        }
        for (String edgeID : edgeIDsFromDB) {
            if (!edgeIDsFromFile.contains(edgeID)) {
                removeEdge(edgeID);
            }

        }
        sc.close();
    }


    // saveEdge
    public void saveEdges() throws IOException {
        File csvFile = new File("C:\\Users\\Public\\SavedEdges.csv");
        FileWriter csvWriter = new FileWriter(csvFile);
        csvWriter.append("edgeID");
        csvWriter.append(",");
        csvWriter.append("startNode");
        csvWriter.append(",");
        csvWriter.append("endNode");
        csvWriter.append("\n");
        try {
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM Edges";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                //edgeID, startNode, endNode
                csvWriter.append(rs.getString("edgeID"));
                csvWriter.append(",");
                csvWriter.append(rs.getString("startNode"));
                csvWriter.append(",");
                csvWriter.append(rs.getString("endNode"));
                csvWriter.append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("CSV file saved");
        csvWriter.flush();
        csvWriter.close();
    }


    //                       SERVICE REQUEST              //
    public void addServiceRequest(ServiceRequest sr) {
        try {
            Statement stmt = conn.createStatement();
            String queryService = "INSERT INTO ServiceRequest(userName,location,description,fulfilledBy,isComplete) VALUES("
                    + "'" + sr.getUser() + "',"
                    + "'" + sr.getLocation() + "',"
                    + "'" + sr.getDescription() + "',"
                    + "'" + sr.getFulfilledBy() + "',"
                    + "'" + sr.getIsComplete() + "')";
            stmt.executeUpdate(queryService);
            sr.setRequestID(this.getRequestID(sr.getFulfilledBy()));
            System.out.println("Service Request " + this.getRequestID(sr.getFulfilledBy()) + " added to database");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void completeRequest(ServiceRequest serviceRequest) {
        try {
            Statement stmt = conn.createStatement();
            String query = "UPDATE ServiceRequest SET isComplete = 'True' WHERE requestID = " + serviceRequest.getRequestID() + "";
            stmt.executeUpdate(query);
            serviceRequest.setComplete();
            System.out.println("Here");
            System.out.println("Request " + serviceRequest.getRequestID() + " marked complete");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void incompleteRequest(ServiceRequest serviceRequest) {
        try {
            Statement stmt = conn.createStatement();
            String query = "UPDATE ServiceRequest SET isComplete = 'False' WHERE requestID = " + serviceRequest.getRequestID() + "";
            stmt.executeUpdate(query);
            serviceRequest.setIncomplete();
            System.out.println("Request " + serviceRequest.getRequestID() + " marked incomplete");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int getRequestID(String assignTo) {
        int serviceID = 0;
        try {
            Statement stmt = conn.createStatement();
            String query = "SELECT MAX(requestID) FROM ServiceRequest WHERE fulfilledby = '" + assignTo + "'";
            ResultSet rs1 = stmt.executeQuery(query);
            while (rs1.next()) {
                serviceID = rs1.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return serviceID;
    }

    public ArrayList<ServiceRequest> getRequestsFromUser(String userName) {
        ArrayList<ServiceRequest> serviceList = new ArrayList<ServiceRequest>();
        try {
            Statement stmt = conn.createStatement();
            String queryServiceRequest = "SELECT * FROM ServiceRequest WHERE fulfilledby = '" + userName + "'";
            ResultSet rsServiceRequest = stmt.executeQuery(queryServiceRequest);
            while (rsServiceRequest.next()) {
                NodeDB node = this.getCorrectNode(rsServiceRequest.getString("location"));
                ServiceRequest sr = new ServiceRequest(
                        rsServiceRequest.getInt("requestID"),
                        rsServiceRequest.getString("userName"),
                        rsServiceRequest.getString("location"),
                        rsServiceRequest.getString("description"),
                        rsServiceRequest.getString("fulfilledBy"),
                        rsServiceRequest.getString("isComplete")
                );
                serviceList.add(sr);
                System.out.println("ServiceRequest " + sr.getRequestID() + " added to list");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return serviceList;
    }


    public void addCovidSurvey(CovidSurvey covidSurvey) {
        try {
            Statement stmt = conn.createStatement();
            String queryCovid = "INSERT INTO CovidSurvey(patientName,userName,positiveTest,symptoms,closeContact,selfIsolate,feelGood,receivedVaccine,assignTo) VALUES("
                    + "'" + covidSurvey.getPatientName() + "',"
                    + "'" + covidSurvey.getUserName() + "',"
                    + "'" + covidSurvey.getPositiveTest() + "',"
                    + "'" + covidSurvey.getSymptoms() + "',"
                    + "'" + covidSurvey.getCloseContact() + "',"
                    + "'" + covidSurvey.getSelfIsolate() + "',"
                    + "'" + covidSurvey.getFeelGood() + "',"
                    + "'" + covidSurvey.getReceivedVaccine() + "',"
                    + "'" + covidSurvey.getAssignTo() + "')";
            stmt.executeUpdate(queryCovid);
            covidSurvey.setSurveyID(this.getSurveyID(covidSurvey.getAssignTo()));
            System.out.println("COVID-19 Survey " + covidSurvey.getSurveyID() + " added to database");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int getSurveyID(String assignTo) {
        int surveyID = 0;
        try {
            Statement stmt = conn.createStatement();
            String query = "SELECT MAX(surveyID) FROM CovidSurvey WHERE assignTo = '" + assignTo + "'";
            ResultSet rs1 = stmt.executeQuery(query);
            while (rs1.next()) {
                surveyID = rs1.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return surveyID;
    }

    public void markForNormalEntry(CovidSurvey covidSurvey) {
        try {
            Statement stmt = conn.createStatement();
            String query = "UPDATE CovidSurvey SET entryType = 'Normal' WHERE surveyID = " + covidSurvey.getSurveyID() + "";
            stmt.executeUpdate(query);
            allowPatientEntry(covidSurvey.getUserName(), "Normal");
            covidSurvey.setEntryType("Normal");
            System.out.println("Survey " + covidSurvey.getSurveyID() + " marked for normal entry");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void markForCOVIDEntry(CovidSurvey covidSurvey) {
        try {
            Statement stmt = conn.createStatement();
            String query = "UPDATE CovidSurvey SET entryType = 'COVID' WHERE surveyID = " + covidSurvey.getSurveyID() + "";
            stmt.executeUpdate(query);
            allowPatientEntry(covidSurvey.getUserName(), "COVID");
            covidSurvey.setEntryType("COVID");
            System.out.println("Survey " + covidSurvey.getSurveyID() + " marked for COVID-19 entry");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void markClearForEntry(CovidSurvey covidSurvey) {
        try {
            Statement stmt = conn.createStatement();
            String query = "UPDATE CovidSurvey SET isClear = 'True' WHERE surveyID = " + covidSurvey.getSurveyID() + "";
            stmt.executeUpdate(query);
            allowPatientClearance(covidSurvey.getUserName(), "True");
            covidSurvey.setIsClear("True");
            System.out.println("Survey " + covidSurvey.getSurveyID() + " marked clear for entry");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void markUnclearForEntry(CovidSurvey covidSurvey) {
        try {
            Statement stmt = conn.createStatement();
            String query = "UPDATE CovidSurvey SET isClear = 'False' WHERE surveyID = " + covidSurvey.getSurveyID() + "";
            stmt.executeUpdate(query);
            allowPatientClearance(covidSurvey.getUserName(), "False");
            covidSurvey.setIsClear("False");
            System.out.println("Survey " + covidSurvey.getSurveyID() + " marked unclear for entry");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void allowPatientEntry(String userName, String entryType) {
        try {
            Statement stmt = conn.createStatement();
            String query = "UPDATE patient SET entryType = '" + entryType + "' WHERE userName = '" + userName + "'";
            stmt.executeUpdate(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void allowPatientClearance(String userName, String entryType) {
        try {
            Statement stmt = conn.createStatement();
            String query = "UPDATE patient SET isClear = '" + entryType + "' WHERE userName = '" + userName + "'";
            stmt.executeUpdate(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void addPatient(Patient patient) {
        try {
            Statement stmt = conn.createStatement();
            String queryHosp = "INSERT INTO HospitalUser VALUES("
                    + "'" + patient.getUserName() + "',"
                    + "'" + patient.getUserEmail() + "',"
                    + "'" + patient.getPassword() + "',"
                    + "'" + patient.getFirstName() + "',"
                    + "'" + patient.getLastName() + "')";
            stmt.executeUpdate(queryHosp);

            String queryEmp = "INSERT INTO Patient(userName) VALUES("
                    + "'" + patient.getUserName() + "')";
            stmt.executeUpdate(queryEmp);
        } catch (
                SQLException throwables) {
            throwables.printStackTrace();
        }

    }


    public ArrayList<CovidSurvey> getAssignedSurveys(String userName) {
        ArrayList<CovidSurvey> surveyList = new ArrayList<CovidSurvey>();
        try {
            Statement stmt = conn.createStatement();
            String queryCovidSurvey = "SELECT * FROM CovidSurvey WHERE assignTo = '" + userName + "'";
            ResultSet rsCovidSurvey = stmt.executeQuery(queryCovidSurvey);
            while (rsCovidSurvey.next()) {
                CovidSurvey covidSurvey = new CovidSurvey(
                        rsCovidSurvey.getInt("surveyID"),
                        rsCovidSurvey.getString("patientName"),
                        rsCovidSurvey.getString("userName"),
                        rsCovidSurvey.getString("positiveTest"),
                        rsCovidSurvey.getString("symptoms"),
                        rsCovidSurvey.getString("closeContact"),
                        rsCovidSurvey.getString("selfIsolate"),
                        rsCovidSurvey.getString("feelGood"),
                        rsCovidSurvey.getString("receivedVaccine"),
                        rsCovidSurvey.getString("assignTo"),
                        rsCovidSurvey.getString("entryType"),
                        rsCovidSurvey.getString("isClear"));
                surveyList.add(covidSurvey);
                System.out.println("Covid survey " + covidSurvey.getSurveyID() + " added to list");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return surveyList;
    }

    //                EMPLOYEE            //

    public ArrayList<Employee> getListOfEmployees() throws SQLException {
        ArrayList<Employee> employees = new ArrayList<Employee>();
        Statement stmt = conn.createStatement();
        String query = "SELECT * FROM EMPLOYEE JOIN HOSPITALUSER H on H.USERNAME = EMPLOYEE.USERNAME";
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            Employee e = new Employee(rs.getString("userName"), rs.getString("userEmail"),
                    rs.getString("password"), rs.getString("firstName"), rs.getString("lastName"));
            employees.add(e);
        }
        return employees;
    }

    public void addEmployee(Employee employee) {
        try {
            Statement stmt = conn.createStatement();
            String queryHosp = "INSERT INTO HospitalUser VALUES("
                    + "'" + employee.getUserName() + "',"
                    + "'" + employee.getUserEmail() + "',"
                    + "'" + employee.getPassword() + "',"
                    + "'" + employee.getFirstName() + "',"
                    + "'" + employee.getLastName() + "')";
            stmt.executeUpdate(queryHosp);

            String queryEmp = "INSERT INTO Employee VALUES("
                    + "'" + employee.getUserName() + "')";
            stmt.executeUpdate(queryEmp);
        } catch (
                SQLException throwables) {
            throwables.printStackTrace();
        }

    }


    public void updateFirstName(String username, String firstName) {
        try {
            Statement stmt = conn.createStatement();
            String query = "UPDATE HospitalUser SET firstName = '" + firstName + "' WHERE userName = '" + username + "'";
            stmt.executeUpdate(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void updateLastName(String username, String lastName) {
        try {
            Statement stmt = conn.createStatement();
            String query = "UPDATE HospitalUser SET lastName = '" + lastName + "' WHERE userName = '" + username + "'";
            stmt.executeUpdate(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void updateUserEmail(String username, String email) {
        try {
            Statement stmt = conn.createStatement();
            String query = "UPDATE HospitalUser SET userEmail = '" + email + "' WHERE userName = '" + username + "'";
            stmt.executeUpdate(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void updatePassword(String username, String pass) {
        try {
            Statement stmt = conn.createStatement();
            String query = "UPDATE HospitalUser SET password = '" + pass + "' WHERE userName = '" + username + "'";
            stmt.executeUpdate(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void removeEmployee(String username) {
        try {
            Statement stmt = conn.createStatement();
            String queryEmp = "DELETE FROM Employee WHERE userName = '" + username + "'";
            stmt.executeUpdate(queryEmp);

            String queryHosp = "DELETE FROM HospitalUser WHERE userName = '" + username + "'";
            stmt.executeUpdate(queryHosp);
            System.out.println("Employee " + username + " removed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //                EXTERNAL PATIENT REQUEST                  //

    public void addAddress(ExternalAddress ea) {
        try {
            Statement stmt = conn.createStatement();
            String queryAddress = "INSERT INTO ExternalAddress (street, town, usState, zipCode) VALUES("
                    + "'" + ea.getStreet() + "',"
                    + "'" + ea.getTown() + "',"
                    + "'" + ea.getState() + "',"
                    + "'" + ea.getZipCode() + "')";
            stmt.executeUpdate(queryAddress);
            ea.setAddressID(this.getAddressID(ea.getStreet()));

            System.out.println("Address  " + ea.getAddressID() + " added to database");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public int getAddressID(String street) {
        int addressID = 0;
        try {
            Statement stmt = conn.createStatement();
            String query1 = "Select MAX(addressID) FROM ExternalAddress WHERE street = '" + street + "'";
            ResultSet rs = stmt.executeQuery(query1);
            while (rs.next()) {
                addressID = rs.getInt(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return addressID;
    }

    public String getEntryType(String p) {
        String entryType = "";
        try {
            Statement stmt = conn.createStatement();
            String query1 = "Select entryType FROM Patient WHERE userName = '" + p + "'";
            ResultSet rs = stmt.executeQuery(query1);
            while (rs.next()) {
                entryType = rs.getString("entryType");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return entryType;
    }

    public void addExternalPatientRequest(ExternalPatientRequest er) {
        try {
            Statement stmt = conn.createStatement();
            String queryExternal = "INSERT INTO ExternalPatientRequest (requestID, userName, patientName, description, location, addressID, transportType, fulfilledBy, isComplete, date, time) VALUES("
                    + "" + er.getServiceID() + ","
                    + "'" + er.getUserEmail() + "',"
                    + "'" + er.getPatientName() + "',"
                    + "'" + er.getReason() + "',"
                    + "'" + er.getLocation() + "',"
                    + "" + er.getAddressID() + ","
                    + "'" + er.getType() + "',"
                    + "'" + er.getAssignTo() + "',"
                    + "'" + er.getIsComplete() + "',"
                    + "'" + er.getDate() + "',"
                    + "'" + er.getTime() + "')";
            System.out.println("External Request " + er.getServiceID() + " added to database");
            stmt.executeUpdate(queryExternal);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void addMedicineDeliveryRequest(MedicineDeliveryRequest mr) {
        try {
            Statement stmt = conn.createStatement();
            String queryMedicine = "INSERT INTO MedicineDeliveryRequest VALUES("
                    + "" + mr.getRequestID() + ","
                    + "'" + mr.getUserName() + "',"
                    + "'" + mr.getPatientID() + "',"
                    + "'" + mr.getMedication() + "',"
                    + "'" + mr.getDosage() + "',"
                    + "'" + mr.getDeliveryTime() + "',"
                    + "'" + mr.getDeliveryDate() + "',"
                    + "'" + mr.getInstructions() + "',"
                    + "'" + mr.getLocation() + "',"
                    + "'" + mr.getFulfilledBy() + "',"
                    + "'" + mr.getIsComplete() + "')";
            System.out.println("Medicine Delivery Request " + mr.getRequestID() + " added to database");
            stmt.executeUpdate(queryMedicine);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    //            SANITATION REQUEST       //

    public void addSanitationRequest(SanitationRequest sr) {
        try {
            Statement stmt = conn.createStatement();
            String querySanitation = "INSERT INTO SanitationRequest VALUES("
                    + "" + sr.getServiceID() + ","
                    + "'" + sr.getEmail() + "',"
                    + "'" + sr.getDescription() + "',"
                    + "'" + sr.getLocation() + "',"
                    + "'" + sr.getAssignTo() + "',"
                    + "'" + sr.getIsComplete() + "')";
            System.out.println("Sanitation Request " + sr.getServiceID() + " added to database");
            stmt.executeUpdate(querySanitation);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void addInternalPatientRequest(InternalPatientRequest ir) {
        try {
            Statement stmt = conn.createStatement();
            String queryInternal = "INSERT INTO InternalPatientRequest VALUES("
                    + ir.getRequestID() + ","
                    + "'" + ir.getUserName() + "',"
                    + "'" + ir.getPatientID() + "',"
                    + "'" + ir.getLocation() + "',"
                    + "'" + ir.getDestination() + "',"
                    + "'" + ir.getTransportType() + "',"
                    + "'" + ir.getTime() + "',"
                    + "'" + ir.getTransportReason() + "',"
                    + "'" + ir.getIsComplete() + "',"
                    + "'" + ir.getFulfilledBy() + "')";
            System.out.println("Internal Request " + ir.getRequestID() + " added to database");
            stmt.executeUpdate(queryInternal);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public boolean checkPatientLogin(String userBoxText, String passBoxText) throws SQLException {
        Statement stmt = conn.createStatement();
        try {
            String queryPatient = "SELECT * FROM PATIENT P INNER JOIN HOSPITALUSER H on H.USERNAME = P.USERNAME";
            ResultSet rsAdmin = stmt.executeQuery(queryPatient);
            System.out.println(rsAdmin.getFetchSize());
            while (rsAdmin.next()) {
                System.out.println("HERE");
                if (rsAdmin.getString("userName").equals(userBoxText)
                        && passBoxText.equals(rsAdmin.getString("password"))) {
                    return true;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean checkAdminLogin(String userBoxText, String passBoxText) throws SQLException {
        Statement stmt = conn.createStatement();
        try {
            String queryAdmin = "SELECT * FROM ADMINISTRATOR A INNER JOIN HOSPITALUSER H on H.USERNAME = A.USERNAME";
            ResultSet rsAdmin = stmt.executeQuery(queryAdmin);
            while (rsAdmin.next()) {
                System.out.println("admin");
                if (rsAdmin.getString("userName").equals(userBoxText)
                        && passBoxText.equals(rsAdmin.getString("password"))) {
                    return true;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean checkEmployeeLogin(String userBoxText, String passBoxText) throws SQLException {
        Statement stmt = conn.createStatement();
        try {
            String queryEmployee = "SELECT * FROM EMPLOYEE E INNER JOIN HOSPITALUSER H on H.USERNAME = E.USERNAME";
            ResultSet rsAdmin = stmt.executeQuery(queryEmployee);
            while (rsAdmin.next()) {
                if (rsAdmin.getString("userName").equals(userBoxText)
                        && passBoxText.equals(rsAdmin.getString("password"))) {
                    return true;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public void addCounselingRequest(CounselingRequest cr) {
        try {
            Statement stmt = conn.createStatement();
            String queryService = "INSERT INTO CounselingRequest VALUES("
                    + "" + cr.getRequestID() + ","
                    + "'" + cr.getUserName() + "',"
                    + "'" + cr.getPatientName() + "',"
                    + "'" + cr.getCounselingType() + "',"
                    + "'" + cr.getLocation() + "',"
                    + "'" + cr.getOtherInfo() + "',"
                    + "'" + cr.getFulfilledBy() + "',"
                    + "'" + cr.getIsComplete() + "')";
            stmt.executeUpdate(queryService);
            System.out.println("Counseling Request " + cr.getRequestID() + " added to database");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void addInterpreterRequest(InterpreterRequest ir) {
        try {
            Statement stmt = conn.createStatement();
            String queryInterpreter = "INSERT INTO InterpreterRequest VALUES("
                    + ir.getRequestID() + ","
                    + "'" + ir.getUserName() + "',"
                    + "'" + ir.getRequestName() + "',"
                    + "'" + ir.getLanguage() + "',"
                    + "'" + ir.getLocation() + "',"
                    + "'" + ir.getDescription() + "',"
                    + "'" + ir.getFulfilledBy() + "',"
                    + "'" + ir.getIsComplete() + "')";
            stmt.executeUpdate(queryInterpreter);
            System.out.println("Interpreter Request " + ir.getRequestID() + " added to database");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void addFloralDeliveryRequest(FloralDeliveryRequest fr) {
        try {
            Statement stmt = conn.createStatement();
            String queryService = "INSERT INTO FloralDeliveryRequest VALUES("
                    + "" + fr.getRequestID() + ","
                    + "'" + fr.getUserName() + "',"
                    + "'" + fr.getLocation() + "',"
                    + "'" + fr.getPatientName() + "',"
                    + "'" + fr.getFlowerType() + "',"
                    + "'" + fr.getFlowerNum() + "',"
                    + "'" + fr.getNotes() + "',"
                    + "'" + fr.getFulfilledBy() + "',"
                    + "'" + fr.getIsComplete() + "')";
            stmt.executeUpdate(queryService);
            System.out.println("Floral delivery Request " + fr.getRequestID() + " added to database");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void addMaintenanceRequest(MaintenanceRequest mr) {
        try {
            Statement stmt = conn.createStatement();
            String queryService = "INSERT INTO MaintenanceRequest VALUES("
                    + mr.getRequestID() + ","
                    + "'" + mr.getUserName() + "',"
                    + "'" + mr.getName() + "',"
                    + "'" + mr.getLocation() + "',"
                    + "'" + mr.getFulfilledBy() + "',"
                    + "'" + mr.getSummary() + "',"
                    + "'" + mr.getIsComplete() + "')";
            stmt.executeUpdate(queryService);
            System.out.println("Maintenance Request " + mr.getRequestID() + " added to database");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //            Food REQUEST       //

    public void addFoodRequest(FoodRequest fr) {
        try {
            Statement stmt = conn.createStatement();
            String queryFood = "INSERT INTO FoodRequest VALUES(" +
                    fr.getRequestID() + ","
                    + "'" + fr.getUsername() + "',"
                    + "'" + fr.getName() + "',"
                    + "'" + fr.getFood() + "',"
                    + "'" + fr.getSide() + "',"
                    + "'" + fr.getDrink() + "',"
                    + "'" + fr.getLocation() + "',"
                    + "'" + fr.getAssignTo() + "',"
                    + "'" + fr.getIsComplete() + "',"
                    + "'" + fr.getDate() + "',"
                    + "'" + fr.getTime() + "',"
                    + "'" + fr.getSpecialInstructions() + "')";
            System.out.println("Food Request " + fr.getRequestID() + " added to database");
            stmt.executeUpdate(queryFood);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void addEmergencyRequest(EmergencyRequest er) {
        try {
            Statement stmt = conn.createStatement();
            String queryService = "INSERT INTO EmergencyRequest VALUES("
                    + er.getRequestID() + ","
                    + "'" + er.getUsername() + "',"
                    + "'" + er.getName() + "',"
                    + "'" + er.getLocation() + "',"
                    + "'" + er.getAssignTo() + "',"
                    + "'" + er.getSummary() + "',"
                    + "'" + er.getIsComplete() + "',"
                    + "'" + er.getEmergencyType() + "')";
            stmt.executeUpdate(queryService);
            System.out.println("Emergency Request " + er.getRequestID() + " added to database");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public String getUserEmail(String username){
        try {
            Statement stmt = conn.createStatement();
            String queryEmail = "SELECT * FROM HOSPITALUSER WHERE userName = '" +username + "'";
            ResultSet rsAdmin = stmt.executeQuery(queryEmail);
            System.out.println(rsAdmin.next());
            return rsAdmin.getString("userEmail");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "";
    }
    }
    /*
    + "requestID INT NOT NULL REFERENCES ServiceRequest(requestID),"
                    + "userName VARCHAR(20) NOT NULL REFERENCES HospitalUser(userName),"
                    + "location VARCHAR(10) NOT NULL REFERENCES Nodes(nodeID),"
                    + "patientName VARCHAR(100) NOT NULL,"
                    + "flowerType VARCHAR(25) NOT NULL,"
                    + "flowerNo VARCHAR(3) NOT NULL,"
                    + "notes VARCHAR(100) NOT NULL,"
                    + "fulfilledBy VARCHAR(20) NOT NULL REFERENCES Employee(userName),"
                    + "isComplete VARCHAR(5) NOT NULL,"
                    + "PRIMARY KEY (requestID))";
     */
