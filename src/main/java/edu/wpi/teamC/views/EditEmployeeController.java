package edu.wpi.teamC.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamC.Capp;
import edu.wpi.teamC.Translate;
import edu.wpi.teamC.entities.Employee;
import edu.wpi.teamC.repository.*;
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
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;

public class EditEmployeeController {
    @FXML private Label editEmployeeHeader;
    @FXML private JFXTextField oldEmail;
    @FXML private JFXTextField username;
    @FXML private JFXTextField newEmail;
    @FXML private JFXTextField newLastName;
    @FXML private JFXTextField oldFirstName;
    @FXML private JFXTextField newFirstName;
    @FXML private JFXTextField newPassword;
    @FXML private JFXTextField oldPassword;
    @FXML private JFXTextField oldLastName;
    @FXML private JFXButton Emergency;
    @FXML private Label userNameText;
    @FXML private Label emailText;
    @FXML private Label passwordText;
    @FXML private Label lastNameText;
    @FXML private Label firstNameText;
    @FXML private JFXButton back;
    @FXML private JFXButton submitButton;
    @FXML private JFXButton logOut;
    @FXML
    private Rectangle r1;
    @FXML
    private Rectangle r2;
    @FXML
    private HBox hbox1;
    @FXML
    private HBox hbox2;
    @FXML
    private AnchorPane anchor;
    @FXML
    private ImageView bwh;
    @FXML private VBox vbox;


    ConnectionSingleton c = ConnectionSingleton.getInstance();
    Connection conn = c.conn;
    DBAccess database = new Database(conn);
    static LanguageSingleton lang = LanguageSingleton.getInstance();
    String language = lang.s;

    @FXML private Label timerLabel;
    private Timeline clockTimeline;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
    private StringProperty time = new SimpleStringProperty("");
    private void updateTime() {
        time.set(timeFormat.format(Calendar.getInstance().getTime()));
    }

    @FXML
    public void initialize() throws SQLException, IOException {
        translateInit();
        clockTimeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> updateTime()));
        clockTimeline.setCycleCount(Animation.INDEFINITE);
        clockTimeline.play();
        timerLabel.textProperty().bind(time);

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
        Emergency.prefWidthProperty().bind(anchor.widthProperty().multiply(85).divide(1280));
        AnchorPane.setRightAnchor(Emergency, anchor.getPrefWidth() * 94 / 1280);

        //width and right anchor for exit button
        logOut.prefWidthProperty().bind(anchor.widthProperty().multiply(85).divide(1280));
        AnchorPane.setRightAnchor(logOut, anchor.getPrefWidth() * 94 / 1280);

        vbox.prefHeightProperty().bind(anchor.heightProperty().multiply(522).divide(680));
        AnchorPane.setBottomAnchor(vbox, anchor.getPrefHeight()*106/680);
        AnchorPane.setTopAnchor(vbox, anchor.getPrefHeight()*215/680);

        hbox2.prefHeightProperty().bind(anchor.heightProperty().multiply(100).divide(680));

    }

    private void translateInit(){
        LinkedList<JFXTextField> textToTranslate=new LinkedList<>();
        textToTranslate.add(oldEmail);
        textToTranslate.add(username);
        textToTranslate.add( newEmail);
        textToTranslate.add( newLastName);
        textToTranslate.add( oldFirstName);
        textToTranslate.add( newFirstName);
        textToTranslate.add( oldLastName);
        textToTranslate.add( newPassword);
        textToTranslate.add( oldPassword);
        String mainTranslation="";
        for(JFXTextField t:textToTranslate){
            mainTranslation=mainTranslation+" ==== "+t.getPromptText();
        }
        try {
            mainTranslation= Translate.translate("en",lang.s,mainTranslation);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] translatedStrings=mainTranslation.split(" ==== ");

        for(int i=0;i<textToTranslate.size();i++){
            System.out.println(translatedStrings[i+1]);
            textToTranslate.get(i).setPromptText(translatedStrings[i+1]);
        }

        LinkedList<JFXButton> buttonToTranslate=new LinkedList<>();

        buttonToTranslate.add(Emergency);
        buttonToTranslate.add(back);
        buttonToTranslate.add(submitButton);
        buttonToTranslate.add(logOut);

        mainTranslation="";
        for(Button b:buttonToTranslate){
            mainTranslation=mainTranslation+" ==== "+b.getText();
        }
        try {
            mainTranslation=Translate.translate("en",lang.s,mainTranslation);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(mainTranslation);
        translatedStrings=mainTranslation.split(" ==== ");

        for(int i=0;i<buttonToTranslate.size();i++){
            System.out.println(translatedStrings[i+1]);
            buttonToTranslate.get(i).setText(translatedStrings[i+1]);
        }

        LinkedList<Label> labelToTranslate=new LinkedList<>();

        labelToTranslate.add(editEmployeeHeader);
        labelToTranslate.add(userNameText);
        labelToTranslate.add(emailText);
        labelToTranslate.add(passwordText);
        labelToTranslate.add(lastNameText);
        labelToTranslate.add(firstNameText);

        mainTranslation="";
        for(Label b:labelToTranslate){
            mainTranslation=mainTranslation+" ==== "+b.getText();
        }
        try {
            mainTranslation=Translate.translate("en",lang.s,mainTranslation);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(mainTranslation);
        translatedStrings=mainTranslation.split(" ==== ");

        for(int i=0;i<labelToTranslate.size();i++){
            System.out.println(translatedStrings[i+1]);
            labelToTranslate.get(i).setText(translatedStrings[i+1]);
        }

    }

    @FXML
    private void goBack(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/ListEmployee.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void goEmergency(ActionEvent actionEvent) {
        try {
            Capp.prevPage = "/edu/wpi/teamC/fxml/EditEmployee.fxml";
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/serviceRequest/Emergency.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void validateSubmit(){
        if(username.getText().isEmpty() || oldEmail.getText().isEmpty() || newEmail.getText().isEmpty() || oldPassword.getText().isEmpty() || newPassword.getText().isEmpty() ||
        oldFirstName.getText().isEmpty() || newFirstName.getText().isEmpty() || oldLastName.getText().isEmpty() || newLastName.getText().isEmpty()) {
            submitButton.setDisable(true);
        } else {
            submitButton.setDisable(false);
        }
    }

    @FXML
    private void goSubmit(ActionEvent e) {
        username.setEditable(false);
        String user = username.getText();
        String email = newEmail.getText();
        String password = newPassword.getText();
        String first = newFirstName.getText();
        String last = newLastName.getText();

        database.updateUserEmail(user,email);
        database.updatePassword(user,password);
        database.updateFirstName(user,first);
        database.updateLastName(user, last);
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/ListEmployee.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void receiveData(Employee employee) {
        System.out.println(employee);
        username.setText(employee.getUserName());
        oldEmail.setText(employee.getUserEmail());
        oldPassword.setText(employee.getPassword());
        oldFirstName.setText(employee.getFirstName());
        oldLastName.setText(employee.getLastName());
    }

    @FXML
    private void goHome(ActionEvent e) {
        if (Capp.userType.equals("Admin")) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/DefaultScreen.fxml"));
                Capp.getPrimaryStage().getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @FXML
    protected void onlogOut(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/StartUp.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
