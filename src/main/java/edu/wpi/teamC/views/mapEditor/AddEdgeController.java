package edu.wpi.teamC.views.mapEditor;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamC.Capp;
import edu.wpi.teamC.entities.CaretakerSingleton;
import edu.wpi.teamC.repository.*;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddEdgeController {

    @FXML
    private JFXTextField EdgeID;
    @FXML
    private JFXTextField EdgeStart;
    @FXML
    private JFXTextField EdgeEnd;
    @FXML
    private JFXButton submitButton;
    @FXML
    private Button Emergency;
    @FXML private Rectangle r1;
    @FXML private Rectangle r2;
    @FXML private HBox hbox1;
    @FXML private HBox hbox3;
    @FXML private JFXButton emergency;
    @FXML private JFXButton exit;
    @FXML private ImageView bwh;
    @FXML private HBox hbox2;
    @FXML private AnchorPane anchor;

    @FXML private Label timerLabel;
    private Timeline clockTimeline;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
    private StringProperty time = new SimpleStringProperty("");
    private void updateTime() {
        time.set(timeFormat.format(Calendar.getInstance().getTime()));
    }


    ConnectionSingleton c = ConnectionSingleton.getInstance();
    Connection conn = c.conn;
    //DBAccess database = new DBAccess(conn);
    static LanguageSingleton lang = LanguageSingleton.getInstance();
    String language = lang.s;
    CaretakerSingleton s = CaretakerSingleton.getInstance();
    Timeline timeline;
    DBAccess database = new Database(conn);

    @FXML
    public void initialize() {
        s.setPrevTime();
        timeline = new Timeline(new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // check if caretaker prev time is far back from current time
                if ((System.currentTimeMillis() - s.getPrevTime()) > 60000) {
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

        //height and width of top rectangle
        r1.widthProperty().bind(anchor.widthProperty());
        r1.heightProperty().bind(anchor.heightProperty().multiply(140).divide(680));

        //height and width of bottom rectangle
        r2.widthProperty().bind(anchor.widthProperty());
        r2.heightProperty().bind(anchor.heightProperty().multiply(522).divide(680));

        //height of top hbox with title in it
        hbox1.prefHeightProperty().bind(anchor.heightProperty().multiply(140).divide(680));

        //height and bottom anchor for middle hbox
        hbox2.prefHeightProperty().bind(anchor.heightProperty().multiply(195).divide(680));
        AnchorPane.setBottomAnchor(hbox2, anchor.getPrefHeight()*210/680);

        //height and bottom anchor for bottom hbox
        hbox3.prefHeightProperty().bind(anchor.heightProperty().multiply(40).divide(680));
        AnchorPane.setBottomAnchor(hbox3, anchor.getPrefHeight()*25/680);

        //width and height for bwh image
        bwh.fitWidthProperty().bind(anchor.widthProperty().multiply(112).divide(1280));
        bwh.fitHeightProperty().bind(anchor.heightProperty().multiply(128).divide(680));

        //width and right anchor for emergency button
        emergency.prefWidthProperty().bind(anchor.widthProperty().multiply(85).divide(1280));
        AnchorPane.setRightAnchor(emergency, anchor.getPrefWidth()*94/1280);

        clockTimeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> updateTime()));
        clockTimeline.setCycleCount(Animation.INDEFINITE);
        clockTimeline.play();
        timerLabel.textProperty().bind(time);

        anchor.setBottomAnchor(timerLabel, 0.0);
        anchor.setRightAnchor(timerLabel, 0.0);

    }

    public void goEdges(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/mapEditor/Edges.fxml"));
            //((Node)event.getSource()).getScene().setRoot(root);
            Capp.getPrimaryStage().getScene().setRoot(root);
            timeline.stop();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void goEmergency(ActionEvent actionEvent) {
        try {
            Capp.prevPage = "/edu/wpi/teamC/fxml/mapEditor/AddEdge.fxml";
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/serviceRequest/Emergency.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
            timeline.stop();
        } catch (IOException ex) {
            ex.printStackTrace();
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

    @FXML
    private void validateSubmit() {
        if (EdgeID.getText().isEmpty() || EdgeStart.getText().isEmpty() || EdgeEnd.getText().isEmpty()) {
            submitButton.setDisable(true);
        } else {
            submitButton.setDisable(false);
        }
    }

    public void submit(ActionEvent actionEvent) {

        String id = EdgeID.getText();
        String start = EdgeStart.getText();
        String end = EdgeEnd.getText();

        EdgeDB edge = new EdgeDB(id, start, end);
        database.addEdge(edge);
        goEdges(actionEvent);
    }

    @FXML
    protected void logOut(ActionEvent actionEvent) {
        try {
            Capp.userType = "Guest";
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/StartUp.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void keyTyped(KeyEvent actionEvent){
        s.setPrevTime();
    }

    @FXML
    public void mouseMoved(MouseEvent actionEvent){
        s.setPrevTime();
    }

}


