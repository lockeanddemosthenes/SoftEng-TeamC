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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

@Slf4j
public class EdgeController {
        @FXML private TreeTableView<EdgeDB> edgeTreeTableView;
        private TreeTableColumn<EdgeDB, String> edgeId = new TreeTableColumn<>("Edge ID");
        private TreeTableColumn<EdgeDB, String> startNode = new TreeTableColumn<>("Start Node");
        private TreeTableColumn<EdgeDB, String> endNode = new TreeTableColumn<>("End Node");

        @FXML private AnchorPane anchor;
        @FXML private Rectangle r1;
        @FXML private Rectangle r2;
        @FXML private HBox hbox1;
        @FXML private HBox hbox2;
        @FXML private HBox hbox3;
        @FXML private HBox hbox4;
        @FXML private JFXButton emergency;
        @FXML private ImageView bwh;

        @FXML private Label timerLabel;
        private Timeline clockTimeline;
        private SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
        private StringProperty time = new SimpleStringProperty("");
        private void updateTime() {
                time.set(timeFormat.format(Calendar.getInstance().getTime()));
        }


        private ArrayList<EdgeDB> edges = new ArrayList<EdgeDB>();

        ConnectionSingleton c = ConnectionSingleton.getInstance();
        Connection conn = c.conn;
        //DBAccess database = new DBAccess(conn);
        CaretakerSingleton s = CaretakerSingleton.getInstance();
        Timeline timeline;
        DBAccess database = new Database(conn);

        static LanguageSingleton lang = LanguageSingleton.getInstance();
        String language = lang.s;

        public void goEmergency(ActionEvent actionEvent) {
                try {
                        Capp.prevPage = "/edu/wpi/teamC/fxml/mapEditor/Edges.fxml";
                        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/serviceRequest/Emergency.fxml"));
                        Capp.getPrimaryStage().getScene().setRoot(root);
                        timeline.stop();
                } catch (IOException ex) {
                        ex.printStackTrace();
                }
        }

        @FXML
        void addEdge(ActionEvent event) {
                try {
                        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/mapEditor/AddEdge.fxml"));
                        //((Node)event.getSource()).getScene().setRoot(root);
                      Capp.getPrimaryStage().getScene().setRoot(root);
                      timeline.stop();
                } catch (IOException ex) {
                        ex.printStackTrace();
                }
                edgeTreeTableView.refresh();
        }

        @FXML
        void editEdge(ActionEvent event) {
                if (edgeTreeTableView.getSelectionModel().getSelectedItem() != null) {
                        EdgeDB selectedEdge = edgeTreeTableView.getSelectionModel().getSelectedItem().getValue();
                        System.out.println(selectedEdge);
                        System.out.println(edgeTreeTableView.getSelectionModel().getSelectedItems());
                        Node node = (Node) event.getSource();
                        Stage stage = (Stage) node.getScene().getWindow();
                        try {
                                //Load second scene
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/wpi/teamC/fxml/mapEditor/EditEdge.fxml"));
                                Parent root = loader.load();
                                //Get controller of scene2
                                EditEdgeController editEdgeController = loader.getController();
                                //Pass whatever data you want. You can have multiple method calls here
                                editEdgeController.receiveData(selectedEdge);
                                //Show scene 2 in new window
                                //Stage stage = new Stage();
                                stage.setScene(new Scene(root));
                                stage.setTitle("Second Window");
                                stage.show();
                        } catch (IOException e) {
                                System.err.println(String.format("Error: %s", e.getMessage()));
                        }

                }
                edgeTreeTableView.refresh();
        }

        @FXML
        private void goHome(ActionEvent e) {
                try {
                        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/DefaultScreen.fxml"));
                        Capp.getPrimaryStage().getScene().setRoot(root);
                } catch (IOException ex) {
                        ex.printStackTrace();
                }
        }

        @FXML
        void removeEdge(ActionEvent event) {
                if (edgeTreeTableView.getSelectionModel().getSelectedItem() != null) {
                        EdgeDB selectedEdge = edgeTreeTableView.getSelectionModel().getSelectedItem().getValue();
                        database.removeEdge(selectedEdge.getEdgeID());
                        loadTreeItems();
                }
                edgeTreeTableView.refresh();
        }

        private void populateEdges(File file) {
                Scanner sc = null;
                try {
                        sc = new Scanner(file);
                } catch (IOException e) {
                        e.printStackTrace();
                }
                sc.nextLine();
                while (sc.hasNextLine()) {
                        String[] line = sc.nextLine().split(",");
                        EdgeDB edge = new EdgeDB(line[0], line[1], line[2]);
                        database.addEdge(edge);
                } sc.close();
        }

