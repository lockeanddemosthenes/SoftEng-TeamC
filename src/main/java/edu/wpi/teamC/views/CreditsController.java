package edu.wpi.teamC.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

public class CreditsController {
    @FXML
    private Rectangle r2;
    @FXML
    private Rectangle r1;
    @FXML
    private Label aboutHeader;
    @FXML
    private Text text1;
    @FXML
    private Text text2;
    @FXML
    private Text text3;
    @FXML
    private Text text4;
   /* @FXML
    private Text text5;
    @FXML
    private Text text6;
    @FXML
    private Text text7;
    @FXML
    private Text text8;
    @FXML
    private Text text9;
    @FXML
    private Text text10;
    @FXML
    private Text text11;
    @FXML
    private Text text12;
    @FXML
    private Text text13;*/
    @FXML
    private JFXButton emergency;
    @FXML private AnchorPane anchor;
    @FXML private VBox vbox;
    @FXML private HBox hbox1;
    @FXML private ImageView bwh;

    @FXML
    private JFXButton exit;
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
        emergency.setText(tr.translate("en", language, emergency.getText()));
        exit.setText(tr.translate("en", language, exit.getText()));
        aboutHeader.setText(tr.translate("en", language, aboutHeader.getText()));

        //height and width of top rectangle
        r1.widthProperty().bind(anchor.widthProperty());
        r1.heightProperty().bind(anchor.heightProperty().multiply(140).divide(680));

        //height and width of bottom rectangle
        r2.widthProperty().bind(anchor.widthProperty());
        r2.heightProperty().bind(anchor.heightProperty().multiply(522).divide(680));

        //width and right anchor for emergency button
        emergency.prefWidthProperty().bind(anchor.widthProperty().multiply(85).divide(1280));
        AnchorPane.setRightAnchor(emergency, anchor.getPrefWidth()*94/1280);

        //width and right anchor for exit button
        exit.prefWidthProperty().bind(anchor.widthProperty().multiply(85).divide(1280));
        AnchorPane.setRightAnchor(exit, anchor.getPrefWidth()*94/1280);

        vbox.prefHeightProperty().bind(anchor.heightProperty().multiply(522).divide(680));

        hbox1.prefHeightProperty().bind(anchor.heightProperty().multiply(140).divide(680));

        //width and height for bwh image
        bwh.fitWidthProperty().bind(anchor.widthProperty().multiply(112).divide(1280));
        bwh.fitHeightProperty().bind(anchor.heightProperty().multiply(128).divide(680));

        clockTimeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> updateTime()));
        clockTimeline.setCycleCount(Animation.INDEFINITE);
        clockTimeline.play();
        timerLabel.textProperty().bind(time);

        anchor.setBottomAnchor(timerLabel, 0.0);
        anchor.setRightAnchor(timerLabel, 0.0);

        translateInit();
    }

    private void translateInit() throws IOException {
        LinkedList<Text> textToTranslate=new LinkedList<>();
        textToTranslate.add(text1);
        textToTranslate.add(text2);
        textToTranslate.add(text3);
        textToTranslate.add(text4);
      /*  textToTranslate.add(text5);
        textToTranslate.add(text6);
        textToTranslate.add(text7);
        textToTranslate.add(text8);
        textToTranslate.add(text9);
        textToTranslate.add(text10);
        textToTranslate.add(text11);
        textToTranslate.add(text12);
        textToTranslate.add(text13);*/
       Translate.translateTextList(textToTranslate);

    }


    @FXML
    private void goBack(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/StartUp.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void goEmergency(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/serviceRequest/Emergency.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


}
