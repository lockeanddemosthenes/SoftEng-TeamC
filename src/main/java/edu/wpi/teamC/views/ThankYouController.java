package edu.wpi.teamC.views;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamC.Capp;
import edu.wpi.teamC.Translate;
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
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;

public class ThankYouController {
    static LanguageSingleton lang = LanguageSingleton.getInstance();
    String language = lang.s;

    @FXML private Label formHeader;
    @FXML private Text formPara;
    @FXML private JFXButton Emergency;
    @FXML private JFXButton logOut;
    @FXML private JFXButton homeButton;
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
    private ImageView bwh;
    @FXML private VBox vbox;
    @FXML private ImageView pic;

    @FXML private Label timerLabel;
    private Timeline clockTimeline;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
    private StringProperty time = new SimpleStringProperty("");
    Translate tr = new Translate();
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
        Emergency.prefWidthProperty().bind(anchor.widthProperty().multiply(85).divide(1280));
        AnchorPane.setRightAnchor(Emergency, anchor.getPrefWidth() * 94 / 1280);

        //width and right anchor for exit button
        logOut.prefWidthProperty().bind(anchor.widthProperty().multiply(85).divide(1280));
        AnchorPane.setRightAnchor(logOut, anchor.getPrefWidth() * 94 / 1280);

        vbox.prefHeightProperty().bind(anchor.heightProperty().multiply(522).divide(680));
        AnchorPane.setBottomAnchor(vbox, anchor.getPrefHeight()*106/680);
        AnchorPane.setTopAnchor(vbox, anchor.getPrefHeight()*215/680);

        hbox2.prefHeightProperty().bind(anchor.heightProperty().multiply(522).divide(680));

        pic.fitHeightProperty().bind(anchor.heightProperty().multiply(522).divide(680));
        pic.fitWidthProperty().bind(anchor.widthProperty());

    }

    private void translateInit() throws IOException {
        formHeader.setText(tr.translate("en", language, formHeader.getText()));
        formPara.setText(tr.translate("en", language, formPara.getText()));

        LinkedList<JFXButton> buttonToTranslate = new LinkedList<>();

        buttonToTranslate.add(Emergency);
        buttonToTranslate.add(logOut);
        buttonToTranslate.add(homeButton);
        String mainTranslation="";
        mainTranslation = "";
        for (Button b : buttonToTranslate) {
            mainTranslation = mainTranslation + " ==== " + b.getText();
        }
        try {
            mainTranslation = Translate.translate("en", language, mainTranslation);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] translatedStrings=mainTranslation.split(" ==== ");
        System.out.println(mainTranslation);
        translatedStrings = mainTranslation.split(" ==== ");

        for (int i = 0; i < buttonToTranslate.size(); i++) {
            System.out.println(translatedStrings[i + 1]);
            buttonToTranslate.get(i).setText(translatedStrings[i + 1]);
        }
    }

    @FXML
    private void goHome(ActionEvent e) {
        if(Capp.userType == "Admin") {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/DefaultScreen.fxml"));
                Capp.getPrimaryStage().getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else if(Capp.userType == "Employee") {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/DefaultScreenEmployee.fxml"));
                Capp.getPrimaryStage().getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/DefaultScreenGuest.fxml"));
                Capp.getPrimaryStage().getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void goEmergency(ActionEvent actionEvent) {
        try {
            Capp.prevPage = "/edu/wpi/teamC/fxml/ThankYou.fxml";
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/serviceRequest/Emergency.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    protected void logOut(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/StartUp.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
