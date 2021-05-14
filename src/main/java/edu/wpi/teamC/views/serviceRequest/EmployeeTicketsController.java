package edu.wpi.teamC.views.serviceRequest;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamC.Capp;
import edu.wpi.teamC.Translate;
import edu.wpi.teamC.entities.requests.CovidSurvey;
import edu.wpi.teamC.entities.requests.ServiceRequest;
import edu.wpi.teamC.repository.ConnectionSingleton;
import edu.wpi.teamC.repository.DBAccess;
import edu.wpi.teamC.repository.Database;
import edu.wpi.teamC.repository.LanguageSingleton;
import edu.wpi.teamC.views.LoginController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class EmployeeTicketsController {
    @FXML
    private TreeTableView<ServiceRequest> requestTable;
    @FXML private Text serviceRequestsText;
    @FXML private JFXButton emergency;
    @FXML private JFXButton logout;
    @FXML private JFXButton markComplete;
    @FXML private JFXButton markIncomplete;
    @FXML private AnchorPane anchor;
    @FXML
    private Rectangle r1;
    @FXML
    private Rectangle r2;
    @FXML
    private HBox hbox1;
    @FXML
    private ImageView bwh;
    @FXML private VBox vbox;


    //private TreeTableColumn<ServiceRequest, String> requestCol = new TreeTableColumn<>("Request #");
    private TreeTableColumn<ServiceRequest, String> locationCol = new TreeTableColumn<>("Location");
    private TreeTableColumn<ServiceRequest, String> descriptionCol = new TreeTableColumn<>("Description");

    private TreeTableColumn<ServiceRequest, String> isCompletedCol = new TreeTableColumn<>("Completed?");
    private String userName = LoginController.getCorrectUser();

    ArrayList<ServiceRequest> serviceList;

    ConnectionSingleton c = ConnectionSingleton.getInstance();
    Connection conn = c.conn;
    DBAccess database = new Database(conn);

    static LanguageSingleton lang = LanguageSingleton.getInstance();
    String language = lang.s;
    Translate tr = new Translate();

    @FXML private Label timerLabel;
    private Timeline clockTimeline;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
    private StringProperty time = new SimpleStringProperty("");
    private void updateTime() {
        time.set(timeFormat.format(Calendar.getInstance().getTime()));
    }

    @FXML
    public void initialize() throws SQLException {
        try {
            ArrayList<Button> buttons = new ArrayList<Button>();
            buttons.add(markComplete);
            buttons.add(markIncomplete);
            buttons.add(emergency);
            //Text
            serviceRequestsText.setText(tr.translate("en", language, serviceRequestsText.getText()));
            // Buttons
            for (Button button : buttons) {
                if (button != null) {
                    button.setText(tr.translate("en", language, button.getText()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(LoginController.getCorrectUser());
        System.out.println(userName);
        this.initTable();

        this.loadTreeItems();

        clockTimeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> updateTime()));
        clockTimeline.setCycleCount(Animation.INDEFINITE);
        clockTimeline.play();
        timerLabel.textProperty().bind(time);

        anchor.setBottomAnchor(timerLabel, 0.0);
        anchor.setRightAnchor(timerLabel, 0.0);

        //height and width of top rectangle
        r1.widthProperty().bind(anchor.widthProperty());
        r1.heightProperty().bind(anchor.heightProperty().multiply(140).divide(680));

        //height and width of bottom rectangle
        r2.widthProperty().bind(anchor.widthProperty());
        r2.heightProperty().bind(anchor.heightProperty().multiply(522).divide(680));

        //height of top hbox with title in it
        hbox1.prefHeightProperty().bind(anchor.heightProperty().multiply(140).divide(680));

        //width and height for bwh image
        bwh.fitWidthProperty().bind(anchor.widthProperty().multiply(112).divide(1280));
        bwh.fitHeightProperty().bind(anchor.heightProperty().multiply(128).divide(680));

        //width and right anchor for emergency button
        emergency.prefWidthProperty().bind(anchor.widthProperty().multiply(85).divide(1280));
        AnchorPane.setRightAnchor(emergency, anchor.getPrefWidth() * 94 / 1280);

        //width and right anchor for exit button
        logout.prefWidthProperty().bind(anchor.widthProperty().multiply(85).divide(1280));
        AnchorPane.setRightAnchor(logout, anchor.getPrefWidth() * 94 / 1280);

        vbox.prefHeightProperty().bind(anchor.heightProperty().multiply(522).divide(680));
        AnchorPane.setTopAnchor(vbox, anchor.getPrefWidth()*235/1280);

    }

    public void initTable() {

        requestTable.setEditable(false);

        //requestCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("requestID"));
        locationCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("location"));
        descriptionCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("description"));
        isCompletedCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("isComplete"));

        //requestCol.setCellFactory(TextFieldTreeTableCell.<ServiceRequest>forTreeTableColumn());
        locationCol.setCellFactory(TextFieldTreeTableCell.<ServiceRequest>forTreeTableColumn());
        descriptionCol.setCellFactory(TextFieldTreeTableCell.<ServiceRequest>forTreeTableColumn());
        isCompletedCol.setCellFactory(TextFieldTreeTableCell.<ServiceRequest>forTreeTableColumn());

        //requestTable.getColumns().add(requestCol);
        requestTable.getColumns().add(locationCol);
        requestTable.getColumns().add(descriptionCol);
        requestTable.getColumns().add(isCompletedCol);

    }


    public void loadTreeItems() {
        System.out.println("Loading tree items...");
        TreeItem<ServiceRequest> root = new TreeItem<ServiceRequest>(new ServiceRequest());
        root.setExpanded(true);
        serviceList = database.getRequestsFromUser(userName);
        for (ServiceRequest sr : serviceList) {
                root.getChildren().add(new TreeItem<ServiceRequest>(sr));
                System.out.println("ServiceRequest " + sr.getRequestID() + " added to tree");
            }
        requestTable.setRoot(root);
        requestTable.refresh();
    }


    @FXML
    private void goHome(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/DefaultScreenEmployee.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void goEmergency(ActionEvent actionEvent) {
        try {
            Capp.prevPage = "/edu/wpi/teamC/fxml/serviceRequest/EmployeeTickets.fxml";
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/serviceRequest/Emergency.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void goBack(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/serviceRequest/ServiceSystemsEmployee.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void logOut(ActionEvent event) {
        try {
            Capp.userType = "Guest";
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/StartUp.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void markComplete(ActionEvent actionEvent) {
        if (requestTable.getSelectionModel().getSelectedItem() != null) {
            ServiceRequest selectedRequest = requestTable.getSelectionModel().getSelectedItem().getValue();
            System.out.println(selectedRequest);
            System.out.println(requestTable.getSelectionModel().getSelectedItems());
            System.out.println(selectedRequest.getRequestID());
            database.completeRequest(selectedRequest);
            loadTreeItems();
        }
        requestTable.refresh();
    }


        @FXML
        public void markIncomplete (ActionEvent actionEvent){
            if (requestTable.getSelectionModel().getSelectedItem() != null) {
                ServiceRequest selectedRequest = requestTable.getSelectionModel().getSelectedItem().getValue();
                System.out.println(selectedRequest);
                System.out.println(requestTable.getSelectionModel().getSelectedItems());
                System.out.println(selectedRequest.getRequestID());
                database.incompleteRequest(selectedRequest);
                loadTreeItems();
            }
            requestTable.refresh();

        }

    }
