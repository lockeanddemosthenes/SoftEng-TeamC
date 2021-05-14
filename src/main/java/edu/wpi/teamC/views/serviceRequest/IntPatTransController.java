package edu.wpi.teamC.views.serviceRequest;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import com.jfoenix.controls.JFXTreeView;
import edu.wpi.teamC.Capp;
import edu.wpi.teamC.SendEmail;
import edu.wpi.teamC.Translate;
import edu.wpi.teamC.entities.CaretakerSingleton;
import edu.wpi.teamC.entities.Employee;
import edu.wpi.teamC.entities.requests.TreeCellFactory;
import edu.wpi.teamC.entities.requests.TreeCellFactoryAlternate;
import edu.wpi.teamC.repository.*;
import edu.wpi.teamC.views.LoginController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import edu.wpi.teamC.entities.requests.ServiceRequest;
import edu.wpi.teamC.entities.requests.InternalPatientRequest;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
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

public class IntPatTransController implements ITreeMenuController,ITreeMenuControllerAlternate{

    @FXML private JFXTextField patientID;
    @FXML private JFXTreeView<NodeDB> currentLocationChooser;
    @FXML private JFXTreeView<NodeDB> newLocationChooser;
    @FXML private MenuButton transportChooser;
    @FXML private JFXTextField transportReason;
    @FXML private JFXButton submitButton;
    @FXML private MenuButton employeeChooser;
    @FXML private JFXButton Emergency;
    @FXML private JFXTimePicker time;
    @FXML private Label internalPatientTitle;
    @FXML private Text patientText;
    @FXML private Text currentLocationText;
    @FXML private Text destinationText;
    @FXML private Text transportTypeText;
    @FXML private JFXButton backButton;
    @FXML private Text timeText;
    @FXML private Text assignToText;
    @FXML private AnchorPane anchor;
    @FXML
    private Rectangle r1;
    @FXML
    private Rectangle r2;
    @FXML
    private HBox hbox1;
    @FXML
    private ImageView bwh;
    @FXML private VBox vbox;


    ConnectionSingleton c = ConnectionSingleton.getInstance();
    Connection conn = c.conn;
    DBAccess database = new Database(conn);
    CaretakerSingleton s = CaretakerSingleton.getInstance();
    Timeline timeline;
    static LanguageSingleton lang = LanguageSingleton.getInstance();
    String language = lang.s;
    Translate tr = new Translate();
    ArrayList<NodeDB> nodes = new ArrayList<NodeDB>();
    ArrayList<Employee> employees = new ArrayList<Employee>();
    String[] transportTypes = {"walking (no physical assistance needed)", "walker", "wheelchair", "stretcher"};
    String transport = "";
    String currentLocation;
    String newLocation;
    String email;
    String assignUserName;
    private String assignedFirstName;
    private String assignedLastName;

    private String currentUser = LoginController.getCorrectUser();

    @FXML private Label timerLabel;
    private Timeline clockTimeline;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
    private StringProperty clockTime = new SimpleStringProperty("");
    private void updateTime() {
        clockTime.set(timeFormat.format(Calendar.getInstance().getTime()));
    }

    public void translate() throws IOException {
        ArrayList<ButtonBase> buttons = new ArrayList<ButtonBase>();
        ArrayList<Text> texts = new ArrayList<Text>();
        buttons.add(submitButton);
        buttons.add(backButton);
        buttons.add(Emergency);
        tr.translateButtonList(buttons);
        ArrayList<TextField> textFields=new ArrayList<>();
        textFields.add(patientID);
        textFields.add(transportReason);
        Translate.translateTextFieldList(textFields);


        transportChooser.setText(tr.translate("en", language, transportChooser.getText()));
        employeeChooser.setText(tr.translate("en", language,employeeChooser.getText()));
        time.setPromptText(tr.translate("en", language, time.getPromptText()));
        internalPatientTitle.setText(tr.translate("en", language, internalPatientTitle.getText()));
        texts.add(patientText);
        texts.add(currentLocationText);
        texts.add(destinationText);
        texts.add(transportTypeText);
        texts.add(timeText);
        texts.add(assignToText);
        tr.translateTextList(texts);
    }

