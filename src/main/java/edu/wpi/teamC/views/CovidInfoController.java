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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import javax.print.attribute.standard.PageRanges;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CovidInfoController {
    @FXML private Label covidInfoHeader;
    @FXML private Text paraText;
    @FXML private JFXButton Emergency;
    @FXML private JFXButton homeButton;
    @FXML private Text vaccineInfo;
    @FXML private Hyperlink bwhLink;
    @FXML private Text covidInfo;
    @FXML private Hyperlink cdcLink;
    @FXML
    private Rectangle r1;
    @FXML
    private Rectangle r2;
    @FXML
    private HBox hbox1;
    @FXML
    private HBox hbox3;
    @FXML private VBox vbox;
    @FXML private AnchorPane anchor;
    @FXML
    private ImageView bwh;

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
    public void initialize() throws IOException {
        initTranslate();

        bwhLink.setText("our website");
        bwhLink.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    System.out.println("BWH link is clicked");
                    java.awt.Desktop.getDesktop().browse(new URI("https://www.brighamandwomens.org/covid-19"));
                } catch (IOException | URISyntaxException e2) {
                    e2.printStackTrace();
                }
            }
        });

        cdcLink.setText("the CDC website");
        cdcLink.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    System.out.println("CDC link is clicked");
                    java.awt.Desktop.getDesktop().browse(new URI("https://www.cdc.gov/coronavirus/2019-ncov/index.html"));
                } catch (IOException | URISyntaxException e2) {
                    e2.printStackTrace();
                }
            }
        });

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

        hbox3.prefHeightProperty().bind(anchor.heightProperty().multiply(100).divide(680));

        AnchorPane.setBottomAnchor(vbox, anchor.getPrefHeight()*226/680);
        AnchorPane.setTopAnchor(vbox, anchor.getPrefHeight()*254/680);

        //width and right anchor for emergency button
        Emergency.prefWidthProperty().bind(anchor.widthProperty().multiply(85).divide(1280));
        AnchorPane.setRightAnchor(Emergency, anchor.getPrefWidth() * 94 / 1280);

        //width and height for bwh image
        bwh.fitWidthProperty().bind(anchor.widthProperty().multiply(112).divide(1280));
        bwh.fitHeightProperty().bind(anchor.heightProperty().multiply(128).divide(680));

    }

    public void initTranslate() throws IOException {
        covidInfoHeader.setText(tr.translate("en", language, covidInfoHeader.getText()));
        paraText.setText(tr.translate("en", language, paraText.getText()));
        Emergency.setText(tr.translate("en", language, Emergency.getText()));
        homeButton.setText(tr.translate("en", language, homeButton.getText()));
        vaccineInfo.setText(tr.translate("en", language, vaccineInfo.getText()));
        covidInfo.setText(tr.translate("en", language, covidInfo.getText()));
    }

    @FXML
    public void goHome(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/StartUp.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void goEmergency(ActionEvent actionEvent) {
        try {
            Capp.prevPage = "/edu/wpi/teamC/fxml/StartUp.fxml";
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/serviceRequest/Emergency.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void goBack(ActionEvent actionEvent) {
        try {
            Capp.prevPage = "/edu/wpi/teamC/fxml/StartUp.fxml";
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/serviceRequest/CovidSurvey.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
