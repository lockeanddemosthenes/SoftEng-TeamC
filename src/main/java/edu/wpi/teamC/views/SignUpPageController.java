package edu.wpi.teamC.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamC.Capp;
import edu.wpi.teamC.Translate;
import edu.wpi.teamC.entities.Patient;
import edu.wpi.teamC.repository.ConnectionSingleton;
import edu.wpi.teamC.repository.DBAccess;
import edu.wpi.teamC.repository.Database;
import edu.wpi.teamC.repository.LanguageSingleton;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;

public class SignUpPageController {
    @FXML private Rectangle r1;
    @FXML private Rectangle r2;
    @FXML private HBox hbox1;
    @FXML private Text welcome;
    @FXML private JFXTextField firstName;
    @FXML private JFXTextField password;
    @FXML private JFXTextField lastName;
    @FXML private JFXTextField email;
    @FXML private JFXTextField username;
    @FXML private JFXButton back;
    @FXML private JFXButton submit;
    @FXML private JFXButton emergency;
    @FXML private Text firstNameText;
    @FXML private Text lastNameText;
    @FXML private Text emailText;
    @FXML private Text usernameText;
    @FXML private Text passwordText;
    @FXML private AnchorPane anchor;
    @FXML
    private HBox hbox2;
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

    public void initialize() throws IOException {
        clockTimeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> updateTime()));
        clockTimeline.setCycleCount(Animation.INDEFINITE);
        clockTimeline.play();
        timerLabel.textProperty().bind(time);
        anchor.setBottomAnchor(timerLabel, 0.0);
        anchor.setRightAnchor(timerLabel, 0.0);

        translateInit();

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

        vbox.prefHeightProperty().bind(anchor.heightProperty().multiply(400).divide(680));
        AnchorPane.setBottomAnchor(vbox, anchor.getPrefHeight()*105/680);
        AnchorPane.setTopAnchor(vbox, anchor.getPrefHeight()*250/680);

        hbox2.prefHeightProperty().bind(anchor.heightProperty().multiply(100).divide(680));

    }

    private void translateInit() throws IOException {

        LinkedList<TextField> textToTranslate=new LinkedList<>();
        textToTranslate.add(firstName);
        textToTranslate.add(lastName);
        textToTranslate.add(password);
        textToTranslate.add(username);
        textToTranslate.add(email);
        Translate.translateTextFieldList(textToTranslate);

        LinkedList<ButtonBase> buttonToTranslate=new LinkedList<>();

        buttonToTranslate.add(emergency);
        buttonToTranslate.add(submit);
        buttonToTranslate.add(back);
        Translate.translateButtonList(buttonToTranslate);


        LinkedList<Text> labelToTranslate=new LinkedList<>();

        labelToTranslate.add(firstNameText);
        labelToTranslate.add(lastNameText);
        labelToTranslate.add(emailText);
        labelToTranslate.add(usernameText);
        labelToTranslate.add(passwordText);
        labelToTranslate.add(welcome);

        Translate.translateTextList(labelToTranslate);

    }

    public void goBack(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/StartUp.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void addPatient() {
        String userName = username.getText();
        String userEmail = email.getText();
        String pass = password.getText();
        String first = firstName.getText();
        String last = lastName.getText();
        Patient patient = new Patient(userName,userEmail,pass,first,last);
        database.addPatient(patient);
    }

    public void goSubmit(ActionEvent actionEvent) {
        addPatient();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/StartUp.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void goEmergency(ActionEvent actionEvent) {
        try {
            Capp.prevPage = "/edu/wpi/teamC/fxml/SignUpPage.fxml";
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/Emergency.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void validateSubmit(){
        if(username.getText().isEmpty() || email.getText().isEmpty() || password.getText().isEmpty() || firstName.getText().isEmpty() || lastName.getText().isEmpty()) {
            submit.setDisable(true);
        } else {
            submit.setDisable(false);
        }
    }
}
