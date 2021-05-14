package edu.wpi.teamC.views.serviceRequest;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamC.Capp;
import edu.wpi.teamC.SendEmail;
import edu.wpi.teamC.Translate;
import edu.wpi.teamC.entities.CaretakerSingleton;
import edu.wpi.teamC.entities.requests.CovidSurvey;
import edu.wpi.teamC.entities.requests.ExternalPatientRequest;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CovidSurveyController {


    @FXML private JFXButton home;
    @FXML private Label covidSurveyLabel;
    @FXML private Text nameText;
    @FXML private Text directionsText;
    //@FXML private JFXButton helpButton;
    @FXML private JFXButton submitButton;
    @FXML private JFXButton Emergency;
    @FXML private JFXCheckBox positiveTestCheck;
    @FXML private JFXCheckBox selfIsolateCheck;
    @FXML private JFXCheckBox COVIDSymptomsCheck;
    @FXML private JFXCheckBox closeContactCheck;
    @FXML private JFXCheckBox feelGoodCheck;
    @FXML private JFXCheckBox vaccineCheck;
    @FXML private JFXTextField nameBox;
    @FXML private JFXButton covidInfo;
    @FXML private AnchorPane anchor;
    private String currentUser = LoginController.getCorrectUser();
    ConnectionSingleton c = ConnectionSingleton.getInstance();
    Connection conn = c.conn;
    DBAccess database = new Database(conn);
    CaretakerSingleton s = CaretakerSingleton.getInstance();
    static LanguageSingleton lang = LanguageSingleton.getInstance();
    String language = lang.s;
    Timeline timeline;
    String positiveTest;
    String symptoms;
    String closeContact;
    String selfIsolate;
    String feelGood;
    String receivedVaccine;
    Translate tr = new Translate();

    @FXML private Label timerLabel;
    private Timeline clockTimeline;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
    private StringProperty time = new SimpleStringProperty("");
    private void updateTime() {
        time.set(timeFormat.format(Calendar.getInstance().getTime()));
    }

    public void initialize() throws SQLException {
        try {
            ArrayList<ButtonBase> buttons = new ArrayList<ButtonBase>();
            //buttons.add(helpButton);
            buttons.add(submitButton);
            buttons.add(Emergency);
            buttons.add(home);
            buttons.add(positiveTestCheck);
            buttons.add(selfIsolateCheck);
            buttons.add(COVIDSymptomsCheck);
            buttons.add(closeContactCheck);
            buttons.add(feelGoodCheck);
            buttons.add(vaccineCheck);
            buttons.add(covidInfo);
            tr.translateButtonList(buttons);
            //Text
            ArrayList<Text> text = new ArrayList<Text>();
            text.add(nameText);
            text.add(directionsText);
            tr.translateTextList(text);
            //TextFields
            ArrayList<TextField> jfxTextFields = new ArrayList<TextField>();
            jfxTextFields.add(nameBox);
            tr.translateTextFieldList(jfxTextFields);
            // Label
            covidSurveyLabel.setText(tr.translate("en", language, covidSurveyLabel.getText()));


        } catch (IOException e) {
            e.printStackTrace();
        }

        clockTimeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> updateTime()));
        clockTimeline.setCycleCount(Animation.INDEFINITE);
        clockTimeline.play();
        timerLabel.textProperty().bind(time);

        anchor.setBottomAnchor(timerLabel, 0.0);
        anchor.setRightAnchor(timerLabel, 0.0);
    }

    private void getUserInput() {
        if (this.positiveTestCheck.isSelected()) {
            positiveTest = this.positiveTestCheck.getText();
        }
        else {
            positiveTest = "I have NOT had a symptomatic COVID-19 test or received a positive test result for COVID-19 in the last 14 days.";
        }
        if (this.COVIDSymptomsCheck.isSelected()) {
            symptoms = this.COVIDSymptomsCheck.getText();
        }
        else {
            symptoms = "I am NOT experiencing COVID-19 like symptoms.";
        }
        if (this.closeContactCheck.isSelected()) {
            closeContact = this.closeContactCheck.getText();
        }
        else {
            closeContact = "I have NOT been in close contact with someone diagnosed with COVID-19 in the last 14 days.";
        }
        if (this.closeContactCheck.isSelected()) {
            selfIsolate = this.selfIsolateCheck.getText();
        }
        else {
            selfIsolate = "I have NOT been asked to self-isolate or quarantine in the last 14 days.";
        }
        if (this.feelGoodCheck.isSelected()) {
            feelGood = this.feelGoodCheck.getText();
        }
        else {
            feelGood = "I do NOT feel good.";
        }
        if (this.vaccineCheck.isSelected()) {
            receivedVaccine = this.vaccineCheck.getText();
        }
        else {
            receivedVaccine = "I have NOT received the COVID-19 vaccine.";
        }
    }

    private void addCovidSurveyResult() throws SQLException {
        getUserInput();
        String patientName = nameBox.getText();
        System.out.println(patientName);
        CovidSurvey cs = new CovidSurvey(patientName, currentUser, positiveTest, symptoms, closeContact, selfIsolate, feelGood, receivedVaccine);
        database.addCovidSurvey(cs);
    }

    private void sendEmail() throws MessagingException {
        String email;
        if(currentUser == null || currentUser.equals("guest")){
            currentUser = "guest";
            email = "hgsmith@wpi.edu";
        }else{
            email = database.getUserEmail(currentUser);
        }
        SendEmail se = new SendEmail(email,"brighamandwomenshospital1@gmail.com");
        se.setMessageSubject("Covid Survey");
        se.setMessageText("Hello "+ currentUser +", " +
                "\n " +
                "\nYou have filled out a covid survey!" +
                "\nPlease wait to enter the building until your survey has been reviewed." +
                "\nThank you!" +
                "\n" +
                "\n-Team C");
        se.sendMessage();
    }

    public void goEmergency(ActionEvent actionEvent) {
        try {
            Capp.prevPage = "/edu/wpi/teamC/fxml/serviceRequest/CovidSurvey.fxml";
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/serviceRequest/Emergency.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void validateSubmit(){
        if(nameBox.getText().isEmpty()) {
            submitButton.setDisable(true);
        } else {
            submitButton.setDisable(false);
        }
    }

    @FXML
    private void goHome(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/DefaultScreenGuest.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void goHelp(ActionEvent actionEvent) {
        //currently directs to previous page
        try {
            Parent root = FXMLLoader.load(getClass().getResource(Capp.prevPage));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void goCovidInfo(ActionEvent event){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/CovidInfo.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
          //  timeline.stop();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void goSubmit(ActionEvent event) throws SQLException, MessagingException {
        addCovidSurveyResult();
        sendEmail();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/serviceRequest/PostCovidSurvey.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
           // timeline.stop();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void logOut(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/StartUp.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
         //   timeline.stop();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
