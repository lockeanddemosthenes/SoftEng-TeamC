package edu.wpi.teamC.views.mapEditor;

import com.jfoenix.controls.JFXButton;
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
import javafx.scene.control.TextField;
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

public class EditEdgeController {

    @FXML
    private TextField EdgeID;
    @FXML
    private TextField NewStartNode;
    @FXML
    private TextField NewEndNode;
    @FXML
    private TextField OldStartNode;
    @FXML
    private TextField OldEndNode;
    public EdgeDB currentEdge;
    @FXML
    private Button Emergency;
    @FXML private AnchorPane anchor;
    @FXML private Rectangle r1;
    @FXML private Rectangle r2;
    @FXML private HBox hbox1;
    @FXML private HBox hbox2;
    @FXML private HBox hbox3;
    @FXML private HBox hbox4;
    @FXML private HBox hbox5;
    @FXML private JFXButton emergency;
    @FXML private ImageView bwh;

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
    CaretakerSingleton s = CaretakerSingleton.getInstance();
    Timeline timeline;
    DBAccess database = new Database(conn);

    static LanguageSingleton lang = LanguageSingleton.getInstance();
    String language = lang.s;

    @FXML
    public void initialize(){
        s.setPrevTime();
        timeline = new Timeline(new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // check if caretaker prev time is far back from current time
                if ((System.currentTimeMillis() - s.getPrevTime()) > 120000) {
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

        //height and bottom anchor for bottom hbox with login/guest buttons in it
        hbox2.prefHeightProperty().bind(anchor.heightProperty().multiply(100).divide(680));
        AnchorPane.setBottomAnchor(hbox2, anchor.getPrefHeight()*374/680);

        //height and bottom anchor for middle hbox with picture in it
        hbox3.prefHeightProperty().bind(anchor.heightProperty().multiply(100).divide(680));
        AnchorPane.setBottomAnchor(hbox3, anchor.getPrefHeight()*259/680);

        hbox4.prefHeightProperty().bind(anchor.heightProperty().multiply(100).divide(680));
        AnchorPane.setBottomAnchor(hbox4, anchor.getPrefHeight()*127/680);

        hbox5.prefHeightProperty().bind(anchor.heightProperty().multiply(100).divide(680));

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
            Capp.prevPage = "/edu/wpi/teamC/fxml/mapEditor/EditEdge.fxml";
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/serviceRequest/Emergency.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
            timeline.stop();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void goHome(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/DefaultScreen.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
            timeline.stop();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void submit(ActionEvent actionEvent) {

        String id = EdgeID.getText();
        String start = NewStartNode.getText();
        String end = NewEndNode.getText();

        database.updateStartNode(id, start);
        database.updateEndNode(id, end);
        goEdges(actionEvent);
    }



    public void receiveData(EdgeDB edge) {
        System.out.println(edge);
        EdgeID.setText(edge.edgeID);
        OldStartNode.setText(edge.startNode);
        OldEndNode.setText(edge.endNode);
    }

    @FXML
    public void keytyped(KeyEvent actionEvent){
        s.setPrevTime();
    }

    @FXML
    public void mouseMoved(MouseEvent actionEvent){
        s.setPrevTime();
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
}
