package edu.wpi.teamC.views;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import edu.wpi.teamC.Capp;
import edu.wpi.teamC.Translate;
import edu.wpi.teamC.entities.CaretakerSingleton;
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
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.text.Text;
import javafx.util.Duration;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;

public class AdminConfigController {
    @FXML
    private JFXTextField addressField;
    @FXML
    private JFXTextField portField;
    @FXML
    private JFXTextField dbField;
    @FXML
    private JFXToggleButton serverToggleButton;
    @FXML
    private JFXButton submitButton;

    @FXML
    private Label timerLabel;
    private Timeline clockTimeline;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
    private StringProperty time = new SimpleStringProperty("");

    private void updateTime() {
        time.set(timeFormat.format(Calendar.getInstance().getTime()));
    }

    Timeline timeline;

    ConnectionSingleton c = ConnectionSingleton.getInstance();
    Connection conn = c.conn;
    DBAccess database = new Database(conn);
    CaretakerSingleton s = CaretakerSingleton.getInstance();
    Translate tr = new Translate();

    static LanguageSingleton lang = LanguageSingleton.getInstance();
    String language = lang.s;

    public void initialize() throws IOException {
        s.setPrevTime();
        timeline = new Timeline(new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // check if caretaker prev time is far back from current time
                if ((System.currentTimeMillis() - s.getPrevTime()) > s.getTimeoutSeconds()) {
                    try {
                        Parent root = FXMLLoader.load(getClass().getResource(s.goHome().fxml));
                        //((Node)event.getSource()).getScene().setRoot(root);
                        Capp.getPrimaryStage().getScene().setRoot(root);
                        timeline.stop();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                }
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        clockTimeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> updateTime()));
        clockTimeline.setCycleCount(Animation.INDEFINITE);
        clockTimeline.play();
        timerLabel.textProperty().bind(time);

        addressField.setDisable(true);
        portField.setDisable(true);
        dbField.setDisable(true);
        submitButton.setDisable(true);

        translateInit();
    }

    private void translateInit() throws IOException {
        addressField.setPromptText(tr.translate("en", language, addressField.getPromptText()));
        portField.setPromptText(tr.translate("en", language, portField.getPromptText()));
        dbField.setPromptText(tr.translate("en", language, dbField.getPromptText()));
        serverToggleButton.setText(tr.translate("en", language, serverToggleButton.getText()));
        submitButton.setText(tr.translate("en", language, submitButton.getText()));
    }

    public void goEmergency(ActionEvent actionEvent) {
        try {
            Capp.prevPage = "/edu/wpi/teamC/fxml/serviceRequest/Counseling.fxml";
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/serviceRequest/Emergency.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
            timeline.stop();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void serverModeUpdate() {
        if (serverToggleButton.isSelected()) {
            addressField.setDisable(false);
            portField.setDisable(false);
            dbField.setDisable(false);
            submitButton.setDisable(false);
        }else if(!serverToggleButton.isSelected()){
            c.remoteToEmbedded();
        }
    }

    //TODO: THIS
     @FXML protected void goSubmit(ActionEvent e){
         c.embeddedToRemote(addressField.getText(),portField.getText(),dbField.getText());

     }

    public void validateSubmit(ActionEvent actionEvent) {
        if (addressField.getText().isEmpty() || portField.getText() == null || dbField.getText() == null) {
            submitButton.setDisable(true);
        } else {
            submitButton.setDisable(false);
        }
    }


    public void goHome(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/DefaultScreen.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
            timeline.stop();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void goBack(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/DefaultScreen.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
            timeline.stop();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    protected void onlogOut(ActionEvent actionEvent) {
        try {
            Capp.userType = "Guest";
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/StartUp.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
