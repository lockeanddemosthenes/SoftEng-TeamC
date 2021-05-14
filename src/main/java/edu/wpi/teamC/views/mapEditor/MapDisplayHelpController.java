package edu.wpi.teamC.views.mapEditor;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import edu.wpi.teamC.Capp;
import edu.wpi.teamC.entities.mapEditor.RenderObjects;
import edu.wpi.teamC.repository.ConnectionSingleton;
import edu.wpi.teamC.repository.DBAccess;
import edu.wpi.teamC.repository.Database;
import edu.wpi.teamC.repository.LanguageSingleton;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MapDisplayHelpController {
    @FXML
    private JFXButton backButton;
    @FXML
    private JFXToggleButton guestToggle;
    @FXML
    private Group guestGroup;
    @FXML
    private Group adminGroup;
    @FXML
    private ImageView mapDisplayImg;
    @FXML private AnchorPane anchor;
    @FXML private HBox hbox1;
    private boolean isAdmin = false;
    private boolean guestLook = !isAdmin;
    DBAccess dbAccess=new Database(ConnectionSingleton.getInstance().conn);;
    static LanguageSingleton lang = LanguageSingleton.getInstance();
    String language = lang.s;

    String guestURL = "/edu/wpi/teamC/fxml/img/MapDisplayGuest2.PNG";
    String adminURL = "/edu/wpi/teamC/fxml/img/MapDisplayAdmin2.PNG";

    @FXML private Label timerLabel;
    private Timeline clockTimeline;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
    private StringProperty time = new SimpleStringProperty("");
    private void updateTime() {
        time.set(timeFormat.format(Calendar.getInstance().getTime()));
    }

    public MapDisplayHelpController() {
        super();
        if (Capp.userType.equals("Admin"))
            isAdmin = true;
        else
            isAdmin = false;
    }

    public void initialize(){
        guestGroup.setVisible(!isAdmin);
        adminGroup.setVisible(isAdmin);
        guestToggle.setVisible(isAdmin);
        guestGroup.toFront();
        URL url;
        if(isAdmin) {
            url = MapDisplayHelpController.class.getResource(adminURL);
        }else{
             url = MapDisplayHelpController.class.getResource(guestURL);
        }

        try {
            Image image = SwingFXUtils.toFXImage(ImageIO.read(url), null);
            mapDisplayImg.setImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        clockTimeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> updateTime()));
        clockTimeline.setCycleCount(Animation.INDEFINITE);
        clockTimeline.play();
        timerLabel.textProperty().bind(time);

        anchor.setBottomAnchor(timerLabel, 0.0);
        anchor.setRightAnchor(timerLabel, 0.0);

        mapDisplayImg.fitWidthProperty().bind(anchor.widthProperty().multiply(1183).divide(1280));
        mapDisplayImg.fitHeightProperty().bind(anchor.heightProperty().multiply(618).divide(680));

        hbox1.prefHeightProperty().bind(anchor.heightProperty().multiply(618).divide(680));

        //guestGroup.layoutXProperty().bind(anchor.widthProperty().multiply(14).divide(1280));
        //guestGroup.layoutYProperty().bind(anchor.heightProperty().multiply(32).divide(680));
    }

    @FXML
    private void backButtonPressed(){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/mapEditor/MapDisplay.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    @FXML
    private void guestToggled(){
        URL url;
        if(guestLook) {
            adminGroup.setVisible(false);
            guestGroup.setVisible(true);
            url = RenderObjects.class.getResource(guestURL);


        } else{
            adminGroup.setVisible(true);
            guestGroup.setVisible(false);
            url = RenderObjects.class.getResource(adminURL);
        }
        guestLook = !guestLook;

        try {
            Image image = SwingFXUtils.toFXImage(ImageIO.read(url), null);
            mapDisplayImg.setImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

