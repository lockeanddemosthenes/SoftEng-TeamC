package edu.wpi.teamC.views.serviceRequest;

import com.jfoenix.controls.JFXTreeView;
import com.jfoenix.controls.JFXButton;
import edu.wpi.teamC.Translate;
import edu.wpi.teamC.entities.CaretakerSingleton;
import edu.wpi.teamC.entities.requests.FloralDeliveryRequest;
import edu.wpi.teamC.entities.requests.ServiceRequest;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamC.Capp;
import edu.wpi.teamC.SendEmail;
import edu.wpi.teamC.entities.Employee;
import edu.wpi.teamC.entities.requests.TreeCellFactory;
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
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.Duration;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class FloralDeliveryController implements ITreeMenuController{

    private String flowerType;
    private ArrayList<NodeDB> nodes;
    private ArrayList<Employee> employees;
    private String location;
    private String email;
    private String assignUserName;
    @FXML private Label floralRequestLabel;
    @FXML private JFXButton Emergency;
    @FXML private Text nameText;
    @FXML private Text roomText;
    @FXML private Text typeText;
    @FXML private Text numberText;
    @FXML private Text noteText;
    @FXML private Text assignToText;
    @FXML private JFXButton backButton;
   // @FXML private JFXButton helpButton;
    @FXML private JFXTextField patientName;
    @FXML private MenuButton flowerChooser;
    @FXML private JFXTreeView<NodeDB> locationChooser;
    @FXML private MenuButton employeeChooser;
    @FXML private JFXTextField flowersNo;
    @FXML private JFXTextField notesField;
    @FXML private Button submitButton;
    @FXML private AnchorPane anchor;
    @FXML
    private Rectangle r1;
    @FXML
    private Rectangle r2;
    @FXML
    private HBox hbox1;
    @FXML
    private ImageView bwh;
    @FXML private HBox hbox2;
    private String currentUser = LoginController.getCorrectUser();
    private String assignedFirstName;
    private String assignedLastName;
    ConnectionSingleton c = ConnectionSingleton.getInstance();
    Connection conn = c.conn;
    DBAccess database = new Database(conn);
    CaretakerSingleton s = CaretakerSingleton.getInstance();
    static LanguageSingleton lang = LanguageSingleton.getInstance();
    String language = lang.s;
    Timeline timeline;
    String[] flowerTypes = {"Geranium", "Rose", "Lilac", "Hyacinth", "Tulip"};
    Translate tr = new Translate();

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
            ArrayList<Button> buttons = new ArrayList<Button>();
            buttons.add(Emergency);
            buttons.add(backButton);
           // buttons.add(helpButton);
            buttons.add(submitButton);
            //buttons
            for (Button button : buttons) {
                if (button != null) {
                    button.setText(tr.translate("en", language, button.getText()));
                }
            }
            // Text
            nameText.setText(tr.translate("en", language, nameText.getText()));
            roomText.setText(tr.translate("en", language, roomText.getText()));
            typeText.setText(tr.translate("en", language, typeText.getText()));
            numberText.setText(tr.translate("en", language, numberText.getText()));
            noteText.setText(tr.translate("en", language, noteText.getText()));
            assignToText.setText(tr.translate("en", language, assignToText.getText()));
            // Label
            floralRequestLabel.setText(tr.translate("en", language, floralRequestLabel.getText()));
            // Menu Buttons
            flowerChooser.setText(tr.translate("en", language, flowerChooser.getText()));
            //locationChooser.setText(tr.translate("en", language, locationChooser.getText()));
            employeeChooser.setText(tr.translate("en", language, employeeChooser.getText()));
            // Text Fields
            patientName.setPromptText(tr.translate("en", language, patientName.getPromptText()));
            flowersNo.setPromptText(tr.translate("en", language, flowersNo.getPromptText()));
            notesField.setPromptText(tr.translate("en", language, notesField.getPromptText()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        addFlowerType();
        addEmployees();
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
        initLegend();

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

        //width and height for bwh image
        bwh.fitWidthProperty().bind(anchor.widthProperty().multiply(112).divide(1280));
        bwh.fitHeightProperty().bind(anchor.heightProperty().multiply(128).divide(680));

        //width and right anchor for emergency button
        Emergency.prefWidthProperty().bind(anchor.widthProperty().multiply(85).divide(1280));
        AnchorPane.setRightAnchor(Emergency, anchor.getPrefWidth() * 94 / 1280);

        hbox2.prefHeightProperty().bind(anchor.heightProperty().multiply(522).divide(680));
        AnchorPane.setTopAnchor(hbox2, anchor.getPrefWidth()*235/1280);

    }

    private void addFlowerType(){
        for (int i = 0; i < flowerTypes.length; i++) {
            String type = flowerTypes[i];
            MenuItem m =
                    new MenuItem(type);
            m.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    flowerChooser.setText(type);
                    flowerType = type;
                }
            });
            flowerChooser.getItems().add(m);
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

    private void addEmployees() throws SQLException{
        employees = database.getListOfEmployees();
        for (int i = 0; i < employees.size(); i++) {
            String firstName = employees.get(i).getFirstName();
            String lastName = employees.get(i).getLastName();
            String userEmail = employees.get(i).getUserEmail();
            String userName = employees.get(i).getUserName();
            MenuItem m =
                    new MenuItem(firstName + " " + lastName);
            m.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    System.out.println("Node " + firstName + " " + lastName + " selected");
                    employeeChooser.setText(firstName + " " + lastName);
                    email = userEmail;
                    assignUserName = userName;
                    assignedFirstName = firstName;
                    assignedLastName = lastName;
                    System.out.println("User name: " + userName);
                }
            });
            employeeChooser.getItems().add(m);
        }
    }

    private void sendEmail() throws MessagingException {
        SendEmail se = new SendEmail(email,"brighamandwomenshospital1@gmail.com");
        se.setMessageSubject("Floral Delivery Request");
        se.setMessageText("Hello "+ assignedFirstName + " " + assignedLastName +", " +
                "\n " +
                "\nYou have a new service request!" +
                "\nPatient Name: " + patientName.getText() +
                "\nLocation: " + location +
                "\nFlower Type: " + flowerType +
                /*"\nDate: " + date +
                "\nTime: " + time +*/
                "\nNumber of Flowers: " + flowersNo.getText() +
                "\nNotes: " + notesField.getText() +
                "\n" +
                "\n-Team C");
        se.sendMessage();
    }

    public void goEmergency(ActionEvent actionEvent) {
        try {
            Capp.prevPage = "/edu/wpi/teamC/fxml/serviceRequest/FloralDelivery.fxml";
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/serviceRequest/Emergency.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
            timeline.stop();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void validateButton(){
        if(patientName.getText().isEmpty() || location.equals("") || flowerChooser.getText().isEmpty() || flowersNo.getText().isEmpty()){
            submitButton.setDisable(true);
        } else {
            submitButton.setDisable(false);
        }
    }

    private void addFloralDeliveryRequest() throws SQLException{
        String patientName = this.patientName.getText();
        String flowerType = this.flowerChooser.getText();
        String flowerNum = this.flowersNo.getText();
        String notes = this.notesField.getText();
        ServiceRequest sr = new ServiceRequest(currentUser, location, notes, assignUserName, "False");
        database.addServiceRequest(sr);
        int requestID= sr.getRequestID();
        FloralDeliveryRequest fr = new FloralDeliveryRequest(requestID,currentUser,location,patientName, flowerType, flowerNum, notes,assignUserName, "False");
        database.addFloralDeliveryRequest(fr);
    }

    @FXML
    protected void goSubmit(ActionEvent e){
        try {
            addFloralDeliveryRequest();
            sendEmail();
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/ThankYou.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
            timeline.stop();
        } catch (IOException | SQLException | MessagingException  ex) {
            ex.printStackTrace();
        }
    }

    public void goHome(ActionEvent actionEvent) {
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

    @FXML
    protected void goServiceSystems(ActionEvent e) {
        if(Capp.userType.equals("Admin")) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/serviceRequest/ServiceSystemsAdmin.fxml"));
                Capp.getPrimaryStage().getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else if(Capp.userType.equals("Employee")) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/serviceRequest/ServiceSystemsEmployee.fxml"));
                Capp.getPrimaryStage().getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/serviceRequest/ServiceSystemsGuest.fxml"));
                Capp.getPrimaryStage().getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void goHelp(ActionEvent actionEvent) {
        //currently directs to previous page
        try {
            Parent root = FXMLLoader.load(getClass().getResource(Capp.prevPage));
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
