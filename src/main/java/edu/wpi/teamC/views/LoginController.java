package edu.wpi.teamC.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamC.Capp;
import edu.wpi.teamC.Translate;
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
import javafx.scene.control.Alert;
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
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;

public class LoginController {
    private static String correctUser;
    @FXML private JFXTextField userBox;
    @FXML private JFXPasswordField passBox;
    @FXML private Label loginHeader;
    @FXML private Text userNameText;
    @FXML private Text passwordText;
    @FXML private JFXButton emergency;
    @FXML private JFXButton Login;
    @FXML private JFXButton exit;
    @FXML private JFXButton logoButton;
    @FXML private AnchorPane anchor;
    @FXML
    private Rectangle r1;
    @FXML
    private Rectangle r2;
    @FXML
    private HBox hbox1;
    @FXML
    private HBox hbox2;
    @FXML
    private HBox hbox3;
    @FXML
    private ImageView bwh;
    @FXML private VBox vbox;


    ConnectionSingleton c = ConnectionSingleton.getInstance();
    Connection conn = c.conn;
    DBAccess database = new Database(conn);
    Translate tr = new Translate();
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

        //width and right anchor for exit button
        exit.prefWidthProperty().bind(anchor.widthProperty().multiply(85).divide(1280));
        AnchorPane.setRightAnchor(exit, anchor.getPrefWidth() * 94 / 1280);

        hbox2.prefHeightProperty().bind(anchor.heightProperty().multiply(100).divide(680));

        vbox.prefHeightProperty().bind(anchor.heightProperty().multiply(200).divide(680));
        AnchorPane.setBottomAnchor(vbox, anchor.getPrefHeight()*202/680);
        AnchorPane.setTopAnchor(vbox, anchor.getPrefHeight()*278/680);

    }

    public static void setCorrectUser(String username) {
        correctUser = username;
    }

    public static String getCorrectUser() {return correctUser;}

    private void translateInit() throws IOException {
        loginHeader.setText(tr.translate("en", language, loginHeader.getText()));
        passBox.setPromptText(tr.translate("en", language, passBox.getPromptText()));

        LinkedList<JFXTextField> textToTranslate=new LinkedList<>();
        textToTranslate.add(userBox);
        String mainTranslation="";
        for(JFXTextField t:textToTranslate){
            mainTranslation=mainTranslation+" ==== "+t.getPromptText();
        }
        try {
            mainTranslation= Translate.translate("en",language,mainTranslation);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] translatedStrings=mainTranslation.split(" ==== ");

        for(int i=0;i<textToTranslate.size();i++){
            System.out.println(translatedStrings[i+1]);
            textToTranslate.get(i).setPromptText(translatedStrings[i+1]);
        }

        LinkedList<JFXButton> buttonToTranslate=new LinkedList<>();

        buttonToTranslate.add(emergency);
        buttonToTranslate.add(Login);
        buttonToTranslate.add(exit);

        mainTranslation="";
        for(Button b:buttonToTranslate){
            mainTranslation=mainTranslation+" ==== "+b.getText();
        }
        try {
            mainTranslation=Translate.translate("en",language,mainTranslation);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(mainTranslation);
        translatedStrings=mainTranslation.split(" ==== ");

        for(int i=0;i<buttonToTranslate.size();i++){
            System.out.println(translatedStrings[i+1]);
            buttonToTranslate.get(i).setText(translatedStrings[i+1]);
        }

        LinkedList<Text> labelToTranslate=new LinkedList<>();

        labelToTranslate.add(userNameText);
        labelToTranslate.add(passwordText);

        mainTranslation="";
        for(Text b:labelToTranslate){
            mainTranslation=mainTranslation+" ==== "+b.getText();
        }
        try {
            mainTranslation=Translate.translate("en",language,mainTranslation);
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
    private void goLogin(ActionEvent e) throws SQLException {
        Statement stmt = conn.createStatement();

        if (database.checkAdminLogin(userBox.getText(), passBox.getText())) {
            try {
                this.setCorrectUser(userBox.getText());
                Capp.userType = "Admin";
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/DefaultScreen.fxml"));
                Capp.getPrimaryStage().getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else if(database.checkEmployeeLogin(userBox.getText(),passBox.getText())){
            try {
                this.setCorrectUser(userBox.getText());
                Capp.userType = "Employee";
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/DefaultScreenEmployee.fxml"));
                Capp.getPrimaryStage().getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else if(database.checkPatientLogin(userBox.getText(), passBox.getText())) {
            try {
                this.setCorrectUser(userBox.getText());
                Capp.userType = "Patient";
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/DefaultScreenGuest.fxml"));
                Capp.getPrimaryStage().getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid user/pass!");
            alert.setHeaderText("Not a valid username/password combo.");
            alert.showAndWait();
        }
    }

    @FXML
    private void goGuestHome(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/DefaultScreenGuest.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void goStartUp(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/StartUp.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void validateSubmit(){
        if(userBox.getText().isEmpty() || passBox.getText().isEmpty()) {
            Login.setDisable(true);
        } else {
            Login.setDisable(false);
        }
    }

    @FXML
    public void goEmergency(ActionEvent actionEvent) {
        try {
            Capp.prevPage = "/edu/wpi/teamC/fxml/DefaultScreen.fxml";
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/serviceRequest/Emergency.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void exitApp(ActionEvent actionEvent) {
        System.exit(0);
    }

}
