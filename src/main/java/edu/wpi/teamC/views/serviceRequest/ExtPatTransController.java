package edu.wpi.teamC.views.serviceRequest;

import com.jfoenix.controls.*;
import edu.wpi.teamC.Capp;
import edu.wpi.teamC.SendEmail;
import edu.wpi.teamC.Translate;
import edu.wpi.teamC.entities.CaretakerSingleton;
import edu.wpi.teamC.entities.Employee;
import edu.wpi.teamC.entities.ExternalAddress;
import edu.wpi.teamC.entities.requests.ExternalPatientRequest;
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
import edu.wpi.teamC.entities.requests.ServiceRequest;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import javafx.scene.text.Text;
import javafx.util.Duration;


import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

public class ExtPatTransController implements ITreeMenuController {

    @FXML private Label extPatTransLabel;
    @FXML private JFXButton emergency;
    @FXML private Text patNameText;
    @FXML private Text locationText;
    @FXML private Text addressText;
    @FXML private Text townText;
    @FXML private Text stateText;
    @FXML private Text zipCodeText;
    @FXML private Text transportTypeText;
    @FXML private Text assignToText;
    @FXML private Text reasonForTransportText;
    @FXML private JFXButton backButton;
   // @FXML private JFXButton helpButton;
    @FXML private JFXTextField patientName;
    @FXML private JFXTextField address;
    @FXML private JFXTextField town;
    @FXML private JFXTextField usState;
    @FXML private MenuButton transportTypes;
    @FXML private MenuItem ambulance;
    @FXML private MenuItem helicopter;
    @FXML private MenuItem plane;
    @FXML private JFXTextField transportReason;
    @FXML private JFXTextField zip;
    @FXML private JFXTreeView<NodeDB> locationChooser;
    @FXML private JFXButton submitButton;
    @FXML private JFXTimePicker getTime;
    @FXML private JFXDatePicker getDate;
    @FXML private MenuButton employeeChooser;
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
    private ArrayList<NodeDB> nodes;
    private ArrayList<Employee> employees;
    private String location;
    private String email;
    private String assignUserName;
    private String assignedFirstName;
    private String assignedLastName;
    private String date;
    private String time;
    ConnectionSingleton c = ConnectionSingleton.getInstance();
    Connection conn = c.conn;
    DBAccess database = new Database(conn);
    CaretakerSingleton s = CaretakerSingleton.getInstance();
    Timeline timeline;
    static LanguageSingleton lang = LanguageSingleton.getInstance();
    String language = lang.s;
    Translate tr = new Translate();

    @FXML private Label timerLabel;
    private Timeline clockTimeline;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
    private StringProperty clockTime = new SimpleStringProperty("");
    private void updateTime() {
        clockTime.set(timeFormat.format(Calendar.getInstance().getTime()));
    }


