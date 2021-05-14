package edu.wpi.teamC;

import edu.wpi.teamC.repository.ConnectionSingleton;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class SetupDB {

    /**
     *
     * @throws SQLException
     *
     * Connects to the drivers and database for Apache Derby
     */
    public static void main(String[] args) {


        ConnectionSingleton c = ConnectionSingleton.getInstance();
        Connection connection = c.conn;
        System.out.println("Apache Derby connection established!");

        createNodesTable(connection);
        populateNodes(connection);
        createEdgesTable(connection);
        populateEdges(connection);
        createUserTable(connection);
        createEmployeeTable(connection);
        createPatientTable(connection);
        createAdminTable(connection);

        createServiceRequestTable(connection);
        createSanitationRequestTable(connection);
        createExternalAddressTable(connection);
        createExternalPatientRequestTable(connection);
        createInterpreterRequestTable(connection);
        createInternalPatientRequestTable(connection);
        createMedicineDeliveryRequestTable(connection);
        createFloralDeliveryRequestTable(connection);
        createFoodRequestTable(connection);
        createCounselingRequestTable(connection);
        createMaintenanceRequestTable(connection);
        createCovidSurveyTable(connection);

        createGraphTable(connection);
        createGraphHasNodeTable(connection);
        createGraphHasEdgeTable(connection);
        creatEmergencyRequestTable(connection);
        addHospitalUsers(connection);

    }

    private static void addHospitalUsers(Connection conn) {
        try{
            Statement stmt = conn.createStatement();
            String queryUser1 = "INSERT INTO HospitalUser VALUES("
                    + "'" + "admin" + "',"
                    + "'" + "admin" + "',"
                    + "'" + "admin" + "',"
                    + "'" + "Admin" + "',"
                    + "'" + "Admin" + "')";
            stmt.executeUpdate(queryUser1);

            String queryUser12 = "INSERT INTO HospitalUser VALUES("
                    + "'" + "trschaeffer" + "',"
                    + "'" + "trschaeffer@wpi.edu" + "',"
                    + "'" + "trschaeffer" + "',"
                    + "'" + "Toby" + "',"
                    + "'" + "Schaeffer" + "')";
            stmt.executeUpdate(queryUser12);

            String queryUser2 = "INSERT INTO HospitalUser VALUES("
                    + "'" + "staff" + "',"
                    + "'" + "staff" + "',"
                    + "'" + "staff" + "',"
                    + "'" + "Staff" + "',"
                    + "'" + "Staff" + "')";
            stmt.executeUpdate(queryUser2);

            String queryUser6 = "INSERT INTO HospitalUser VALUES("
                    + "'" + "hgsmith" + "',"
                    + "'" + "hgsmith@wpi.edu" + "',"
                    + "'" + "hgsmith" + "',"
                    + "'" + "Hayden" + "',"
                    + "'" + "Smith" + "')";
            stmt.executeUpdate(queryUser6);

            String queryUser7 = "INSERT INTO HospitalUser VALUES("
                    + "'" + "ceflanagan" + "',"
                    + "'" + "ceflanagan@wpi.edu" + "',"
                    + "'" + "password" + "',"
                    + "'" + "Carlie" + "',"
                    + "'" + "Flanagan" + "')";
            stmt.executeUpdate(queryUser7);

            String queryUser5 = "INSERT INTO HospitalUser VALUES("
                    + "'" + "guest" + "',"
                    + "'" + "guest" + "',"
                    + "'" + "guest" + "',"
                    + "'" + "Guest" + "',"
                    + "'" + "Guest" + "')";
            stmt.executeUpdate(queryUser5);

            String queryUser10 = "INSERT INTO HospitalUser VALUES("
                    + "'" + "wwong2" + "',"
                    + "'" + "wwong2@wpi.edu" + "',"
                    + "'" + "wwong2" + "',"
                    + "'" + "Wilson" + "',"
                    + "'" + "Wong" + "')";
            stmt.executeUpdate(queryUser10);

            String queryUser3 = "INSERT INTO Administrator VALUES("
                    + "'" + "admin" + "')";
            stmt.executeUpdate(queryUser3);

            String queryUser4 = "INSERT INTO Employee VALUES("
                    + "'" + "staff" + "')";
            stmt.executeUpdate(queryUser4);

            String queryUser8 = "INSERT INTO Employee VALUES("
                    + "'" + "hgsmith" + "')";
            stmt.executeUpdate(queryUser8);

            String queryUser11 = "INSERT INTO Employee VALUES("
                    + "'" + "wwong2" + "')";
            stmt.executeUpdate(queryUser11);

            String queryUser9 = "INSERT INTO Patient(userName) VALUES("
                    + "'" + "guest" + "')";
            stmt.executeUpdate(queryUser9);

            String queryUser14 = "INSERT INTO Patient(userName) VALUES("
                    + "'" + "trschaeffer" + "')";
            stmt.executeUpdate(queryUser14);

            String queryUser13 = "INSERT INTO Employee VALUES("
                    + "'" + "ceflanagan" + "')";
            stmt.executeUpdate(queryUser13);

            System.out.println("Working!");
        }catch (SQLException e){

        }
    }

    /**
     *
     * @param conn Connection to a database
     *
     * Creates the table of nodes within the database
     */
    private static void createNodesTable(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            String query = "CREATE TABLE Nodes("
                    + "nodeID VARCHAR(10) NOT NULL,"
                    + "xCoord INTEGER NOT NULL,"
                    + "yCoord INTEGER  NOT NULL,"
                    + "floor VARCHAR(2) NOT NULL,"
                    + "building VARCHAR(50) NOT NULL,"
                    + "nodeType VARCHAR(4) NOT NULL,"
                    + "longName	VARCHAR(120) NOT NULL,"
                    + "shortName VARCHAR(60) NOT NULL,"
                    + "PRIMARY KEY (nodeID))";
            stmt.executeUpdate(query);
            System.out.println("Nodes created");
        } catch (SQLException e) {

        }
    }

    /**
     *
     * @param conn Connection to a database
     *
     * Populates the table of nodes within the database with information from a csv file
     */
    private static void populateNodes(Connection conn){
        Scanner sc = null;
        try{
            URL url = SetupDB.class.getResource("/MapCAllNodes.csv");
            sc = new Scanner(url.openStream());
        } catch (IOException e) {

        }
        sc.nextLine();
        while(sc.hasNextLine()){
            String[] line = sc.nextLine().split(",");
            try{
                Statement stmt = conn.createStatement();
                //NodeID, xcoord, ycoord, floor, building, nodeType, longName, shortName
                String query = "INSERT INTO Nodes VALUES("
                        + "'" + line[0] +"'," //nodeID
                        + Integer.parseInt(line[1]) +"," //xcoord
                        + Integer.parseInt(line[2])  +"," //ycoord
                        + "'" + line[3] +"'," //floor
                        + "'" + line[4] +"'," //building
                        + "'" + line[5] +"'," //nodeType
                        + "'" + line[6] +"'," //longName
                        + "'" + line[7] +"')"; //shortName
                stmt.execute(query);
                System.out.println("Nodes populated");
            } catch (SQLException e) {

            }
        }
        sc.close();

        try{
            conn.commit();
        } catch (SQLException e) {

        }
    }

    /**
     *
     * @param conn Connection to a database
     *
     * Creates the table of edges within the database
     */
    private static void createEdgesTable(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            String query = "CREATE TABLE Edges("
                    + "edgeID VARCHAR(21) NOT NULL,"
                    + "startNode VARCHAR(10) NOT NULL REFERENCES Nodes(nodeID),"
                    + "endNode VARCHAR(10) NOT NULL REFERENCES Nodes(nodeID),"
                    + "PRIMARY KEY (edgeID))";
            stmt.executeUpdate(query);
            System.out.println("Edges created");
        } catch (SQLException throwables) {

        }
    }

    /**
     *
     * @param conn Connection to a database
     *
     * Populates the table of edges within the database with information from a csv file
     */
    private static void populateEdges(Connection conn){
        Scanner sc = null;
        try{
            URL url = SetupDB.class.getResource("/MapCAllEdges.csv");
            sc = new Scanner(url.openStream());
        } catch (IOException e) {

        }
        sc.nextLine();
        while(sc.hasNextLine()){
            String[] line = sc.nextLine().split(",");
            try{
                Statement stmt = conn.createStatement();
                String query = "INSERT INTO Edges VALUES("
                        + "'" + line[0] + "',"
                        + "'" + line[1] + "',"
                        + "'" + line[2] + "')";
                stmt.execute(query);
                System.out.println("Edges populated");
            } catch (SQLException throwables) {

            }
        }
        sc.close();
        try{
            conn.commit();
        } catch (SQLException throwables) {

        }
    }

    private static void createUserTable(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            String query = "CREATE TABLE HospitalUser("
                    + "userName VARCHAR(20) NOT NULL,"
                    + "userEmail VARCHAR(50) NOT NULL,"
                    + "password VARCHAR(50) NOT NULL,"
                    + "firstName VARCHAR(100) NOT NULL,"
                    + "lastName VARCHAR(100) NOT NULL,"
                    + "PRIMARY KEY (userName))";
            stmt.executeUpdate(query);
            System.out.println("Users created");
        } catch (SQLException throwables) {

        }
    }

    private static void createEmployeeTable(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            String query = "CREATE TABLE Employee("
                    + "userName VARCHAR(20) NOT NULL REFERENCES HospitalUser(userName),"
                    + "PRIMARY KEY (userName))";
            stmt.executeUpdate(query);
            System.out.println("Employees created");
        } catch (SQLException throwables) {

        }
    }

    private static void createPatientTable(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            String query = "CREATE TABLE Patient("
                    + "userName VARCHAR(20) NOT NULL REFERENCES HospitalUser(userName),"
                    + "entryType VARCHAR(10),"
                    + "isClear VARCHAR(5),"
                    + "PRIMARY KEY (userName))";
            stmt.executeUpdate(query);
            System.out.println("Patients created");
        } catch (SQLException throwables) {

        }
    }

    private static void createAdminTable(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            String query = "CREATE TABLE Administrator("
                    + "userName VARCHAR(20) NOT NULL REFERENCES HospitalUser(userName),"
                    + "PRIMARY KEY (userName))";
            stmt.executeUpdate(query);
            System.out.println("Administrators created");
        } catch (SQLException throwables) {

        }
    }

    private static void createGraphTable(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            String query = "CREATE TABLE Graph("
                    + "graphNum INTEGER NOT NULL,"
                    + "adminID VARCHAR(20) NOT NULL REFERENCES Administrator(userName),"
                    + "PRIMARY KEY (graphNum))";
            stmt.executeUpdate(query);
            System.out.println("Graph table created");
        } catch (SQLException throwables) {

        }
    }

    private static void createGraphHasNodeTable(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            String query = "CREATE TABLE GraphHasNode("
                    + "graphNodeID VARCHAR(100) NOT NULL,"
                    + "graphNum INTEGER NOT NULL REFERENCES Graph(graphNum),"
                    + "nodeID VARCHAR(10) NOT NULL references Nodes(nodeID),"
                    + "PRIMARY KEY (graphNodeID))";
            stmt.executeUpdate(query);
            System.out.println("GraphHasNode table created");
        } catch (SQLException throwables) {

        }
    }

    private static void createGraphHasEdgeTable(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            String query = "CREATE TABLE GraphHasEdge("
                    + "graphEdgeID VARCHAR(100) NOT NULL,"
                    + "graphNum INTEGER NOT NULL REFERENCES Graph(graphNum),"
                    + "edgeID VARCHAR(21) NOT NULL references Edges(edgeID),"
                    + "PRIMARY KEY (graphEdgeID))";
            stmt.executeUpdate(query);
            System.out.println("GraphHasEdge table created");
        } catch (SQLException throwables) {

        }
    }


    private static void createServiceRequestTable(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            String query = "CREATE TABLE ServiceRequest("
                    + "requestID INT NOT NULL GENERATED ALWAYS AS IDENTITY,"
                    + "userName VARCHAR(20) NOT NULL REFERENCES HospitalUser(userName),"
                    + "location VARCHAR(10) REFERENCES Nodes(nodeID),"
                    + "description VARCHAR(200) NOT NULL,"
                    + "fulfilledBy VARCHAR(20) NOT NULL REFERENCES Employee(userName),"
                    + "isComplete VARCHAR(5) NOT NULL,"
                    + "PRIMARY KEY (requestID))";
            stmt.executeUpdate(query);
            System.out.println("Service Request table created");
        } catch (SQLException e) {

        }
    }


    private static void createSanitationRequestTable(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            String query = "CREATE TABLE SanitationRequest("
                    + "requestID INT NOT NULL REFERENCES ServiceRequest(requestID),"
                    + "userName VARCHAR(20) NOT NULL REFERENCES HospitalUser(userName),"
                    + "description VARCHAR(200) NOT NULL,"
                    + "location VARCHAR(10) REFERENCES Nodes(nodeID),"
                    + "fulfilledBy VARCHAR(20) NOT NULL REFERENCES Employee(userName),"
                    + "isComplete VARCHAR(5) NOT NULL,"
                    + "PRIMARY KEY (requestID))";
            stmt.executeUpdate(query);
            System.out.println("Sanitation Request table created");
        } catch (SQLException e) {

        }
    }

    private static void createExternalAddressTable(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            String query = "CREATE TABLE ExternalAddress("
                    + "addressID INT NOT NULL GENERATED ALWAYS AS IDENTITY,"
                    + "street VARCHAR(150) NOT NULL,"
                    + "town VARCHAR(100) NOT NULL,"
                    + "usState VARCHAR(50) NOT NULL,"
                    + "zipCode VARCHAR(5) NOT NULL,"
                    + "PRIMARY KEY (addressID))";
            stmt.executeUpdate(query);
            System.out.println("External Address Request table created");
        } catch (SQLException e) {

        }
    }

    private static void createExternalPatientRequestTable(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            String query = "CREATE TABLE ExternalPatientRequest("
                    + "requestID INT NOT NULL REFERENCES ServiceRequest(requestID),"
                    + "userName VARCHAR(20) NOT NULL REFERENCES HospitalUser(userName),"
                    + "patientName VARCHAR(100) NOT NULL,"
                    + "description VARCHAR(200) NOT NULL,"
                    + "location VARCHAR(10) REFERENCES Nodes(nodeID),"
                    + "addressID INT NOT NULL REFERENCES ExternalAddress(addressID),"
                    + "transportType VARCHAR(50) NOT NULL,"
                    + "fulfilledBy VARCHAR(20) NOT NULL REFERENCES Employee(userName),"
                    + "isComplete VARCHAR(5) NOT NULL,"
                    + "date VARCHAR(10) NOT NULL,"
                    + "time VARCHAR(8) NOT NULL,"
                    + "PRIMARY KEY (requestID))";
            stmt.executeUpdate(query);
            System.out.println("External Transportation Request table created");
        } catch (SQLException e) {

        }
    }

    private static void createFoodRequestTable(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            String query = "CREATE TABLE FoodRequest("
                    + "requestID INT NOT NULL REFERENCES ServiceRequest(requestID),"
                    + "userName VARCHAR(20) NOT NULL REFERENCES HospitalUser(userName),"
                    + "name VARCHAR(25) NOT NULL,"
                    + "selectFood VARCHAR(200) NOT NULL,"
                    + "selectSide VARCHAR(20) NOT NULL,"
                    + "selectDrink VARCHAR(50) NOT NULL,"
                    + "location VARCHAR(10) REFERENCES Nodes(nodeID),"
                    + "fulfilledBy VARCHAR(20) NOT NULL REFERENCES Employee(userName),"
                    + "isComplete VARCHAR(5) NOT NULL,"
                    + "selectDate VARCHAR(10) NOT NULL,"
                    + "selectTime VARCHAR(10) NOT NULL,"
                    + "specialInstructions VARCHAR(100) NOT NULL,"
                    + "PRIMARY KEY (requestID))";
            stmt.executeUpdate(query);
            System.out.println("Food Request table created");
        } catch (SQLException e) {

        }
    }


    private static void createInterpreterRequestTable(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            String query = "CREATE TABLE InterpreterRequest("
                    + "requestID INT NOT NULL REFERENCES ServiceRequest(requestID),"
                    + "userName VARCHAR(20) NOT NULL REFERENCES HospitalUser(userName),"
                    + "name VARCHAR(100) NOT NULL,"
                    + "language VARCHAR(50) NOT NULL,"
                    + "location VARCHAR(10) REFERENCES Nodes(nodeID),"
                    + "requestDescription VARCHAR(200),"
                    + "fulfilledBy VARCHAR(20) NOT NULL REFERENCES Employee(userName),"
                    + "isComplete VARCHAR(5) NOT NULL,"
                    + "PRIMARY KEY (requestID))";

            stmt.executeUpdate(query);
            System.out.println("External Transportation Request table created");
        } catch (SQLException e) {

        }
    }

    private static void createInternalPatientRequestTable(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            String query = "CREATE TABLE InternalPatientRequest("
                    + "requestID INT NOT NULL REFERENCES ServiceRequest(requestID),"
                    + "userName VARCHAR(20) NOT NULL REFERENCES HospitalUser(userName),"
                    + "patientID VARCHAR(100) NOT NULL,"
                    + "location VARCHAR(10) REFERENCES Nodes(nodeID),"
                    + "destination VARCHAR(10) NOT NULL REFERENCES Nodes(nodeID),"
                    + "transportType VARCHAR(50) NOT NULL,"
                    + "time VARCHAR(50) NOT NULL,"
                    + "transportReason VARCHAR(100) NOT NULL,"
                    + "isComplete VARCHAR(5) NOT NULL,"
                    + "fulfilledBy VARCHAR(20) NOT NULL REFERENCES Employee(userName),"
                    + "PRIMARY KEY (requestID))";
            stmt.executeUpdate(query);
            System.out.println("Internal Transportation Request table created");
        } catch (SQLException e) {

        }
    }

    private static void createMedicineDeliveryRequestTable(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            String query = "CREATE TABLE MedicineDeliveryRequest("
                    + "requestID INT NOT NULL REFERENCES ServiceRequest(requestID),"
                    + "userName VARCHAR(20) NOT NULL REFERENCES HospitalUser(userName),"
                    + "patientID VARCHAR(100) NOT NULL,"
                    + "medication VARCHAR(200) NOT NULL,"
                    + "dosage VARCHAR(100) NOT NULL,"
                    + "deliveryTime VARCHAR(100) NOT NULL,"
                    + "deliveryDate VARCHAR(100) NOT NULL,"
                    + "instructions VARCHAR(100) NOT NULL,"
                    + "location VARCHAR(10) REFERENCES Nodes(nodeID),"
                    + "fulfilledBy VARCHAR(20) NOT NULL REFERENCES Employee(userName),"
                    + "isComplete VARCHAR(5) NOT NULL,"
                    + "PRIMARY KEY (requestID))";
            stmt.executeUpdate(query);
            System.out.println("Medicine Delivery Request table created");
        } catch (SQLException e) {

        }
    }

    private static void createFloralDeliveryRequestTable(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            String query = "CREATE TABLE FloralDeliveryRequest("
                    + "requestID INT NOT NULL REFERENCES ServiceRequest(requestID),"
                    + "userName VARCHAR(20) NOT NULL REFERENCES HospitalUser(userName),"
                    + "location VARCHAR(10) REFERENCES Nodes(nodeID),"
                    + "patientName VARCHAR(100) NOT NULL,"
                    + "flowerType VARCHAR(25) NOT NULL,"
                    + "flowerNo VARCHAR(3) NOT NULL,"
                    + "notes VARCHAR(200) NOT NULL,"
                    + "fulfilledBy VARCHAR(20) NOT NULL REFERENCES Employee(userName),"
                    + "isComplete VARCHAR(5) NOT NULL,"
                    + "PRIMARY KEY (requestID))";
            stmt.executeUpdate(query);
            System.out.println("Floral Delivery Request table created");
        } catch (SQLException e) {

        }
    }

    private static void createCounselingRequestTable(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            String query = "CREATE TABLE CounselingRequest("
                    + "requestID INT NOT NULL REFERENCES ServiceRequest(requestID),"
                    + "userName VARCHAR(20) NOT NULL REFERENCES HospitalUser(userName),"
                    + "patientName VARCHAR(100) NOT NULL,"
                    + "counselingType VARCHAR(100) NOT NULL,"
                    + "location VARCHAR(10) REFERENCES Nodes(nodeID),"
                    + "otherInfo VARCHAR(200),"
                    + "fulfilledBy VARCHAR(20) NOT NULL REFERENCES Employee(userName),"
                    + "isComplete VARCHAR(5) NOT NULL,"
                    + "PRIMARY KEY (requestID))";
            stmt.executeUpdate(query);
            System.out.println("Counseling Request table created");
        } catch (SQLException e) {

        }
    }

    private static void createMaintenanceRequestTable(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            String query = "CREATE TABLE MaintenanceRequest("
                    + "requestID INT NOT NULL REFERENCES ServiceRequest(requestID),"
                    + "userName VARCHAR(20) NOT NULL REFERENCES HospitalUser(userName),"
                    + "name VARCHAR(50) NOT NULL,"
                    + "location VARCHAR(10) REFERENCES Nodes(nodeID),"
                    + "fulfilledBy VARCHAR(20) NOT NULL REFERENCES Employee(userName),"
                    + "summary VARCHAR(100) NOT NULL,"
                    + "isComplete VARCHAR(5) NOT NULL,"
                    + "PRIMARY KEY (requestID))";
            stmt.executeUpdate(query);
            System.out.println("Maintenance Request table created");
        } catch (SQLException e) {

        }
    }

    private static void createCovidSurveyTable(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            String query = "CREATE TABLE CovidSurvey("
                    + "surveyID INT NOT NULL GENERATED ALWAYS AS IDENTITY,"
                    + "patientName VARCHAR(100) NOT NULL,"
                    + "userName VARCHAR(20) NOT NULL REFERENCES HospitalUser(userName),"
                    + "positiveTest VARCHAR(200) NOT NULL,"
                    + "symptoms VARCHAR(200) NOT NULL,"
                    + "closeContact VARCHAR(200) NOT NULL,"
                    + "selfIsolate VARCHAR(200) NOT NULL,"
                    + "feelGood VARCHAR(200) NOT NULL,"
                    + "receivedVaccine VARCHAR(200),"
                    + "assignTo VARCHAR(20) NOT NULL REFERENCES HospitalUser(userName),"
                    + "entryType VARCHAR(10),"
                    + "isClear VARCHAR(5),"
                    + "PRIMARY KEY (surveyID))";
            stmt.executeUpdate(query);
            System.out.println("Covid survey table created");
        } catch (SQLException e) {

        }
    }

    private static void creatEmergencyRequestTable(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            String query = "CREATE TABLE EmergencyRequest("
                    + "requestID INT NOT NULL REFERENCES ServiceRequest(requestID),"
                    + "userName VARCHAR(20) NOT NULL REFERENCES HospitalUser(userName),"
                    + "name VARCHAR(50),"
                    + "location VARCHAR(10) REFERENCES Nodes(nodeID),"
                    + "fulfilledBy VARCHAR(20) NOT NULL REFERENCES Employee(userName),"
                    + "summary VARCHAR(100),"
                    + "isComplete VARCHAR(5) NOT NULL,"
                    + "emergencyType VARCHAR(20) NOT NULL,"
                    + "PRIMARY KEY (requestID))";
            stmt.executeUpdate(query);
            System.out.println("Emergency Request table created");
        } catch (SQLException e) {

        }
    }

}
