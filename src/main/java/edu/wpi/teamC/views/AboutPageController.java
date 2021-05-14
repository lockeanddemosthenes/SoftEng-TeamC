package edu.wpi.teamC.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;

public class AboutPageController {
    static LanguageSingleton lang = LanguageSingleton.getInstance();
    String language = lang.s;

    @FXML private AnchorPane anchor;
    @FXML private Rectangle r2;
    @FXML private Rectangle r1;
    @FXML private Label creditsHeader;
    @FXML private Text creditsInfo;
    @FXML private ImageView bwh;
    @FXML private JFXButton Emergency;
    @FXML private JFXButton home;
    @FXML private Text alexText;
    @FXML private Text alexTitle;
    @FXML private Text maryText;
    @FXML private Text maryTitle;
    @FXML private Text nigelText;
    @FXML private Text nigelTitle;
    @FXML private Text tobyText;
    @FXML private Text tobyTitle;
    @FXML private Text erricaText;
    @FXML private Text erricaTitle;
    @FXML private Text carlieText;
    @FXML private Text carlieTitle;
    @FXML private Text haydenText;
    @FXML private Text haydenTitle;
    @FXML private Text nathanText;
    @FXML private Text nathanTitle;
    @FXML private Text dylanText;
    @FXML private Text dylanTitle;
    Translate tr = new Translate();

    @FXML private Label timerLabel;
    private Timeline clockTimeline;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
    private StringProperty time = new SimpleStringProperty("");
    private void updateTime() {
        time.set(timeFormat.format(Calendar.getInstance().getTime()));
    }

    @FXML
    public void initialize() throws SQLException, IOException {

        //height and width of top rectangle
        r1.widthProperty().bind(anchor.widthProperty());
        r1.heightProperty().bind(anchor.heightProperty().multiply(169).divide(720));

        //height and width of bottom rectangle
        r2.widthProperty().bind(anchor.widthProperty());
        r2.heightProperty().bind(anchor.heightProperty().multiply(535).divide(720));

        //width and height for bwh image
        bwh.fitWidthProperty().bind(anchor.widthProperty().multiply(142).divide(1280));
        bwh.fitHeightProperty().bind(anchor.heightProperty().multiply(161).divide(680));

        //width and right anchor for emergency button
        Emergency.prefWidthProperty().bind(anchor.widthProperty().multiply(85).divide(1280));
        AnchorPane.setRightAnchor(Emergency, anchor.getPrefWidth()*94/1280);

        //width and right anchor for home button
        home.prefWidthProperty().bind(anchor.widthProperty().multiply(85).divide(1280));
        AnchorPane.setRightAnchor(home, anchor.getPrefWidth()*94/1280);

        //height of top hbox with title in it
        //hbox1.prefHeightProperty().bind(anchor.heightProperty().multiply(159).divide(680));
        //AnchorPane.setBottomAnchor(hbox1, anchor.getPrefHeight()*361/680);

        //height and bottom anchor for bottom hbox with login/guest buttons in it
        //hbox2.prefHeightProperty().bind(anchor.heightProperty().multiply(159).divide(680));
        //AnchorPane.setBottomAnchor(hbox2, anchor.getPrefHeight()*191/680);

        //height and bottom anchor for middle hbox with picture in it
        //hbox3.prefHeightProperty().bind(anchor.heightProperty().multiply(159).divide(680));
        //AnchorPane.setBottomAnchor(hbox3, anchor.getPrefHeight()*21/680);

        translateInit();

        clockTimeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> updateTime()));
        clockTimeline.setCycleCount(Animation.INDEFINITE);
        clockTimeline.play();
        timerLabel.textProperty().bind(time);

        anchor.setBottomAnchor(timerLabel, 0.0);
        anchor.setRightAnchor(timerLabel, 0.0);
    }

    private void translateInit() throws IOException {
        creditsHeader.setText(tr.translate("en", language, creditsHeader.getText()));
        creditsInfo.setText(tr.translate("en", language, creditsInfo.getText()));
        Emergency.setText(tr.translate("en", language, Emergency.getText()));
        home.setText(tr.translate("en", language, home.getText()));

        LinkedList<Text> textToTranslate=new LinkedList<>();
        textToTranslate.add(creditsInfo);
        textToTranslate.add(alexText);
        textToTranslate.add( alexTitle);
        textToTranslate.add( maryText);
        textToTranslate.add( maryTitle);
        textToTranslate.add( nigelText);
        textToTranslate.add( nigelTitle);
        textToTranslate.add( tobyText);
        textToTranslate.add( tobyTitle);
        textToTranslate.add( erricaText);
        textToTranslate.add( erricaTitle);
        textToTranslate.add( carlieText);
        textToTranslate.add(carlieTitle);
        textToTranslate.add( haydenText);
        textToTranslate.add( haydenTitle);
        textToTranslate.add( nathanText);
        textToTranslate.add(nathanTitle);
        textToTranslate.add( dylanText);
        textToTranslate.add(dylanTitle);

        tr.translateTextList(textToTranslate);
    }

    public void goEmergency(ActionEvent actionEvent) {
        try {
            Capp.prevPage = "/edu/wpi/teamC/fxml/Credits.fxml";
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/serviceRequest/Emergency.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void goHome(ActionEvent actionEvent) {
        if(Capp.userType.equals("Admin")) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/StartUp.fxml"));
                Capp.getPrimaryStage().getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else if(Capp.userType.equals("Employee")) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/StartUp.fxml"));
                Capp.getPrimaryStage().getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/StartUp.fxml"));
                Capp.getPrimaryStage().getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