    @FXML
    public void initialize() throws SQLException {
        try {
            ArrayList<ButtonBase> buttons = new ArrayList<ButtonBase>();
            buttons.add(backButton);
          //  buttons.add(helpButton);
            buttons.add(emergency);
            Translate.translateButtonList(buttons);
            // Date Picker
            getDate.setPromptText(tr.translate("en", language, getDate.getPromptText()));
            getTime.setPromptText(tr.translate("en", language, getTime.getPromptText()));

            //TextField
            LinkedList<TextField> textFields=new LinkedList<>();
            textFields.add(patientName);
            textFields.add(address);
            textFields.add(town);
            textFields.add(usState);
            textFields.add(transportReason);
            textFields.add(zip);
            Translate.translateTextFieldList(textFields);
            //buttons

            //Label
            extPatTransLabel.setText(tr.translate("en", language, extPatTransLabel.getText()));
            //Text
            LinkedList<Text> texts=new LinkedList<>();
            texts.add(patNameText);
            texts.add(locationText);
            texts.add(addressText);
            texts.add(townText);
            texts.add(stateText);
            texts.add(zipCodeText);
            texts.add(transportTypeText);
            texts.add(assignToText);
            texts.add(reasonForTransportText);
            Translate.translateTextList(texts);



            // Menu Items and Menu Buttons
            transportTypes.setText(tr.translate("en", language, transportTypes.getText()));
            ambulance.setText(tr.translate("en", language, ambulance.getText()));
            helicopter.setText(tr.translate("en", language, helicopter.getText()));
            plane.setText(tr.translate("en", language, plane.getText()));
         //   locationChooser.setText(tr.translate("en", language, locationChooser.getText()));
            employeeChooser.setText(tr.translate("en", language, employeeChooser.getText()));




        } catch (IOException e) {
            e.printStackTrace();
        }
        //database.displayAllNodes();
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
        emergency.prefWidthProperty().bind(anchor.widthProperty().multiply(85).divide(1280));
        AnchorPane.setRightAnchor(emergency, anchor.getPrefWidth() * 94 / 1280);

        hbox2.prefHeightProperty().bind(anchor.heightProperty().multiply(522).divide(680));
        AnchorPane.setTopAnchor(hbox2, anchor.getPrefWidth()*235/1280);

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

    private void sendEmail() throws MessagingException {
        SendEmail se = new SendEmail(email,"brighamandwomenshospital1@gmail.com");
        se.setMessageSubject("External Patient Transportation Request");
        se.setMessageText("Hello "+ assignedFirstName + " " + assignedLastName +", " +
                "\n " +
                "\nYou have a new service request!" +
                "\nPatient Name: " + patientName.getText() +
                "\nLocation: " + location +
                "\nAddress: " + address.getText() + ", " + town.getText() + ", " + usState.getText() + " " + zip.getText() +
                "\nTransport Type: " + transportTypes.getText() +
                "\nDate: " + date +
                "\nTime: " + time +
                "\nReason: " + transportReason.getText() +
                "\n" +
                "\n-Team C");
        se.sendMessage();
    }


    private void addExternalRequest() throws SQLException {
        String patientName = this.patientName.getText();
        String street = this.address.getText();
        String town = this.town.getText();
        String usState = this.usState.getText();
        String type = this.transportTypes.getText();
        System.out.println(type);
        String reason = this.transportReason.getText();
        Integer zipcode = Integer.parseInt(this.zip.getText());
        String isComplete = "False";
        java.sql.Date gettedDatePickerDate = java.sql.Date.valueOf(getDate.getValue());
        java.sql.Time gettedTimePickerDate = java.sql.Time.valueOf(getTime.getValue());
        date = gettedDatePickerDate.toString();
        time = gettedTimePickerDate.toString();
        ExternalAddress ea = new ExternalAddress(street,town,usState,zipcode);
        database.addAddress(ea);
        ServiceRequest sr = new ServiceRequest(currentUser, location, reason, assignUserName, isComplete);
        database.addServiceRequest(sr);
        int addressID = ea.getAddressID();
        int serviceID = sr.getRequestID();
        ExternalPatientRequest er = new ExternalPatientRequest(serviceID, currentUser, patientName, reason, location, addressID, type, assignUserName, isComplete, date, time);
        database.addExternalPatientRequest(er);
    }

    public void goEmergency(ActionEvent actionEvent) {
        try {
            Capp.prevPage = "/edu/wpi/teamC/fxml/serviceRequest/ExtPatTrans.fxml";
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/serviceRequest/Emergency.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
            timeline.stop();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    protected void goSubmit(ActionEvent e) {
        try {
            addExternalRequest();
            sendEmail();
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/ThankYou.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
            timeline.stop();
        } catch (IOException | SQLException | MessagingException ex) {
            ex.printStackTrace();
        }

    }

    @FXML
    void onAmbulance(ActionEvent event) {
        transportTypes.setText("Ambulance");
    }

    @FXML
    void onHelicopter(ActionEvent event) {
        transportTypes.setText("Helicopter");
    }

    @FXML
    void onPlane(ActionEvent event) {
        transportTypes.setText("Plane");
    }

    /*
    @FXML
    void onNode(ActionEvent event) {locationChooser.setText(chooseLocationOnAction(event))}

     */

    public void chooseLocationOnAction(ActionEvent actionEvent) {
        /*
        int selectedIndex = locationChooser.getItems().;
        Object selectedItem = locationChooser.getSelectionModel().getSelectedItem();

        System.out.println("Selection made: [" + selectedIndex + "] " + selectedItem);
        System.out.println("   ComboBox.getValue(): " + locationChooser.getValue());

        System.out.println(nodes.get(selectedIndex).getShortName());
        location = nodes.get(selectedIndex).nodeID;

         */
    }

    @FXML
    void chooseEmployeeOnAction(ActionEvent event) {

    }

    @FXML
    private void validateButton(){
        if(patientName.getText().isEmpty() || address.getText().isEmpty() || town.getText().isEmpty() || usState.getText().isEmpty() ||
            /*transportTypes.getValue() == null ||*/   zip.getText().isEmpty()  /*assignTo.getValue() == null ||/*
            /*locationChooser.getValue() == null ||*/) {
            submitButton.setDisable(true);
        } else {
            submitButton.setDisable(false);
        }
    }

    public void goServiceSystems(ActionEvent actionEvent) {
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
    public void keyTyped(KeyEvent actionEvent){
        s.setPrevTime();
    }

    @FXML
    public void mouseMoved(MouseEvent actionEvent){
        s.setPrevTime();
    }
}

