package edu.wpi.teamC.views.serviceRequest;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamC.Capp;
import edu.wpi.teamC.SendEmail;
import edu.wpi.teamC.Translate;
import edu.wpi.teamC.entities.requests.*;
import edu.wpi.teamC.repository.LanguageSingleton;
import edu.wpi.teamC.entities.Employee;
import edu.wpi.teamC.repository.*;
import edu.wpi.teamC.views.LoginController;
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
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.Duration;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;


public class EmergencyController implements ITreeMenuController {
    @FXML private Label emergencyLabel;
    @FXML private JFXButton cancelButton;
    @FXML TreeView<NodeDB> locationChooser;
    @FXML MenuButton emergencyChooser;
    @FXML JFXButton submitButton;
    @FXML private AnchorPane anchor;
    @FXML private Text currentLocation;
    @FXML private Text emergencyTypeText;

    private String currentUser = LoginController.getCorrectUser();
    static LanguageSingleton lang = LanguageSingleton.getInstance();
    String language = lang.s;
    Translate tr = new Translate();
    private String location = "";
    private ArrayList<Employee> employees;
    ArrayList<NodeDB> nodes;

    ConnectionSingleton c = ConnectionSingleton.getInstance();
    Connection conn = c.conn;
    DBAccess database = new Database(conn);

    String[] emergencyTypes = {"Medical", "Police", "Fire", "Other"};
    private String emergencyType;

    @FXML private Label timerLabel;
    private Timeline clockTimeline;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
    private StringProperty time = new SimpleStringProperty("");
    private void updateTime() {
        time.set(timeFormat.format(Calendar.getInstance().getTime()));
    }

    @FXML
    public void initialize() throws SQLException {
        try {
            ArrayList<ButtonBase> buttons = new ArrayList<ButtonBase>();
            buttons.add(cancelButton);
            buttons.add(submitButton);
            tr.translateButtonList(buttons);
            // label
            emergencyLabel.setText(tr.translate("en", language, emergencyLabel.getText()));
            emergencyChooser.setText(tr.translate("en", language, emergencyChooser.getText()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        initLegend();
        addEmergency();

        clockTimeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> updateTime()));
        clockTimeline.setCycleCount(Animation.INDEFINITE);
        clockTimeline.play();
        timerLabel.textProperty().bind(time);

        anchor.setBottomAnchor(timerLabel, 0.0);
        anchor.setRightAnchor(timerLabel, 0.0);

        initTranslate();
    }

    public void initTranslate() {
        try {
            ArrayList<Button> buttons = new ArrayList<Button>();
            buttons.add(cancelButton);
            buttons.add(submitButton);

            System.out.println(language);
            if(emergencyLabel != null){
                emergencyLabel.setText(tr.translate("en", language, emergencyLabel.getText()));
            }
            for (Button button : buttons) {
                if (button != null){
                    button.setText(tr.translate("en",language,button.getText()));
                }
            }
            currentLocation.setText(tr.translate("en", language, currentLocation.getText()));
            emergencyTypeText.setText(tr.translate("en", language, emergencyTypeText.getText()));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void back(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(Capp.prevPage));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void goHome(ActionEvent e) {
        if(Capp.userType.equals("Admin")) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/DefaultScreen.fxml"));
                Capp.getPrimaryStage().getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else if(Capp.userType.equals("Employee")) {
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

    private void addEmergency() {
        for (int i = 0; i < emergencyTypes.length; i++) {
            String type = emergencyTypes[i];
            MenuItem m =
                    new MenuItem(type);
            m.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    emergencyChooser.setText(type);
                    emergencyType = type;
                }
            });
            emergencyChooser.getItems().add(m);
        }
    }

    private void initLegend() {
        location="";
        TreeItem<NodeDB> rootNode = new TreeItem<>(new NodeDB("Category","X","Y","floor","building","type","longName","shortName"));
        rootNode.setValue(new NodeDB());
        locationChooser.setRoot(rootNode);
        rootNode.setExpanded(true);
        locationChooser.setShowRoot(false);
        locationChooser.setCellFactory(new Callback<TreeView<NodeDB>, TreeCell<NodeDB>>() {
            @Override
            public TreeCell<NodeDB> call(TreeView<NodeDB> param) {
                return new TreeCellFactory(getThis());
            }
        });
        try {
            addLocations();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public ITreeMenuController getThis(){
        return this;
    }

    private void addLocations() throws SQLException {
        nodes = database.getListOfNodes();
        for (int i = 0; i < nodes.size(); i++) {
            TreeCellFactory t=new TreeCellFactory();
            t.addToLegend(nodes.get(i),locationChooser,"Transparent");
        }
    }
    public void setLocation(String ID){
        location=ID;
        System.out.println("Set location to"+ID);
    }

    public void addEmergencyRequest() {
        String isComplete = "False";

        if(currentUser == null){
            currentUser = "guest";
        }
        ServiceRequest sr = new ServiceRequest(currentUser, location, "emergency request", "hgsmith", isComplete);
        database.addServiceRequest(sr);
        EmergencyRequest er = new EmergencyRequest(sr.getRequestID(), currentUser,"", location, "hgsmith", "", isComplete, emergencyType);
        database.addEmergencyRequest(er);
    }

    private void sendEmail() throws MessagingException {
        SendEmail se = new SendEmail("hgsmith@wpi.edu","brighamandwomenshospital1@gmail.com");
        se.setMessageSubject("Emergency Request");
        se.setMessageText("Hello Hayden Smith, " +
                "\n " +
                "\nYou have a new service request!" +
                "\nCustomer Name: " + currentUser +
                "\nLocation: " + location +
                "\nEmergency Type: " + emergencyType +
                "\n" +
                "\n-Team C");
        se.sendMessage();
    }

    public void goEmergencyProcessed(ActionEvent actionEvent) {
        try {
            addEmergencyRequest();
            sendEmail();
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/serviceRequest/EmergencyProcessed.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException | MessagingException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void validateButton() {
        if (location.equals("") || emergencyChooser.getText() == null) {
            submitButton.setDisable(true);
        } else {
            submitButton.setDisable(false);
        }
    }

}