    @FXML
    public void initialize() throws SQLException, IOException {
        nodes = database.getListOfNodes();
        translate();
        addTransports();
        initLegend();
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

        clockTimeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> updateTime()));
        clockTimeline.setCycleCount(Animation.INDEFINITE);
        clockTimeline.play();
        timerLabel.textProperty().bind(clockTime);

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

        vbox.prefHeightProperty().bind(anchor.heightProperty().multiply(522).divide(680));
        AnchorPane.setTopAnchor(vbox, anchor.getPrefWidth()*235/1280);

    }

    private void addTransports() throws IOException {

        for (int i = 0; i < transportTypes.length; i++) {
            String type = tr.translate("en", language,transportTypes[i]);
            String typeEnglish = transportTypes[i];
            MenuItem m =
                    new MenuItem(type);
            m.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    transportChooser.setText(type);
                    transport = typeEnglish;
                }
            });
            transportChooser.getItems().add(m);
        }
    }

    private void initLegend() {
        currentLocation="";
        TreeItem<NodeDB> rootNode = new TreeItem<>(new NodeDB("Category","X","Y","floor","building","type","longName","shortName"));
        rootNode.setValue(new NodeDB());
        currentLocationChooser.setRoot(rootNode);
        rootNode.setExpanded(true);
        currentLocationChooser.setShowRoot(false);
        currentLocationChooser.setCellFactory(new Callback<TreeView<NodeDB>, TreeCell<NodeDB>>() {
            @Override
            public TreeCell<NodeDB> call(TreeView<NodeDB> param) {
                return new TreeCellFactory(getThis());
            }
        });
        initLegendAlternate();
    }
    public ITreeMenuController getThis(){
        return this;
    }

    private void addLocations() throws SQLException {
        nodes = database.getListOfNodes();
        for (int i = 0; i < nodes.size(); i++) {
            TreeCellFactory t=new TreeCellFactory();
            TreeCellFactoryAlternate ta=new TreeCellFactoryAlternate();
            t.addToLegend(nodes.get(i),currentLocationChooser,"Transparent");
            ta.addToLegend(nodes.get(i),newLocationChooser,"Transparent");

        }
    }
    public void setLocation(String ID){
        currentLocation=ID;
        System.out.println("Set current location to"+ID);
    }

    private void initLegendAlternate() {
        newLocation="";
        TreeItem<NodeDB> rootNode = new TreeItem<>(new NodeDB("Category","X","Y","floor","building","type","longName","shortName"));
        rootNode.setValue(new NodeDB());
        newLocationChooser.setRoot(rootNode);
        rootNode.setExpanded(true);
        newLocationChooser.setShowRoot(false);
        newLocationChooser.setCellFactory(new Callback<TreeView<NodeDB>, TreeCell<NodeDB>>() {
            @Override
            public TreeCell<NodeDB> call(TreeView<NodeDB> param) {
                return new TreeCellFactoryAlternate(getThisAlternate());
            }
        });
        try {
            addLocations();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public ITreeMenuControllerAlternate getThisAlternate(){
        return this;
    }


    public void setLocationAlternate(String ID){
        newLocation=ID;
        System.out.println("Set new location to"+ID);
    }

    private void addEmployees() throws SQLException {
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
                    System.out.println("Employee " + firstName + " " + lastName + " selected");
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


    public void goEmergency(ActionEvent actionEvent) {
        try {
            Capp.prevPage = "/edu/wpi/teamC/fxml/serviceRequest/IntPatTrans.fxml";
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/serviceRequest/Emergency.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
            timeline.stop();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void addInternalPatientRequest() {
        String description = "Patient needs transport from " + currentLocation + " to " + newLocation + "by " + transport + " because " + transportReason.getText();
        //String assignTo = employeeChooser.getText();
        ServiceRequest sr = new ServiceRequest(currentUser,currentLocation, description, assignUserName);
        database.addServiceRequest(sr);
        int requestID = sr.getRequestID();
        InternalPatientRequest ir = new InternalPatientRequest(requestID, currentUser, patientID.getText(), currentLocation, newLocation, transport, time.getValue().toString(), transportReason.getText(), "False", assignUserName);
        database.addInternalPatientRequest(ir);
    }

    private void sendEmail() throws MessagingException {
        SendEmail se = new SendEmail(email,"brighamandwomenshospital1@gmail.com");
        se.setMessageSubject("Internal Patient Transportation Request");
        se.setMessageText("Hello "+ assignedFirstName + " " + assignedLastName +", " +
                "\n " +
                "\nYou have a new service request!" +
                "\nPatient ID: " + patientID.getText() +
                "\nCurrent Location: " + currentLocation +
                "\nDestination: " + newLocation +
                "\nTransport Type: " + transport +
                /*"\nDate: " + date +*/
                "\nTime: " + time.getValue().toString() +
                "\nReason: " + transportReason.getText() +
                "\n" +
                "\n-Team C");
        se.sendMessage();
    }

    @FXML
    protected void goSubmit() throws MessagingException {
        addInternalPatientRequest();
        sendEmail();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/ThankYou.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
            timeline.stop();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    @FXML
    private boolean validateButton(){
        if(time.getValue() == null || patientID.getText().isEmpty() || currentLocation.equals("") ||  newLocationChooser.equals("")
                || employeeChooser.getText().isEmpty() ||transportChooser.getText().isEmpty() || transportReason.getText().isEmpty()){
            submitButton.setDisable(true);
            return false;
        } else {
            submitButton.setDisable(false);
            return true;
        }
    }

    public void enterKey(KeyEvent keyEvent) throws MessagingException {
        if(validateButton() == true && keyEvent.getCode().equals(KeyCode.ENTER)) {
            goSubmit();
        }
    }

    public void goServiceSystems(ActionEvent event) {
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

    public void goHome(ActionEvent event) {
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
    public void keyTyped(KeyEvent actionEvent){
        s.setPrevTime();
    }

    @FXML
    public void mouseMoved(MouseEvent actionEvent){
        s.setPrevTime();
    }
}