        @FXML
        public void initialize() throws SQLException {
                System.out.println("Initializing...");
                Connection connection = null;
                try {
                        //substitute your database name for myDB
                        connection = DriverManager.getConnection("jdbc:derby:cDB;create=true");
                } catch (SQLException e) {
                        System.out.println("Connection failed. Check output console.");
                        e.printStackTrace();
                        return;
                }
                System.out.println("Apache Derby connection established!");


                this.initTable();
                this.loadTreeItems();

                edgeTreeTableView.setTreeColumn(edgeId);
                edgeTreeTableView.setShowRoot(false);
                connection.close();

                s.setPrevTime();
                timeline = new Timeline(new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                                // check if caretaker prev time is far back from current time
                                if ((System.currentTimeMillis() - s.getPrevTime()) > 60000) {
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
                hbox2.prefHeightProperty().bind(anchor.heightProperty().multiply(294).divide(680));
                //todo: figure this out later (treenode table resizing)
                AnchorPane.setTopAnchor(hbox2, anchor.getPrefHeight()*190/680);

                //height and bottom anchor for middle hbox with picture in it
                hbox3.prefHeightProperty().bind(anchor.heightProperty().multiply(62).divide(680));
                AnchorPane.setBottomAnchor(hbox3, anchor.getPrefHeight()*101/680);

                hbox4.prefHeightProperty().bind(anchor.heightProperty().multiply(82).divide(680));
                //AnchorPane.setBottomAnchor(hbox4, anchor.getPrefHeight()*101/680);


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

        public void loadTreeItems() {
                System.out.println("Loading tree items");
                TreeItem<EdgeDB> root = new TreeItem<EdgeDB>(new EdgeDB());
                root.setExpanded(true);
                try {
                        ArrayList<EdgeDB> edges = database.getListofEdges();
                        for (int i= 0; i < edges.size(); i++)
                                root.getChildren().add(new TreeItem<EdgeDB>(edges.get(i)));

                } catch (SQLException throwables) {
                        throwables.printStackTrace();
                }
                edgeTreeTableView.setRoot(root);
                edgeTreeTableView.refresh();
        }

        public void loadTreeItems(File file) {
                TreeItem<EdgeDB> root = new TreeItem<EdgeDB>(new EdgeDB(file.getName(),"",""));
                root.setExpanded(true);
                Scanner sc = null;
                try{
                        sc = new Scanner(file);
                } catch (IOException e) {
                        e.printStackTrace();
                }
                sc.nextLine();

                while(sc.hasNextLine()){
                        String[] line = sc.nextLine().split(",");
                        EdgeDB e = new EdgeDB(line[0],line[1],line[2]);
                        root.getChildren().add(new TreeItem<EdgeDB>(e));
                        System.out.println("Edge" +  e.edgeID + "added to tree");
                }
                edgeTreeTableView.setRoot(root);
                edgeTreeTableView.refresh();
        }


        @FXML
        void save() throws IOException {
                edgeTreeTableView.refresh();
               database.saveEdges();
        }

        @FXML
        void importCSV() throws SQLException {
                Stage stage = null;
                FileChooser fileChooser = new FileChooser();
                configureFileChooser(fileChooser);
                File file = fileChooser.showOpenDialog(stage);
                if (file != null) {
                        database.deleteEdges();
                        populateEdges(file);
                        database.displayAllEdges();
                        loadTreeItems(file);
                }
        }

        private static void configureFileChooser(final FileChooser fileChooser){
                fileChooser.setTitle("Open CSV File");
                fileChooser.setInitialDirectory(
                        new File(System.getProperty("user.home")));
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        }

        private void initTable(){
                edgeTreeTableView.setEditable(false);

                edgeId.setCellValueFactory(new TreeItemPropertyValueFactory<>("edgeID"));
                startNode.setCellValueFactory(new TreeItemPropertyValueFactory<>("startNode"));
                endNode.setCellValueFactory(new TreeItemPropertyValueFactory<>("endNode"));


                edgeId.setCellFactory(TextFieldTreeTableCell.<EdgeDB>forTreeTableColumn());
                startNode.setCellFactory(TextFieldTreeTableCell.<EdgeDB>forTreeTableColumn());
                endNode.setCellFactory(TextFieldTreeTableCell.<EdgeDB>forTreeTableColumn());


                edgeTreeTableView.getColumns().add(edgeId);
                edgeTreeTableView.getColumns().add(startNode);
                edgeTreeTableView.getColumns().add(endNode);
        }
        @FXML
        void resetCSV() throws SQLException {
                File file = new File("src/main/resources/edgesReset.csv");
                if (file != null) {
                        database.deleteEdges();
                        populateEdges(file);
                        database.displayAllEdges();
                        loadTreeItems(file);
                }
                System.out.println("Edges Reset.");
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
