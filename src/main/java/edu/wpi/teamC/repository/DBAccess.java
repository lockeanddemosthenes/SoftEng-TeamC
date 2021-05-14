package edu.wpi.teamC.repository;


import com.sun.org.apache.xpath.internal.operations.Bool;
import edu.wpi.teamC.entities.Employee;
import edu.wpi.teamC.entities.ExternalAddress;
import edu.wpi.teamC.entities.Patient;
import edu.wpi.teamC.entities.requests.*;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public interface DBAccess {
    Connection conn = null;

    void setConn(Connection conn);

    void displayAllNodes();

    void addNode(NodeDB node);

    void updateNodeX(int x, String nodeID);

    void updateNodeY(int y, String nodeID);

    void updateNodeFloor(String f, String nodeID);

    void updateNodeBuilding(String b, String nodeID);

    void updateNodeType(String t, String nodeID);

    void updateNodeLongName(String l, String nodeID);

    void updateNodeShortName(String s, String nodeID);

    ArrayList<NodeDB> getListOfNodes() throws SQLException;

    void removeNode(String nodeID);

    void deleteNodes() throws SQLException;

    void saveNodes() throws IOException;

    NodeDB getCorrectNode(String nodeID);

    void addEdge(EdgeDB edge);

    Boolean databaseContainsEdge(EdgeDB edge) throws SQLException;

    void fileContainsEdge(File file, EdgeDB edge) throws SQLException;

    void resetEdges(File file) throws SQLException;

    void removeServiceRequestsWithNode(String nodeID) throws SQLException;

    Boolean databaseContainsNode(NodeDB node) throws SQLException;

    void fileContainsNode(File file, NodeDB node) throws SQLException;

    ArrayList<EdgeDB> getListofEdgesFromNode(String nodeID) throws SQLException;

        void resetNodes(File file) throws SQLException;

    void updateStartNode(String id, String start);

    void updateEndNode(String id, String end);

    void displayAllEdges();

    void removeEdge(String edgeID);

    ArrayList<EdgeDB> getListofEdges() throws SQLException;

    void deleteEdges() throws SQLException;

    void saveEdges() throws IOException;

    void addServiceRequest(ServiceRequest sr);

    void completeRequest(ServiceRequest sr);

    void incompleteRequest(ServiceRequest sr);

    void addCovidSurvey(CovidSurvey covidSurvey);

    void markForNormalEntry(CovidSurvey covidSurvey);

    void markForCOVIDEntry(CovidSurvey covidSurvey);

    int getRequestID(String assignTo);

    ArrayList<ServiceRequest> getRequestsFromUser(String userName);

    ArrayList<CovidSurvey> getAssignedSurveys(String userName);

    ArrayList<Employee> getListOfEmployees() throws SQLException;

    void addEmployee(Employee employee);

    void updateFirstName(String username, String firstName);

    void updateLastName(String username, String lastName);

    void updateUserEmail(String username, String email);

    void updatePassword(String username, String pass);

    void removeEmployee(String username);

    void addAddress(ExternalAddress ea);

    int getAddressID(String street);

    void addExternalPatientRequest(ExternalPatientRequest er);

    void addMedicineDeliveryRequest(MedicineDeliveryRequest mr);

    void addSanitationRequest(SanitationRequest sr);

    void addInternalPatientRequest(InternalPatientRequest ir);

    boolean checkPatientLogin(String userBoxText, String passBoxText) throws SQLException;

    boolean checkAdminLogin(String userBoxText, String passBoxText) throws SQLException;

    boolean checkEmployeeLogin(String userBoxText, String passBoxText) throws SQLException;

    void addCounselingRequest(CounselingRequest cr);

    void addInterpreterRequest(InterpreterRequest ir);

    void addFloralDeliveryRequest(FloralDeliveryRequest fr);

    void addMaintenanceRequest(MaintenanceRequest mr);

    void addFoodRequest(FoodRequest fr);

    String getEntryType(String p);

    void markClearForEntry(CovidSurvey selectedSurvey);

    void markUnclearForEntry(CovidSurvey selectedSurvey);

    void addPatient(Patient patient);

    void addEmergencyRequest(EmergencyRequest er);
    String getUserEmail(String username);
}
