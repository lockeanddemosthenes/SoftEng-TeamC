package edu.wpi.teamC.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableView;
import edu.wpi.teamC.Capp;
import edu.wpi.teamC.Translate;
import edu.wpi.teamC.entities.Employee;
import edu.wpi.teamC.repository.*;
import edu.wpi.teamC.views.mapEditor.EditNodeController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ListEmployeeController {

    @FXML private JFXTreeTableView<Employee> employeeJFXTreeTableView;
    @FXML private Label employeesHeader;
    @FXML private JFXButton addEmployee;
    @FXML private JFXButton accessEmployee;
    @FXML private JFXButton removeEmployee;
    @FXML private JFXButton emergency;
    @FXML private JFXButton logOut;
    @FXML private JFXButton back;
    @FXML
    private Rectangle r1;
    @FXML
    private Rectangle r2;
    @FXML
    private HBox hbox1;
    @FXML
    private HBox hbox2;
    @FXML
    private HBox hbox3;
    @FXML
    private AnchorPane anchor;
    @FXML
    private ImageView bwh;
    @FXML private VBox vbox;

    private TreeTableColumn<Employee, String> userCol = new TreeTableColumn<>("Username");
    private TreeTableColumn<Employee, String> fistNameCol = new TreeTableColumn<>("First Name");
    private TreeTableColumn<Employee, String> lastNameCol = new TreeTableColumn<>("Last Name");
    private TreeTableColumn<Employee, String> emailCol = new TreeTableColumn<>("Email");
    private TreeTableColumn<Employee, String> passCol = new TreeTableColumn<>("Password");

    private ArrayList<Employee> employees;

    ConnectionSingleton c = ConnectionSingleton.getInstance();
    Connection conn = c.conn;
    DBAccess database = new Database(conn);

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
    public void initialize() throws SQLException {
        System.out.println("Initializing...");

        this.initTable();
        this.loadTreeItems();

        employeeJFXTreeTableView.setTreeColumn(userCol);
        employeeJFXTreeTableView.setShowRoot(false);

        initTranslate();

        clockTimeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> updateTime()));
        clockTimeline.setCycleCount(Animation.INDEFINITE);
        clockTimeline.play();
        timerLabel.textProperty().bind(time);

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

        //width and right anchor for exit button
        logOut.prefWidthProperty().bind(anchor.widthProperty().multiply(85).divide(1280));
        AnchorPane.setRightAnchor(logOut, anchor.getPrefWidth() * 94 / 1280);

        hbox2.prefHeightProperty().bind(anchor.heightProperty().multiply(100).divide(680));
        AnchorPane.setBottomAnchor(hbox2, anchor.getPrefHeight()*86/680);

        hbox3.prefHeightProperty().bind(anchor.heightProperty().multiply(100).divide(680));

        AnchorPane.setBottomAnchor(employeeJFXTreeTableView, anchor.getPrefHeight()*185/680);
        AnchorPane.setTopAnchor(employeeJFXTreeTableView, anchor.getPrefHeight()*250/680);
        AnchorPane.setRightAnchor(employeeJFXTreeTableView, anchor.getPrefWidth()*200/680);
        AnchorPane.setLeftAnchor(employeeJFXTreeTableView, anchor.getPrefWidth()*200/680);



        anchor.setBottomAnchor(timerLabel, 0.0);
        anchor.setRightAnchor(timerLabel, 0.0);
    }

    public void initTranslate() {
        try {
            ArrayList<JFXButton> buttons = new ArrayList<JFXButton>();
            buttons.add(addEmployee);
            buttons.add(accessEmployee);
            buttons.add(removeEmployee);
            buttons.add(logOut);
            buttons.add(emergency);
            buttons.add(back);

            System.out.println(language);
            employeesHeader.setText(tr.translate("en", language, employeesHeader.getText()));
            for (Button button : buttons) {
                if (button != null){
                    button.setText(tr.translate("en",language,button.getText()));
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initTable(){
        employeeJFXTreeTableView.setEditable(false);

        userCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("userName"));
        fistNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("firstName"));
        lastNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("lastName"));
        emailCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("userEmail"));
        passCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("password"));

        userCol.setCellFactory(TextFieldTreeTableCell.<Employee>forTreeTableColumn());
        fistNameCol.setCellFactory(TextFieldTreeTableCell.<Employee>forTreeTableColumn());
        lastNameCol.setCellFactory(TextFieldTreeTableCell.<Employee>forTreeTableColumn());
        emailCol.setCellFactory(TextFieldTreeTableCell.<Employee>forTreeTableColumn());
        passCol.setCellFactory(TextFieldTreeTableCell.<Employee>forTreeTableColumn());


        employeeJFXTreeTableView.getColumns().add(userCol);
        employeeJFXTreeTableView.getColumns().add(emailCol);
        employeeJFXTreeTableView.getColumns().add(passCol);
        employeeJFXTreeTableView.getColumns().add(fistNameCol);
        employeeJFXTreeTableView.getColumns().add(lastNameCol);

    }

    public void loadTreeItems() {
        employees = new ArrayList<Employee>();
        System.out.println("Loading tree items");
        TreeItem<Employee> root = new TreeItem<Employee>(new Employee());
        root.setExpanded(true);
        try {
            employees = database.getListOfEmployees();
            for (int i= 0; i < employees.size(); i++)
                root.getChildren().add(new TreeItem<Employee>(employees.get(i)));

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        employeeJFXTreeTableView.setRoot(root);
        employeeJFXTreeTableView.refresh();
    }

    public void goBack(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/DefaultScreen.fxml"));
            //((Node)event.getSource()).getScene().setRoot(root);
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void goHelp(ActionEvent e) {
        //currently directs to home screen
        try {
            Capp.prevPage = "/edu/wpi/teamC/fxml/ListEmployee.fxml";
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/serviceRequest/DefaultScreenController.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void goAddEmployee(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/AddEmployee.fxml"));
            //((Node)event.getSource()).getScene().setRoot(root);
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        employeeJFXTreeTableView.refresh();
    }

    public void goAccessEmployee(ActionEvent event) {
        if (employeeJFXTreeTableView.getSelectionModel().getSelectedItem() != null) {
            Employee selectedEmployee = employeeJFXTreeTableView.getSelectionModel().getSelectedItem().getValue();
            System.out.println(selectedEmployee);
            System.out.println(employeeJFXTreeTableView.getSelectionModel().getSelectedItems());
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            try {
                //Load second scene
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/wpi/teamC/fxml/EditEmployee.fxml"));
                Parent root = loader.load();

                //Get controller of scene2
                EditEmployeeController editEmployeeController = loader.getController();
                //Pass whatever data you want. You can have multiple method calls here
                editEmployeeController.receiveData(selectedEmployee);

                //Show scene 2 in new window
                //Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Second Window");
                stage.show();
            } catch (IOException e) {
                System.err.println(String.format("Error: %s", e.getMessage()));
            }
        }
        employeeJFXTreeTableView.refresh();
    }

    public void goRemove(ActionEvent actionEvent) {
        if (employeeJFXTreeTableView.getSelectionModel().getSelectedItem() != null) {
            Employee selectedEmployee = employeeJFXTreeTableView.getSelectionModel().getSelectedItem().getValue();
            database.removeEmployee(selectedEmployee.getUserName());
            loadTreeItems();
        }
        employeeJFXTreeTableView.refresh();
    }

    public void goEmergency(ActionEvent actionEvent) {
        try {
            Capp.prevPage = "/edu/wpi/teamC/fxml/ListEmployee.fxml";
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/serviceRequest/Emergency.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void goHome(ActionEvent e) {
        if (Capp.userType.equals("Admin")) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/DefaultScreen.fxml"));
                Capp.getPrimaryStage().getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @FXML
    protected void logOut(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/StartUp.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
