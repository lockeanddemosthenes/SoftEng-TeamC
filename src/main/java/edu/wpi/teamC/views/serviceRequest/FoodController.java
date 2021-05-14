package edu.wpi.teamC.views.serviceRequest;

import com.jfoenix.controls.*;
import edu.wpi.teamC.Capp;
import edu.wpi.teamC.SendEmail;
import edu.wpi.teamC.Translate;
import edu.wpi.teamC.entities.CaretakerSingleton;
import edu.wpi.teamC.entities.Employee;
import edu.wpi.teamC.entities.requests.FoodRequest;
import edu.wpi.teamC.entities.requests.ServiceRequest;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
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

public class FoodController implements ITreeMenuController{
    @FXML JFXTextField name;
    @FXML private Label foodRequestLabel;
    @FXML private JFXButton Emergency;
    @FXML private Text nameText;
    @FXML private Text foodText;
    @FXML private Text sideText;
    @FXML private Text drinkText;
    @FXML private Text numberText;
    @FXML private Text dateText;
    @FXML private Text timeText;
    @FXML private Text assignToText;
    @FXML private Text instructionsText;
    @FXML JFXButton backButton;
    @FXML JFXTextField specialInstructions;
    @FXML MenuButton foodChooser;
    @FXML MenuButton sideChooser;
    @FXML MenuButton drinkChooser;
    @FXML TreeView<NodeDB> locationChooser;
    @FXML JFXDatePicker selectDate;
    @FXML JFXTimePicker selectTime;
    @FXML JFXButton submitButton;
    @FXML MenuButton employeeChooser;
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

    private String assignUserName;
    private String email;
    private String currentUser = LoginController.getCorrectUser();
    private String location = "";
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

    private ArrayList<Employee> employees;
    ArrayList<NodeDB> nodes;

    String[] foodTypes = {"Burger", "Hot Dog", "Pasta", "Pizza", "Chicken Sub", "No Food"};
    private String foodType;
    String[] sideTypes = {"Fries","Garden Salad","Caesar Salad","Fruits","Vegetables","Rice","Soup","No Side"};
    private String sideType;
    String[] drinkTypes = {"Water","Iced Tea","Soda","Lemonade","Coffee","Fruit Punch","Apple Juice","Orange Juice","No drink"};
    private String drinkType;
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
            buttons.add(Emergency);
          //  buttons.add(helpButton);
            buttons.add(backButton);
            buttons.add(submitButton);
            //buttons
            Translate.translateButtonList(buttons);
            //Label
            foodRequestLabel.setText(tr.translate("en", language, foodRequestLabel.getText()));
            //Text
            ArrayList<Text> texts=new ArrayList<>();
            texts.add(nameText);
            texts.add(foodText);
            texts.add(sideText);
            texts.add(drinkText);
            texts.add(numberText);
            texts.add(dateText);
            texts.add(timeText);
            texts.add(assignToText);
            texts.add(instructionsText);
            Translate.translateTextList(texts);
            // Menu Button
            foodChooser.setText(tr.translate("en", language, foodChooser.getText()));
            sideChooser.setText(tr.translate("en", language, sideChooser.getText()));
            drinkChooser.setText(tr.translate("en", language, drinkChooser.getText()));
           // locationChooser.setText(tr.translate("en", language, locationChooser.getText()));
            employeeChooser.setText(tr.translate("en", language, employeeChooser.getText()));
            // TextFields and Time/Date Picker
            name.setPromptText(tr.translate("en", language, name.getPromptText()));
            specialInstructions.setPromptText(tr.translate("en", language, specialInstructions.getPromptText()));
            selectDate.setPromptText(tr.translate("en", language, selectDate.getPromptText()));
            selectTime.setPromptText(tr.translate("en", language, selectTime.getPromptText()));
        } catch (IOException e) {
        e.printStackTrace();
    }
        addFoods();
        addSides();
        addDrinks();
        addEmployees();
        initLegend();
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

    private void addFoods() {
        for (int i = 0; i < foodTypes.length; i++) {
            String type = foodTypes[i];
            MenuItem m =
                    new MenuItem(type);
            m.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    foodChooser.setText(type);
                    foodType = type;
                }
            });
            foodChooser.getItems().add(m);
        }
    }

    private void addSides() {
        for (int i = 0; i < sideTypes.length; i++) {
            String type = sideTypes[i];
            MenuItem m =
                    new MenuItem(type);
            m.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    sideChooser.setText(type);
                    sideType = type;
                }
            });
            sideChooser.getItems().add(m);
        }
    }

    private void addDrinks() {

        for (int i = 0; i < drinkTypes.length; i++) {
            String type = drinkTypes[i];
            MenuItem m =
                    new MenuItem(type);
            m.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    drinkChooser.setText(type);
                    drinkType = type;
                }
            });
            drinkChooser.getItems().add(m);
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

    private void sendEmail() throws MessagingException {
        SendEmail se = new SendEmail(email,"brighamandwomenshospital1@gmail.com");
        se.setMessageSubject("Food Delivery Request");
        se.setMessageText("Hello "+ assignedFirstName + " " + assignedLastName +", " +
                "\n " +
                "\nYou have a new service request!" +
                "\nCustomer Name: " + name.getText() +
                "\nLocation: " + location +
                "\nFood: " + foodType +
                "\nSide: " + sideType +
                "\nDrink: " + drinkType +
                "\nDate: " + date +
                "\nTime: " + time +
                "\nSpecial Instructions: " + specialInstructions.getText() +
                "\n" +
                "\n-Team C");
        se.sendMessage();
    }

    public void goEmergency(ActionEvent actionEvent) {
        try {
            Capp.prevPage = "/edu/wpi/teamC/fxml/serviceRequest/Food.fxml";
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/serviceRequest/Emergency.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
            timeline.stop();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void addFoodServiceRequest() {
        String isComplete = "False";
        String food = foodChooser.getText();
        String side = sideChooser.getText();
        String drink = drinkChooser.getText();
        String instructions = specialInstructions.getText();
        String description = "Order of " + food + " with " + side + " and " + drink;
        java.sql.Date getDatePickerDate = java.sql.Date.valueOf(selectDate.getValue());
        java.sql.Time getTimePickerDate = java.sql.Time.valueOf(selectTime.getValue());
        date = getDatePickerDate.toString();
        time = getTimePickerDate.toString();
        ServiceRequest sr = new ServiceRequest(currentUser, location, description, assignUserName, isComplete);
        database.addServiceRequest(sr);
        FoodRequest fr = new FoodRequest(sr.getRequestID(), currentUser, name.getText(), food, side, drink, location, assignUserName, isComplete, date, time, instructions);
        database.addFoodRequest(fr);
    }

    @FXML
    private void validateButton() {
        if (name.getText().isEmpty() || location.equals("") || foodChooser.getText() == null || sideChooser.getText() == null || drinkChooser.getText() == null
                 || selectDate.getValue() == null || selectTime.getValue() == null) {
            submitButton.setDisable(true);
        } else {
            submitButton.setDisable(false);
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

    public void goSubmit(ActionEvent event) {
        try {
            addFoodServiceRequest();
            sendEmail();
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/ThankYou.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
            timeline.stop();
        } catch (IOException | MessagingException ex) {
            ex.printStackTrace();
        }    }

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
