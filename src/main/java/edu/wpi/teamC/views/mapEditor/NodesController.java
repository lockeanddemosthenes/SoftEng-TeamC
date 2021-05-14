package edu.wpi.teamC.views.mapEditor;

import edu.wpi.teamC.Capp;
import edu.wpi.teamC.entities.CaretakerSingleton;
import edu.wpi.teamC.repository.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class NodesController {
    @FXML
    private TreeTableView<NodeDB> nodeTable;
    private TreeTableColumn<NodeDB, String> idCol = new TreeTableColumn<>("ID");
    private TreeTableColumn<NodeDB, String> xCol = new TreeTableColumn<>("X Coordinate");
    private TreeTableColumn<NodeDB, String> yCol = new TreeTableColumn<>("Y Coordinate");
    private TreeTableColumn<NodeDB, String> floorCol = new TreeTableColumn<>("Floor");
    private TreeTableColumn<NodeDB, String> buildCol = new TreeTableColumn<>("Building");
    private TreeTableColumn<NodeDB, String> typeCol = new TreeTableColumn<>("Type");
    private TreeTableColumn<NodeDB, String> longNameCol = new TreeTableColumn<>("Long Name");
    private TreeTableColumn<NodeDB, String> shortNameCol = new TreeTableColumn<>("Short Name");
    @FXML
    private Button addNodeButton;
    @FXML
    private Button editNodeButton;
    @FXML
    private Button remNodeButton;
    @FXML
    private Button homeButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button importButton;
    @FXML
    private Button Emergency;
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
    CaretakerSingleton s = CaretakerSingleton.getInstance();
    Timeline timeline;
    DBAccess database = new Database(conn);
    static LanguageSingleton lang = LanguageSingleton.getInstance();
    String language = lang.s;

    private void populateNodes(File file) {
        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sc.nextLine();
        while (sc.hasNextLine()) {
            String[] line = sc.nextLine().split(",");
            NodeDB node = new NodeDB(line[0], line[1],
                    line[2], line[3], line[4],
                    line[5], line[6], line[7]);
            database.addNode(node);
            System.out.println("Nodes populated");
        }
        sc.close();

    }

    @FXML
    public void initialize() {
        System.out.println("Initializing...");
        TreeItem<NodeDB> root = new TreeItem<NodeDB>(new NodeDB());
        root.setExpanded(true);
        this.initTable();
        this.loadTreeItems();
        nodeTable.refresh();
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

        clockTimeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> updateTime()));
        clockTimeline.setCycleCount(Animation.INDEFINITE);
        clockTimeline.play();
        timerLabel.textProperty().bind(time);

        anchor.setBottomAnchor(timerLabel, 0.0);
        anchor.setRightAnchor(timerLabel, 0.0);

        //nodeTable.layoutYProperty().bind(anchor.heightProperty().multiply(227).divide(680));
    }

    public void loadTreeItems() {
        System.out.println("Loading tree items");
        TreeItem<NodeDB> root = new TreeItem<NodeDB>(new NodeDB());
        root.setExpanded(true);

        try {
            ArrayList<NodeDB> nodes = database.getListOfNodes();
            for (int i = 0; i < nodes.size(); i++) {
                root.getChildren().add(new TreeItem<NodeDB>(nodes.get(i)));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        nodeTable.setRoot(root);
        nodeTable.refresh();
    }

    public void loadTreeItems(File file) {
        TreeItem<NodeDB> root = new TreeItem<NodeDB>(new NodeDB(file.getName(), "", "", "", "", "", "", ""));
        root.setExpanded(true);
        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sc.nextLine();

        while (sc.hasNextLine()) {
            String[] line = sc.nextLine().split(",");
            NodeDB n = new NodeDB(line[0], line[1], line[2], line[3], line[4], line[5], line[6], line[7]);
            root.getChildren().add(new TreeItem<NodeDB>(n));
            System.out.println("NodeDB " + n.getNodeID() + " added to tree");
        }
        nodeTable.setRoot(root);
        nodeTable.refresh();
    }

    public void goEmergency(ActionEvent actionEvent) {
        try {
            Capp.prevPage = "/edu/wpi/teamC/fxml/mapEditor/Nodes.fxml";
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/serviceRequest/Emergency.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
            timeline.stop();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void addNode(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/mapEditor/AddNode.fxml"));
            //((NodeDB)event.getSource()).getScene().setRoot(root);
            Capp.getPrimaryStage().getScene().setRoot(root);
            timeline.stop();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        nodeTable.refresh();

    }


    @FXML
    void editNode(ActionEvent event) {
        if (nodeTable.getSelectionModel().getSelectedItem() != null) {
            NodeDB selectedNode = nodeTable.getSelectionModel().getSelectedItem().getValue();
            System.out.println(selectedNode);
            System.out.println(nodeTable.getSelectionModel().getSelectedItems());
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            try {
                //Load second scene
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/wpi/teamC/fxml/mapEditor/EditNode.fxml"));
                Parent root = loader.load();

                //Get controller of scene2
                EditNodeController editNodeController = loader.getController();
                //Pass whatever data you want. You can have multiple method calls here
                editNodeController.receiveData(selectedNode);

                //Show scene 2 in new window
                //Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Second Window");
                stage.show();
            } catch (IOException e) {
                System.err.println(String.format("Error: %s", e.getMessage()));
            }
        }
        nodeTable.refresh();

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


    @FXML
    void removeNode() {
        if (nodeTable.getSelectionModel().getSelectedItem() != null) {
            NodeDB selectedNode = nodeTable.getSelectionModel().getSelectedItem().getValue();
            database.removeNode(selectedNode.getNodeID());
            loadTreeItems();
        }
        nodeTable.refresh();
    }


    @FXML
    void save() throws IOException {
        nodeTable.refresh();
        database.saveNodes();
    }

    @FXML
    void importCSV() throws SQLException {
        Stage stage = null;
        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            database.deleteNodes();
            populateNodes(file);
            database.displayAllNodes();
            loadTreeItems(file);
        }
    }

    private static void configureFileChooser(final FileChooser fileChooser) {
        fileChooser.setTitle("Open CSV File");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
    }


    public void initTable() {
        nodeTable.setEditable(false);

        idCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("nodeID"));
        xCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("xCoord"));
        yCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("yCoord"));
        floorCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("floor"));
        buildCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("building"));
        typeCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("nodeType"));
        longNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("longName"));
        shortNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("shortName"));

        idCol.setCellFactory(TextFieldTreeTableCell.<NodeDB>forTreeTableColumn());
        xCol.setCellFactory(TextFieldTreeTableCell.<NodeDB>forTreeTableColumn());
        yCol.setCellFactory(TextFieldTreeTableCell.<NodeDB>forTreeTableColumn());
        floorCol.setCellFactory(TextFieldTreeTableCell.<NodeDB>forTreeTableColumn());
        buildCol.setCellFactory(TextFieldTreeTableCell.<NodeDB>forTreeTableColumn());
        typeCol.setCellFactory(TextFieldTreeTableCell.<NodeDB>forTreeTableColumn());
        longNameCol.setCellFactory(TextFieldTreeTableCell.<NodeDB>forTreeTableColumn());
        shortNameCol.setCellFactory(TextFieldTreeTableCell.<NodeDB>forTreeTableColumn());


        nodeTable.getColumns().add(idCol);
        nodeTable.getColumns().add(xCol);
        nodeTable.getColumns().add(yCol);
        nodeTable.getColumns().add(floorCol);
        nodeTable.getColumns().add(buildCol);
        nodeTable.getColumns().add(typeCol);
        nodeTable.getColumns().add(longNameCol);
        nodeTable.getColumns().add(shortNameCol);
    }


    @FXML
    void reset() throws SQLException {
        File file = new File("src/main/resources/nodesReset.csv");
        if (file != null) {
            database.deleteNodes();
            populateNodes(file);
            database.displayAllNodes();
            loadTreeItems(file);
        }

        System.out.println("Nodes Reset.");
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
    public void mouseMoved(MouseEvent actionEvent){
        s.setPrevTime();
    }

}
