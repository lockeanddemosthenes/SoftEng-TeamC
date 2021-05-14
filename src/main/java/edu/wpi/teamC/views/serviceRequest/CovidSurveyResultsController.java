package edu.wpi.teamC.views.serviceRequest;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamC.Capp;
import edu.wpi.teamC.SendEmail;
import edu.wpi.teamC.Translate;
import edu.wpi.teamC.entities.requests.CovidSurvey;
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
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CovidSurveyResultsController {
    @FXML private TreeTableView<CovidSurvey> surveyTable;
    @FXML private JFXButton covidButton;
    @FXML private  JFXButton normalButton;
    @FXML private Label covidSurveyLabel;
    @FXML private JFXButton emergency;
    @FXML private JFXButton clearButton;
    @FXML private JFXButton unclearButton;
    @FXML private JFXButton backButton;
    @FXML private AnchorPane anchor;
    //@FXML private JFXButton help;

    private TreeTableColumn<CovidSurvey, String> nameCol = new TreeTableColumn<>("Patient Name");
    private TreeTableColumn<CovidSurvey, String> usernameCol = new TreeTableColumn<>("Patient Username");
    private TreeTableColumn<CovidSurvey, String> positiveTestCol = new TreeTableColumn<>("Positive Test/Symptomatic Test");
    private TreeTableColumn<CovidSurvey, String> symptomsCol = new TreeTableColumn<>("Has COVID Symptoms");
    private TreeTableColumn<CovidSurvey, String> closeContactCol = new TreeTableColumn<>("Close Contact w/ COVID");
    private TreeTableColumn<CovidSurvey, String> selfIsolateCol = new TreeTableColumn<>("Asked to Self-Isolate");
    private TreeTableColumn<CovidSurvey, String> feelGoodCol = new TreeTableColumn<>("Feel Good");
    private TreeTableColumn<CovidSurvey, String> receivedVaccineCol = new TreeTableColumn<>("Received Vaccine");
    private TreeTableColumn<CovidSurvey, String> entryCol = new TreeTableColumn<>("Normal Entry Allowed?");
    private TreeTableColumn<CovidSurvey, String> clearCol = new TreeTableColumn<>("Cleared to Enter?");

    private TreeTableColumn<edu.wpi.teamC.entities.requests.CovidSurvey, String> isNormal = new TreeTableColumn<>("Normal?");
    private String userName = LoginController.getCorrectUser();

    ArrayList<CovidSurvey> surveyList;

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

        clockTimeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> updateTime()));
        clockTimeline.setCycleCount(Animation.INDEFINITE);
        clockTimeline.play();
        timerLabel.textProperty().bind(time);
        anchor.setBottomAnchor(timerLabel, 0.0);
        anchor.setRightAnchor(timerLabel, 0.0);

        try {
            ArrayList<ButtonBase> buttons = new ArrayList<ButtonBase>();
            buttons.add(covidButton);
            buttons.add(normalButton);
            buttons.add(emergency);
            buttons.add(unclearButton);
            buttons.add(clearButton);
            buttons.add(backButton);
            tr.translateButtonList(buttons);

            // Label
            covidSurveyLabel.setText(tr.translate("en", language, covidSurveyLabel.getText()));

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(LoginController.getCorrectUser());
        System.out.println(userName);
        this.initTable();

        this.loadTreeItems();
    }

    public void initTable() {

        surveyTable.setEditable(false);

        nameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("patientName"));
        usernameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("userName"));
        positiveTestCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("positiveTest"));
        symptomsCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("symptoms"));
        closeContactCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("closeContact"));
        selfIsolateCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("selfIsolate"));
        feelGoodCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("feelGood"));
        receivedVaccineCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("receivedVaccine"));
        entryCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("entryType"));
        clearCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("isClear"));

        nameCol.setCellFactory(TextFieldTreeTableCell.<CovidSurvey>forTreeTableColumn());
        usernameCol.setCellFactory(TextFieldTreeTableCell.<CovidSurvey>forTreeTableColumn());
        positiveTestCol.setCellFactory(TextFieldTreeTableCell.<CovidSurvey>forTreeTableColumn());
        symptomsCol.setCellFactory(TextFieldTreeTableCell.<CovidSurvey>forTreeTableColumn());
        closeContactCol.setCellFactory(TextFieldTreeTableCell.<CovidSurvey>forTreeTableColumn());
        selfIsolateCol.setCellFactory(TextFieldTreeTableCell.<CovidSurvey>forTreeTableColumn());
        feelGoodCol.setCellFactory(TextFieldTreeTableCell.<CovidSurvey>forTreeTableColumn());
        receivedVaccineCol.setCellFactory(TextFieldTreeTableCell.<CovidSurvey>forTreeTableColumn());
        entryCol.setCellFactory(TextFieldTreeTableCell.<CovidSurvey>forTreeTableColumn());
        clearCol.setCellFactory(TextFieldTreeTableCell.<CovidSurvey>forTreeTableColumn());

        surveyTable.getColumns().add(nameCol);
        surveyTable.getColumns().add(usernameCol);
        surveyTable.getColumns().add(positiveTestCol);
        surveyTable.getColumns().add(symptomsCol);
        surveyTable.getColumns().add(closeContactCol);
        surveyTable.getColumns().add(selfIsolateCol);
        surveyTable.getColumns().add(feelGoodCol);
        surveyTable.getColumns().add(receivedVaccineCol);
        surveyTable.getColumns().add(entryCol);
        surveyTable.getColumns().add(clearCol);

    }


    public void loadTreeItems() {
        System.out.println("Loading tree items...");
        TreeItem<CovidSurvey> root = new TreeItem<CovidSurvey>(new CovidSurvey());
        root.setExpanded(true);
        surveyList = database.getAssignedSurveys(userName);
        for (CovidSurvey covidSurvey : surveyList) {
            root.getChildren().add(new TreeItem<CovidSurvey>(covidSurvey));
            System.out.println("COVID Survey " + covidSurvey.getSurveyID() + " added to tree");
        }
        surveyTable.setRoot(root);
        surveyTable.refresh();
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
            Capp.prevPage = "/edu/wpi/teamC/fxml/serviceRequest/CovidSurveyResults.fxml";
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/serviceRequest/Emergency.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void markNormal(ActionEvent actionEvent) throws MessagingException {
        if (surveyTable.getSelectionModel().getSelectedItem() != null) {
            CovidSurvey selectedSurvey = surveyTable.getSelectionModel().getSelectedItem().getValue();
            System.out.println(selectedSurvey);
            System.out.println(surveyTable.getSelectionModel().getSelectedItems());
            System.out.println(selectedSurvey.getSurveyID());
            database.markForNormalEntry(selectedSurvey);
            loadTreeItems();
            sendEmail(selectedSurvey.getUserName());
        }
        surveyTable.refresh();
    }

    @FXML
    public void markCovid(ActionEvent actionEvent) throws MessagingException {
        if (surveyTable.getSelectionModel().getSelectedItem() != null) {
            CovidSurvey selectedSurvey = surveyTable.getSelectionModel().getSelectedItem().getValue();
            System.out.println(selectedSurvey);
            System.out.println(surveyTable.getSelectionModel().getSelectedItems());
            System.out.println(selectedSurvey.getSurveyID());
            database.markForCOVIDEntry(selectedSurvey);
            loadTreeItems();
            sendEmail(selectedSurvey.getUserName());
        }
        surveyTable.refresh();

    }

    private void sendEmail(String user) throws MessagingException {
        System.out.println(user);
        SendEmail se = new SendEmail(database.getUserEmail(user),"brighamandwomenshospital1@gmail.com");
        se.setMessageSubject("Covid Survey");
        se.setMessageText("Hello "+ user +", " +
                "\n " +
                "\nYour survey has been reviewed." +
                "\nPlease use the map to enter through the correct entrance." +
                "\nThank you!" +
                "\n" +
                "\n-Team C");
        se.sendMessage();
    }

    @FXML
    public void markClear(ActionEvent actionEvent) {
        if (surveyTable.getSelectionModel().getSelectedItem() != null) {
            CovidSurvey selectedSurvey = surveyTable.getSelectionModel().getSelectedItem().getValue();
            System.out.println(selectedSurvey);
            System.out.println(surveyTable.getSelectionModel().getSelectedItems());
            System.out.println(selectedSurvey.getSurveyID());
            database.markClearForEntry(selectedSurvey);
            loadTreeItems();
        }
        surveyTable.refresh();
    }

    @FXML
    public void markUnclear(ActionEvent actionEvent){
        if (surveyTable.getSelectionModel().getSelectedItem() != null) {
            CovidSurvey selectedSurvey = surveyTable.getSelectionModel().getSelectedItem().getValue();
            System.out.println(selectedSurvey);
            System.out.println(surveyTable.getSelectionModel().getSelectedItems());
            System.out.println(selectedSurvey.getSurveyID());
            database.markUnclearForEntry(selectedSurvey);
            loadTreeItems();
        }
        surveyTable.refresh();
    }

    public void goBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/DefaultScreenEmployee.fxml"));
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
}
