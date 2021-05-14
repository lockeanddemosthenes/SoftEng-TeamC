package edu.wpi.teamC.views.serviceRequest;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeView;
import edu.wpi.teamC.Capp;
import edu.wpi.teamC.SendEmail;
import edu.wpi.teamC.Translate;
import edu.wpi.teamC.entities.CaretakerSingleton;
import edu.wpi.teamC.entities.requests.InterpreterRequest;
import edu.wpi.teamC.entities.requests.ServiceRequest;
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

public class InterpreterController implements ITreeMenuController{


    @FXML private JFXButton submitButton;
    @FXML private JFXButton backButton;
    @FXML private JFXTextField patName;
    @FXML private JFXTextField summary;
    @FXML private JFXButton Emergency;
    @FXML private JFXTreeView<NodeDB> locationChooser;
    @FXML private MenuButton languageChooser;
    @FXML private MenuButton employeeChooser;
    @FXML private Label interpreterTitle;
    @FXML private Text nameText;
    @FXML private Text languageText;
    @FXML private Text summaryText;
    @FXML private Text locationText;
    @FXML private Text employeeText;
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


    private String currentUser = LoginController.getCorrectUser();

    private ArrayList<NodeDB> nodes;
    private ArrayList<Employee> employees;
    private String location;
    private String email;
    private String assignUserName;
    private String assignedFirstName;
    private String assignedLastName;

    ConnectionSingleton c = ConnectionSingleton.getInstance();
    Connection conn = c.conn;
    DBAccess database = new Database(conn);
    CaretakerSingleton s = CaretakerSingleton.getInstance();
    Timeline timeline;
    static LanguageSingleton lang = LanguageSingleton.getInstance();
    String language = lang.s;
    Translate tr = new Translate();

    String[] languages = {"Espanol","Portugues", "Italiano","Francais","Chinese","Japanese","Hindi","Arabic","Russian","English"};
    private String languageSelected;

    @FXML private Label timerLabel;
    private Timeline clockTimeline;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
    private StringProperty time = new SimpleStringProperty("");
    private void updateTime() {
        time.set(timeFormat.format(Calendar.getInstance().getTime()));
    }

    public void translate() throws IOException {
        ArrayList<ButtonBase> buttons = new ArrayList<ButtonBase>();
        ArrayList<Text> texts = new ArrayList<Text>();
        buttons.add(submitButton);
        buttons.add(backButton);
        buttons.add(Emergency);
        tr.translateButtonList(buttons);
        patName.setPromptText(tr.translate("en", language, patName.getPromptText()));
        summary.setPromptText(tr.translate("en", language, patName.getPromptText()));
        texts.add(languageText);
        texts.add(summaryText);
        texts.add(locationText);
        texts.add(employeeText);
        tr.translateTextList(texts);
        interpreterTitle.setText(tr.translate("en", language, interpreterTitle.getText()));
        languageChooser.setText(tr.translate("en", language, languageChooser.getText()));
        employeeChooser.setText(tr.translate("en", language, employeeChooser.getText()));

    }

    @FXML
    public void initialize() throws SQLException, IOException {
        //database.displayAllNodes();
        translate();
        addLanguages();
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

        vbox.prefHeightProperty().bind(anchor.heightProperty().multiply(522).divide(680));
        AnchorPane.setTopAnchor(vbox, anchor.getPrefWidth()*235/1280);


    }

    private void addLanguages() {
        for (int i = 0; i < languages.length; i++) {
            String lang = languages[i];
            MenuItem m =
                    new MenuItem(lang);
            m.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    languageChooser.setText(lang);
                    languageSelected = lang;
                }
            });
            languageChooser.getItems().add(m);
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

    @FXML
    protected void addLanguageRequest() {
        String lang = languageChooser.getText();
        String request = summary.getText();
        String name = patName.getText();
        String description = "Request for a(n) " + lang + " interpreter with special request: " + request;
        ServiceRequest sr = new ServiceRequest(currentUser, location, description, assignUserName);
        database.addServiceRequest(sr);
        InterpreterRequest ir = new InterpreterRequest(sr.getRequestID(), currentUser,name,lang,location,request,assignUserName);
        database.addInterpreterRequest(ir);
    }

    private void sendEmail() throws MessagingException {
        SendEmail se = new SendEmail(email,"brighamandwomenshospital1@gmail.com");
        se.setMessageSubject("Interpreter Request");
        se.setMessageText("Hello "+ assignedFirstName + " " + assignedLastName +", " +
                "\n " +
                "\nYou have a new service request!" +
                "\nName: " + patName.getText() +
                "\nLocation: " + location +
                "\nLanguage: " + languageSelected +
                /*"\nDate: " + date +
                "\nTime: " + time +*/
                "\nRequest Summary: " + summary.getText() +
                "\n" +
                "\n-Team C");
        se.sendMessage();
    }

    @FXML
    public void goSubmit(ActionEvent e) throws MessagingException {
        addLanguageRequest();
        sendEmail();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/ThankYou.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
            timeline.stop();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void goEmergency(ActionEvent actionEvent) {
        try {
            Capp.prevPage = "/edu/wpi/teamC/fxml/serviceRequest/Interpreter.fxml";
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/serviceRequest/Emergency.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
            timeline.stop();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void validateButton(){
        if(patName.getText().isEmpty() || languageChooser.getText().isEmpty() || employeeChooser.getText().isEmpty()
        || location.equals("")){
            submitButton.setDisable(true);
        } else {
            submitButton.setDisable(false);
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

    @FXML
    public void keyTyped(KeyEvent actionEvent){
        s.setPrevTime();
    }

    @FXML
    public void mouseMoved(MouseEvent actionEvent){
        s.setPrevTime();
    }

}
