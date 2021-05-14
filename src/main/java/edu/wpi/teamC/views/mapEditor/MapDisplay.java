package edu.wpi.teamC.views.mapEditor;

import com.jfoenix.controls.*;
import edu.wpi.teamC.Capp;

import edu.wpi.teamC.SendEmail;
import edu.wpi.teamC.Translate;
import edu.wpi.teamC.entities.mapEditor.*;
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
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import net.kurobako.gesturefx.GesturePane;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.*;


public class MapDisplay {

    private boolean isAdmin;
    private String navType = "A-Star";

    @FXML
    private Button calculateButton;
    @FXML
    private Group adminGroup;
    @FXML
    JFXComboBox<String> listFloor;
    @FXML
    JFXComboBox<String> listNavType;
    @FXML
    private Text distText;
    @FXML
    private Text timeText;
    @FXML
    private Text mainText;
    @FXML
    private Text startNodeId;
    @FXML
    private Text endNodeId;
    @FXML
    private Text legendText;
    @FXML
    private AnchorPane rootPane;
    @FXML
    public ImageView mapImg;
    @FXML
    private Group lineGroup;
    @FXML
    private Group buttonGroup;
    @FXML
    private Group zoomObjects;
    @FXML
    private JFXSlider zoomAmount;
    @FXML
    public GesturePane zoomPane;
    @FXML
    private JFXTreeView<String> legendTable;
    @FXML
    private Group edgeGroup;

    @FXML
    private JFXTextField editNodeX;
    @FXML
    private JFXTextField editNodeY;
    @FXML
    private JFXTextField editNodeShortName;
    @FXML
    private JFXTextField editNodeLongName;
    @FXML
    private JFXTextField editNodeBuilding;
    @FXML
    private JFXTextField editNodeType;
    @FXML
    private JFXComboBox editFloorMenu;
    @FXML
    private JFXButton editNodeSaveButton;
    @FXML
    private JFXButton editNodeAddButton;
    @FXML
    private Rectangle pathSelectRect;
    @FXML
    private Text editNodeTitle;
    @FXML
    private JFXTextField editNodeID;
    @FXML
    private Group editNodeMenuGroup;
    @FXML
    private Text textDirection;
    @FXML
    private JFXButton nextDirectionButton;
    @FXML
    private Group directionsGroup;
    @FXML
    private JFXToggleButton isEditMode;
    @FXML
    private JFXToggleButton isShowEdges;
    @FXML
    private JFXToggleButton isEmergencyButton;
    @FXML
    private JFXButton sendDirectionsButton;
    @FXML
    private JFXButton restartDirectionsButton;
    @FXML
    private JFXTextField emailField;
    @FXML
    private JFXButton helpButton;
    @FXML
    private JFXButton parkButton;
    @FXML
    private JFXButton resetCSVButton;
    @FXML
    private JFXButton loadCSVButton;
    @FXML
    private JFXButton alignSelection;
    @FXML
    private Group startGroup;
    @FXML
    private Group endGroup;
    @FXML
    private Text navigationText;
    @FXML
    private Text startNodeText;
    @FXML
    private Text endNodeText;
    @FXML
    private Text estimatedDistanceText;
    @FXML
    private Text estimatedTimeText;
    @FXML
    private Text editFloorText;
    @FXML
    private Text editBuildingText;
    @FXML
    private Text editTypeText;
    @FXML
    private Text editShortNameText;
    @FXML
    private Text editLongNameText;
    @FXML
    private Text editNodeIDText;
    @FXML
    private Text currentFloorText;
    @FXML
    private JFXButton clearButton;
    @FXML
    private JFXButton homeButton;
    @FXML
    private JFXButton emergencyButton;
    private Polygon directionArrow;
    @FXML private Rectangle r1;
    @FXML private ImageView bwh;
    @FXML private HBox hbox1;

    @FXML private Label timerLabel;
    private Timeline clockTimeline;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
    private StringProperty time = new SimpleStringProperty("");
    private void updateTime() {
        time.set(timeFormat.format(Calendar.getInstance().getTime()));
    }

    RenderObjects renderEngine;
    private double pathDist;
    private double pathTime;
    private double costPerFoot = 675 / 190; //foot per cost
    private int buttonSize;
    LinkedList<TextDirection> directions;
    private int currDirection = 0;
    private MultiFloorPath currPath;
    private boolean isEmergency = false;
    private String currentUser = LoginController.getCorrectUser();

    DBAccess dbAccess = new Database(ConnectionSingleton.getInstance().conn);
    static LanguageSingleton lang = LanguageSingleton.getInstance();
    String language = lang.s;

    HashMap<String, String> colorMap;
    MapData mapData;

    public MapDisplay() {
        super();
        if (Capp.userType.equals("Admin"))
            isAdmin = true;
        else
            isAdmin = false;
        if (Capp.userType.equals("Patient")) {
            String entryType = dbAccess.getEntryType(currentUser);
            if (entryType != null) {
                isEmergency = (entryType.equals("COVID"));
            } else {
                isEmergency = false;
            }
        }
        //todo:set is emergency
        buttonSize = 6;
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
        } else if (Capp.userType.equals("Employee")) {
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

    public void goEmergency(ActionEvent actionEvent) {
        try {
            Capp.prevPage = "/edu/wpi/teamC/fxml/mapEditor/MapDisplay.fxml";
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/serviceRequest/Emergency.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        if (isAdmin) {
            mapData = new AdminMapData(this, dbAccess, isEmergency);
        } else {
            mapData = new GuestMapData(this, dbAccess, isEmergency);
        }
        double offsetX = mapImg.getLayoutX();
        double offsetY = mapImg.getLayoutY();
        parkButton.setDisable(true);
        alignSelection.setVisible(false);
        buttonGroup.toFront();
        lineGroup.toFront();
        endGroup.toFront();
        startGroup.toFront();
        directionsGroup.setVisible(false);
        initZoom();
        zoomPane.toBack();
        if (isAdmin) {
            renderEngine = new RenderObjectsAdmin();
            adminInit();
            isShowEdges.setDisable(true);
        } else
            renderEngine = new RenderObjects();
        double imageWidth = mapImg.getLayoutBounds().getWidth();
        double imageHeight = mapImg.getLayoutBounds().getHeight();

        //todo: don't hardcode the image size
        int originalImageHeight = 3400;
        int originalImageWidth = 5000;
        initColorMap();
        mapData.setImageFields(originalImageWidth, originalImageHeight, offsetX, offsetY, imageWidth, imageHeight);
        createFloorMenu();
        adminGroup.setVisible(isAdmin);
        editNodeMenuGroup.setVisible(false);
        mapData.setEditMode(false);
        createNavTypeMenu();
        initLegend();
        selectDifferentFloor("1");
        listNavType.setVisible(isAdmin);
        pathSelectRect.setVisible(isAdmin);
        triangleInit();
        translateInit();


        r1.widthProperty().bind(rootPane.widthProperty());
        //r1.heightProperty().bind(rootPane.heightProperty().multiply(140).divide(680));

        //hbox1.prefHeightProperty().bind(rootPane.heightProperty().multiply(140).divide(680));

        bwh.fitWidthProperty().bind(rootPane.widthProperty().multiply(112).divide(1280));
        //bwh.fitHeightProperty().bind(rootPane.heightProperty().multiply(128).divide(680));

        emergencyButton.prefWidthProperty().bind(rootPane.widthProperty().multiply(85).divide(1280));
        AnchorPane.setRightAnchor(emergencyButton, rootPane.getPrefWidth()*94/1280);

        homeButton.prefWidthProperty().bind(rootPane.widthProperty().multiply(85).divide(1280));
        AnchorPane.setRightAnchor(homeButton, rootPane.getPrefWidth()*94/1280);

        AnchorPane.setRightAnchor(adminGroup, rootPane.getPrefWidth()*-11/1280);
        AnchorPane.setBottomAnchor(adminGroup, rootPane.getPrefHeight()*70/680);

        AnchorPane.setBottomAnchor(parkButton, rootPane.getPrefHeight()*49/680);
        AnchorPane.setBottomAnchor(helpButton, rootPane.getPrefHeight()*9/680);

        zoomPane.prefHeightProperty().bind(rootPane.heightProperty().multiply(504).divide(680));
        zoomPane.prefWidthProperty().bind(rootPane.widthProperty().multiply(1022).divide(1280));

        clockTimeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> updateTime()));
        clockTimeline.setCycleCount(Animation.INDEFINITE);
        clockTimeline.play();
        timerLabel.textProperty().bind(time);

        rootPane.setBottomAnchor(timerLabel, 0.0);
        rootPane.setRightAnchor(timerLabel, 100.0);

    }
    private void translateInit(){
        LinkedList<Text> textToTranslate=new LinkedList<>();
        textToTranslate.add(navigationText);
        textToTranslate.add(startNodeText);
        textToTranslate.add( endNodeText);
        textToTranslate.add( estimatedDistanceText);
        textToTranslate.add( estimatedTimeText);
        textToTranslate.add( editFloorText);
        textToTranslate.add( editBuildingText);
        textToTranslate.add( editTypeText);
        textToTranslate.add( editShortNameText);
        textToTranslate.add( editLongNameText);
        textToTranslate.add( editNodeIDText);
        textToTranslate.add( currentFloorText);
        textToTranslate.add(editNodeTitle);
        String mainTranslation="";
        for(Text t:textToTranslate){
            mainTranslation=mainTranslation+" //,// "+t.getText();
        }
        try {
            mainTranslation=Translate.translate("en",lang.s,mainTranslation);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] translatedStrings=mainTranslation.split(" // *, *// ");

        for(int i=0;i<textToTranslate.size();i++){
            System.out.println(translatedStrings[i+1]);
            textToTranslate.get(i).setText(translatedStrings[i+1]);
        }
        LinkedList<JFXButton> buttonToTranslate=new LinkedList<>();
        
        buttonToTranslate.add(nextDirectionButton);
        buttonToTranslate.add(clearButton);
        buttonToTranslate.add(resetCSVButton);
        buttonToTranslate.add(loadCSVButton);
        buttonToTranslate.add(homeButton);
        buttonToTranslate.add(emergencyButton);
        buttonToTranslate.add(parkButton);
        buttonToTranslate.add(helpButton);
        buttonToTranslate.add(sendDirectionsButton);
        buttonToTranslate.add(editNodeSaveButton);
        buttonToTranslate.add(editNodeAddButton);
        buttonToTranslate.add(alignSelection);
        mainTranslation="";
        for(Button b:buttonToTranslate){
            mainTranslation=mainTranslation+" //,// "+b.getText();
        }
        try {
            mainTranslation=Translate.translate("en",lang.s,mainTranslation);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(mainTranslation);
        translatedStrings=mainTranslation.split(" // *, *// ");

        for(int i=0;i<buttonToTranslate.size();i++){
            System.out.println(translatedStrings[i+1]);
            buttonToTranslate.get(i).setText(translatedStrings[i+1]);
        }
        translateToggleButton(isEditMode);
        translateToggleButton(isEmergencyButton);
        translateToggleButton(isShowEdges);
        translatePromptText(listNavType);
        translateTextField(emailField);

    }
    private void translateText(Text t){
        try {
            t.setText(Translate.translate("en",lang.s,t.getText()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void translateButton(JFXButton b){
        try {
            b.setText(Translate.translate("en",lang.s,b.getText()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void translateToggleButton(JFXToggleButton b){
        try {
            b.setText(Translate.translate("en",lang.s,b.getText()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void translatePromptText(JFXComboBox<String> box){
        try {
            box.setPromptText(Translate.translate("en",lang.s,box.getPromptText()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void translateTextField(JFXTextField textField){
        try {
            textField.setPromptText(Translate.translate("",lang.s,textField.getPromptText()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void triangleInit() {
        directionArrow = new Polygon();
        directionArrow.getPoints().addAll(0.0, -5.0, -2.5, 2.5, 2.5, 2.5);
        directionArrow.setFill(Color.BLACK);
        directionArrow.setStroke(Color.RED);
        directionArrow.setVisible(false);
        zoomObjects.getChildren().add(directionArrow);
    }

    private void adminInit() {
        setMainText("Click two nodes to Navigate or Right Click to edit");

        MenuItem addNode = new MenuItem("Add Node...");
        final int[] x = {0};
        final int[] y = {0};
        addNode.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addNode(event, x[0], y[0]);

            }
        });
        ContextMenu mapMenu = new ContextMenu(addNode);
        mapImg.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                if(mapData.isEditMode()) {
                    mapMenu.show(mapImg, event.getScreenX(), event.getScreenY());
                }
                x[0] = (int) event.getX();
                y[0] = (int) event.getY();
            }
        });
        mapImg.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                editNodeX.setText(Integer.toString(Transforms.get_Instance().inverseTransformX(event.getX())));
                editNodeY.setText(Integer.toString(Transforms.get_Instance().inverseTransformY(event.getY())));
            }
        });
    }

    private void initLegend() {
        TreeItem<String> rootNode = new TreeItem<>();
        rootNode.setValue("Map");
        legendTable.setRoot(rootNode);
        rootNode.setExpanded(true);
        legendTable.setShowRoot(false);
        legendTable.setCellFactory(new Callback<TreeView<String>, TreeCell<String>>() {
            @Override
            public TreeCell<String> call(TreeView<String> param) {

                return new TreeCellWithAdminMenu(getThis());


            }
        });
    }

    private void clearEditFields() {
        editNodeID.clear();
        editNodeX.clear();
        editNodeY.clear();
        editNodeBuilding.clear();
        editNodeType.clear();
        editNodeShortName.clear();
        editNodeLongName.clear();
        editNodeID.setPromptText("");
    }

    private MapDisplay getThis() {
        return this;
    }

    private void initZoom() {
        zoomPane.setContent(zoomObjects);
    }

    @FXML
    private void mouseZoom(ZoomEvent event) {
        zoomAmount.setValue(event.getTotalZoomFactor() * 50 + 50);
        zoomMap();
    }

    @FXML
    private void zoomMap() {
        zoomPane.zoomTo((zoomAmount.getValue() + 50) / 50, new Point2D(300, 300));

    }

    @FXML
    void createFloorMenu() {
        listFloor.getItems().clear();
        editFloorMenu.getItems().clear();
        LinkedList<String> listFloors = mapData.getFloorList();
        Collections.sort(listFloors);
        for (String floorName : listFloors) {
            listFloor.getItems().add(floorName);
            editFloorMenu.getItems().add(floorName);
        }
        listFloor.setPromptText("Select floor");
        editFloorMenu.setPromptText("Select floor");
    }

    @FXML
    public void comboBoxPressed() {
        if (listFloor.getSelectionModel().getSelectedItem() != null) {
            selectDifferentFloor(listFloor.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    void createNavTypeMenu() {
        listNavType.getItems().addAll(mapData.getNavTypes());
    }

    @FXML
    public void pathSelectPressed() {
        navType = listNavType.getSelectionModel().getSelectedItem();
    }

    @FXML
    public void calcNav() {
        if (mapData.isNavReady()) {
            lineGroup.getChildren().clear();
            currPath = mapData.navigate(navType);

            renderEngine.renderMultiFloorPath(currPath, mapData.getCurrentFloor(), lineGroup);
            listFloor.getItems().clear();
            listFloor.getItems().addAll(currPath.getActiveFloors());

            //directions
            currDirection = 0;
            directions = currPath.getOriginalPath().generateTextDirections();
            directionsGroup.setVisible(true);
            textDirection.setText(directions.get(currDirection).getDirection());
            nextDirectionPressed();
            System.out.println(directions);

            //add lines to linegroup and total distances
            pathDist = currPath.getCosts() / costPerFoot;
            pathTime += getTime(pathDist);
            distText.setText((int) pathDist + " ft");
            timeText.setText(dispTime(pathTime));
            parkButton.setDisable(!mapData.isReturnReady());
            System.out.println(mapData.isReturnReady());
            directionsPopUp();
        }
    }

    @FXML
    public void toParking() {
        clearButtonPressed();
        if (mapData.setupReturn())
            calcNav();
    }

    public String dispTime(double time) {
        int seconds = (int) time;
        int minutes = 0;
        minutes += seconds / 60;
        seconds -= minutes * 60;
        if (seconds < 10) {
            return minutes + ":0" + seconds;
        } else return minutes + ":" + seconds;
    }

    public void drawButtons(LinkedList<Node> nodes) {
        clearLegend();
        buttonGroup.getChildren().clear();
        for (Node n : nodes) {
            Button b = renderEngine.drawMainButtons(n, buttonSize, buttonGroup, this, mapData.isEditMode(), mapData.isAddEdgeMode());
            renderEngine.addToLegend(n.getID(), n.getShortName(), n.getNodeType(), legendTable, getColorMap(n.getNodeType()));
            if (mapData.getStartId() != null) {
                if (mapData.getStartId().equals(n.getID())) {
                    renderEngine.drawStartButton(b, startGroup, buttonSize, true);
                }
            }
            if (mapData.getEndId() != null) {
                if (mapData.getEndId().equals(n.getID())) {
                    renderEngine.drawStartButton(b, endGroup, buttonSize, false);
                }
            }
        }

    }

    public void buttonClicked(Button button, double screenX, double screenY) {
        String Id = button.getId();
        if (!mapData.isEditMode()) {
            MenuItem start = new MenuItem("Add as start");
            start.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    mapData.setStartId(Id);
                    renderEngine.drawStartButton(button, startGroup, buttonSize, true);
                    calcNav();
                }
            });
            MenuItem end = new MenuItem("Add as destination");
            end.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    mapData.setEndId(Id);
                    renderEngine.drawStartButton(button, endGroup, buttonSize, false);
                    calcNav();
                }
            });
            ContextMenu selectStartOrEnd = new ContextMenu(start, end);
            selectStartOrEnd.show(mapImg, screenX, screenY);

        }

    }


    public double getTime(double dist) {
        double speed = 3.5; //ft/s
        return dist / speed;
    }

    @FXML
    public void selectDifferentFloor(String floor) {
        startGroup.getChildren().clear();
        endGroup.getChildren().clear();
        mapData.setFloor(floor);
        lineGroup.getChildren().clear();
        buttonGroup.getChildren().clear();
        renderEngine.renderMultiFloorPath(mapData.navigate(navType), floor, lineGroup);
        drawButtons(mapData.generateButtonInfo());
        listFloor.setPromptText(floor);
        renderEngine.switchImage(floor, mapImg);
        if (mapData.isShowEdgeMode()) {
            enterShowEdgeMode();
        } else {
            exitShowEdgeMode();
        }
    }

    public void restartDirections(){
        currDirection = 0;
        nextDirectionPressed();
    }

    @FXML
    public void nextDirectionPressed() {
        if (currDirection < directions.size()) {
            String currFloor = directions.get(currDirection).getDirectionAt().getFloor();


            if (!mapData.getCurrentFloor().equals(currFloor)) {
                selectDifferentFloor(currFloor);
            }

            double x = directions.get(currDirection).getDirectionAt().getXcoord();
            double y = directions.get(currDirection).getDirectionAt().getYcoord();
            System.out.println(("X:" + x + " Y: " + y));
            Point2D location = new Point2D(mapData.getTransforms().transformX(x),
                    mapData.getTransforms().transformY(y));
            //todo: zoom on location, having issues moving to next node
            if (currDirection == 0) {
                zoomPane.zoomTo(currPath.getOriginalPath().getZoom(), new Point2D(300, 300));
                zoomPane.centreOn(currPath.getOriginalPath().getCentre());

            } else {
                zoomPane.zoomTo(3.0, location);
                zoomPane.centreOn(location);
            }
            directionArrow.setRotate(directions.get(currDirection).getAngle());
            directionArrow.toFront();
            directionArrow.setLayoutX(location.getX());
            directionArrow.setLayoutY(location.getY());
            directionArrow.setVisible(true);

            textDirection.setText(directions.get(currDirection).getDirection());
            translateText(textDirection);
            currDirection++;
        } else {
            textDirection.setText(directions.getLast().getDirection());
            currDirection = 0;
        }
    }
    public void directionsPopUp(){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Directions");
        alert.setHeaderText("Directions");

        String directionMessage = generateHeirarchicalDirections();
        alert.setContentText(directionMessage);

        ButtonType close = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType sendDirections = new ButtonType("Email directions");
        alert.getButtonTypes().setAll(close, sendDirections);
        Optional<ButtonType> result = alert.showAndWait();

        try {
            if (result.get() == sendDirections) {
                sendDirectionsPopup(directionMessage);
            } else {
                alert.close();
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        alert.initModality(Modality.NONE);
        alert.show();
    }

    private void sendDirectionsPopup(String directionMessage){
        TextInputDialog email = new TextInputDialog("");
        email.setTitle("Email Directions");
        email.setHeaderText("Email Directions");
        email.setContentText("Please enter recipiant email: ");
        Optional<String> result = email.showAndWait();
        try {
            if (result.isPresent()) {
                System.out.println("sending email");
                emailField.setText(result.get());
                System.out.println(result.get());
                sendDirections();
            } else {
                email.close();
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void clearButtonPressed() {
        mapData.initHospitalMapDB();
        createFloorMenu();
        mapData.clearSelection();
        lineGroup.getChildren().clear();
        buttonGroup.getChildren().clear();
        edgeGroup.getChildren().clear();
        endGroup.getChildren().clear();
        startGroup.getChildren().clear();
        drawButtons(mapData.generateButtonInfo());
        mapData.clearAddEdgeFrom();
        editNodeMenuGroup.setVisible(false);
        directionsGroup.setVisible(false);
        directionArrow.setVisible(false);

        pathDist = 0;
        pathTime = 0;
        distText.setText((int) pathDist + " ft");
        timeText.setText(dispTime(pathTime));

        if (mapData.isEditMode()) {
            toEditMode();
            mapData.stopSelecting();
        } else {
            exitEditMode();
            setMainText("Click node to begin navigating");
        }
        if (mapData.isShowEdgeMode()) {
            enterShowEdgeMode();
        } else {
            exitShowEdgeMode();
        }


        clearEditFields();
    }

    public void setMainText(String s) {
        mainText.setText(s);
        System.out.println(mainText.getText());
        translateText(mainText);
        System.out.println(mainText.getText());
    }

    public void initColorMap() {
        colorMap = new HashMap<>();
        colorMap.put("SERV", "Green");
        colorMap.put("ELEV", "Black");
        colorMap.put("REST", "Orange");
        colorMap.put("BATH", "Pink");
        colorMap.put("LABS", "Brown");
        colorMap.put("EXIT", "Tomato");
        colorMap.put("EMER", "RED");
        colorMap.put("DEPT", "Blue");
        colorMap.put("CONF", "Purple");
        colorMap.put("RETL", "DarkOliveGreen");
        colorMap.put("STAI", "DeepPink");
        colorMap.put("INFO", "Lime");
        colorMap.put("WALK", "Grey");
        colorMap.put("PARK", "Orchid");
        colorMap.put("HALL", "Transparent");
    }

    public String getColorMap(String key) {
        if (colorMap.get(key) != null) {
            return colorMap.get(key);
        } else return "Magenta";
    }

    public void setStartNodeId(String startNodeId) {
        this.startNodeId.setText(startNodeId);
    }

    public void setEndNodeId(String endNodeId) {
        this.endNodeId.setText(endNodeId);
    }

    public void legendClicked(String clickedCell, double screenX, double screenY) {
        Node clicked = new Node("friend");
        try {
            clicked = mapData.getNodeByShortName(clickedCell);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Button clickedButton = new Button();
        for (javafx.scene.Node button : buttonGroup.getChildren()) {
            Node checkNode = new Node(button.getId());
            if (clicked.equals(checkNode)) {
                clickedButton = (Button) button;
            }
        }
        Transforms transform = Transforms.get_Instance();
        zoomPane.zoomTo(4.0, new Point2D(300, 300));
        zoomPane.centreOn(new Point2D(transform.transformX(clicked.getXcoord()), transform.transformY(clicked.getYcoord())));
        if (!mapData.isEditMode()) {
            try {
                buttonClicked(clickedButton, screenX, screenY);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (mapData.isAddEdgeMode()) {
            try {
                Edge e = mapData.addEdgeTo(clicked);
                drawNewEdgeLine(mapData.edgeToLine(e));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            MenuItem editNode = new MenuItem("Edit Node");
            editNode.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    editNode(clickedCell);
                }
            });
            MenuItem delNode = new MenuItem("Delete Node");
            delNode.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    deleteNode(clickedCell);
                }
            });
            MenuItem addEdge = new MenuItem("Add edge to...");
            addEdge.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    addEdges(clickedCell);
                }
            });
            ContextMenu menu = new ContextMenu(editNode, delNode, addEdge);
            menu.show(rootPane, screenX, screenY);
        }
    }

    public void clearLegend() {
        initLegend();
    }

    @FXML
    public void editAddButtonPressed() {
        if (!editNodeID.getText().isEmpty()) {
            Node n = new Node(editNodeID.getText());
            Integer x = Integer.parseInt(editNodeX.getText());
            System.out.println(x);
            n.setXcoord(x);
            Integer y = Integer.parseInt(editNodeY.getText());
            n.setYcoord(y);
            n.setBuilding(editNodeBuilding.getText());
            n.setShortName(editNodeShortName.getText());
            n.setLongName(editNodeLongName.getText());
            n.setNodeType(editNodeType.getText());
            if (!editFloorMenu.getSelectionModel().isEmpty()) {
                n.setFloor(editFloorMenu.getSelectionModel().getSelectedItem().toString());
            } else {
                n.setFloor(mapData.getCurrentFloor());
            }
            editNodeMenuGroup.setVisible(false);
            NodeDB node = new NodeDB(n.getID(), Integer.toString(n.getXcoord()), Integer.toString(n.getYcoord()), n.getFloor(), n.getBuilding(), n.getNodeType(), n.getShortName(), n.getLongName());
            dbAccess.addNode(node);
            clearButtonPressed();
        }
        clearEditFields();
    }

    public void addNode(ActionEvent event, int x, int y) {
        clearEditFields();
        mapData.clearSelection();
        toEditMode();
        editNodeMenuGroup.setVisible(true);
        editNodeAddButton.setVisible(true);
        editNodeSaveButton.setVisible(false);

        editNodeID.setDisable(false);
        Transforms t = Transforms.get_Instance();
        System.out.println(t.inverseTransformX(x));
        System.out.println(t.inverseTransformY(y));
        Integer Xint = t.inverseTransformX(x);
        editNodeX.setText(Xint.toString());
        Integer Yint = t.inverseTransformY(y);
        editNodeY.setText(Yint.toString());
        setMainText("Enter fields for new Node");
    }

    public void editNode(String shortName) {
        clearEditFields();
        setMainText("Enter fields to edit then press save");
        editNodeMenuGroup.setVisible(true);
        editNodeSaveButton.setVisible(true);
        editNodeAddButton.setVisible(false);
        Node nodeToEdit;
        try {
            nodeToEdit = mapData.getNodeByShortName(shortName);
        } catch (Exception e) {
            nodeToEdit = new Node("enter Node ID");
            e.printStackTrace();
        }
        setEditTextFields(nodeToEdit);
    }

    void setEditTextFields(Node nodeToEdit) {

        editNodeID.setPromptText(nodeToEdit.getID());
        editNodeID.setDisable(true);
        Integer x = nodeToEdit.getXcoord();
        Integer y = nodeToEdit.getYcoord();
        editNodeX.setText(x.toString());
        editNodeY.setText(y.toString());
        editNodeType.setText(nodeToEdit.getNodeType());
        editNodeBuilding.setText(nodeToEdit.getBuilding());
        editNodeShortName.setText(nodeToEdit.getShortName());
        editNodeLongName.setText(nodeToEdit.getLongName());
        editFloorMenu.setPromptText(nodeToEdit.getFloor());
        editNodeSaveButton.setId(nodeToEdit.getID());



    }

    @FXML
    public void editSaveButtonPressed() {
        Node n;
        try {
            n = mapData.getNodeById(editNodeSaveButton.getId());
        } catch (Exception e) {
            n = new Node("ERROR");
        }

        Integer x = Integer.parseInt(editNodeX.getText());
        System.out.println(x);
        n.setXcoord(x);
        Integer y = Integer.parseInt(editNodeY.getText());
        n.setYcoord(y);
        n.setBuilding(editNodeBuilding.getText());
        n.setShortName(editNodeShortName.getText());
        n.setLongName(editNodeLongName.getText());
        n.setNodeType(editNodeType.getText());
        if (!editFloorMenu.getSelectionModel().isEmpty()) {
            n.setFloor(editFloorMenu.getSelectionModel().getSelectedItem().toString());
        } else {
            n.setFloor(mapData.getCurrentFloor());
        }
        editNodeMenuGroup.setVisible(false);
        dbAccess.updateNodeX(n.getXcoord(), n.getID());
        dbAccess.updateNodeY(n.getYcoord(), n.getID());
        dbAccess.updateNodeFloor(n.getFloor(), n.getID());
        dbAccess.updateNodeBuilding(n.getBuilding(), n.getID());
        dbAccess.updateNodeType(n.getNodeType(), n.getID());
        dbAccess.updateNodeLongName(n.getLongName(), n.getID());
        dbAccess.updateNodeShortName(n.getShortName(), n.getID());
        clearButtonPressed();

    }

    public void deleteNode(String shortName) {
        Node n;
        try {
            n = mapData.getNodeByShortName(shortName);
        } catch (Exception e) {
            e.printStackTrace();
            n = new Node();
        }
        //todo:fix the delete function
        LinkedList<Edge> copyEdge = new LinkedList<>();
        for (Edge e : n.getEdges()) {
            copyEdge.add(new Edge(new Node("_"), new Node("_"), String.valueOf(e.getID())));
        }
        for (Edge e : copyEdge) {
            System.out.println(e);
            removeEdge(e);
        }
        dbAccess.removeNode(n.getID());

        clearButtonPressed();
    }


    public void removeEdge(Edge e) {
        mapData.deleteEdge(e);
        dbAccess.removeEdge(e.getID());
        mapData.deleteEdge(mapData.getReverseEdge(e));
    }

    public void addEdges(String shortName) {
        setMainText("Click nodes to add edge to");

        LinkedList<Edge> edgesToEdit = new LinkedList<>();
        try {
            edgesToEdit = mapData.getNodeByShortName(shortName).getEdges();
            mapData.setAddEdgeFrom(mapData.getNodeByShortName(shortName));

        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Edge e : edgesToEdit) {

            Line line = mapData.edgeToLine(e);
            line.setUserData(e);
            drawNewEdgeLine(line);
        }
        drawButtons(mapData.generateButtonInfo());

    }

    public void editModeUpdate() {

        mapData.setEditMode(isEditMode.isSelected());
        if (isEditMode.isSelected()) {
            parkButton.setDisable(true);
        }
        clearButtonPressed();

    }

    public void showEdgesUpdate() {
        mapData.setShowEdgeMode(isShowEdges.isSelected());
        clearButtonPressed();
    }

    void toEditMode() {
        mapData.setEditMode(true);
        parkButton.setDisable(true);
        isShowEdges.setDisable(false);
        edgeGroup.toFront();
        edgeGroup.getChildren().clear();
        colorMap.put("HALL", "Grey");
        drawButtons(mapData.generateButtonInfo());
        setMainText("Click nodes to edit");
    }

    @FXML
    void isEmergencyChanged() {
        mapData.setEmergency(isEmergencyButton.isSelected());
        isEmergency = isEmergencyButton.isSelected();
    }

    void exitEditMode() {

        mapData.setEditMode(false);
        mapData.setShowEdgeMode(false);
        isShowEdges.setDisable(true);
        editNodeMenuGroup.setVisible(false);
        colorMap.put("HALL", "Transparent");
        drawButtons(mapData.generateButtonInfo());
    }

    private void exitShowEdgeMode() {
        edgeGroup.getChildren().clear();
    }

    private void enterShowEdgeMode() {
        edgeGroup.getChildren().clear();
        for (Line l : mapData.getAllTransformedEdgesAsLines()) {
            MenuItem delete = new MenuItem("Delete Edge");
            delete.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    removeEdge((Edge) l.getUserData());
                    l.setVisible(false);
                }
            });
            ContextMenu menu = new ContextMenu(delete);
            l.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    menu.show(mapImg, event.getScreenX(), event.getScreenY());
                }
            });
            renderEngine.drawLine(l, lineGroup);
        }
    }

    public void drawNewEdgeLine(Line l) {
        renderEngine.drawLine(l, edgeGroup);
    }

    public void dragNode(Button button, Double x, Double y) {

        button.setLayoutX(x - buttonSize / 2);
        button.setLayoutY(y - buttonSize / 2);
        try {
            Node n = mapData.getNodeById(button.getId());
            n.setXcoord(Transforms.get_Instance().inverseTransformX(x));
            n.setYcoord(Transforms.get_Instance().inverseTransformY(y));
            dbAccess.updateNodeX(n.getXcoord(), n.getID());
            dbAccess.updateNodeY(n.getYcoord(), n.getID());
            editNodeX.setText(Integer.toString(n.getXcoord()));
            editNodeY.setText(Integer.toString(n.getYcoord()));
            setEditTextFields(n);
            if (!editNodeMenuGroup.isVisible()) {

                editNodeMenuGroup.setVisible(true);
                editNodeSaveButton.setVisible(true);

                editNodeAddButton.setVisible(false);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        {

        }

        for (javafx.scene.Node line : (lineGroup.getChildren())) {
            if (line instanceof Line) {
                Line l = (Line) line;
                Edge e = (Edge) line.getUserData();
                try {
                    if (e.getStartNode().equals(mapData.getNodeById(button.getId()))) {
                        l.setStartX(x);
                        l.setStartY(y);
                    } else if (e.getDestinationNode().equals(mapData.getNodeById(button.getId()))) {
                        l.setEndX(x);
                        l.setEndY(y);
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }


        }

    }


    public void addEdgeTo(String id) {
        try {
            Edge e = mapData.addEdgeTo(mapData.getNodeById(id));
            drawNewEdgeLine(mapData.edgeToLine(e));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addToSelection(String nodeID) {

        alignSelection.setVisible(true);
        try {
            mapData.addToSelection(mapData.getNodeById(nodeID));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearSelection() {
        clearButtonPressed();
        alignSelection.setVisible(false);
    }

    @FXML
    public void alignSelection() {

        mapData.alignSelection();
        clearSelection();

    }

    public void legendSelection(String t) {
        if (mapData.isEditMode()) {
            try {
                Node n = mapData.getNodeByShortName(t);
                zoomPane.zoomTo(3.0, new Point2D(300, 300));
                Transforms transform = Transforms.get_Instance();
                zoomPane.centreOn(new Point2D(transform.transformX(n.getXcoord()), transform.transformY(n.getYcoord())));
                addToSelection(n.getID());
                for (javafx.scene.Node button : buttonGroup.getChildren()) {
                    Node checkNode = new Node(button.getId());
                    if (n.equals(checkNode)) {
                        button.setStyle("-fx-background-color: " + getColorMap(n.getNodeType()));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void helpButtonPressed() {
        System.out.println("help Button Pressed");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/mapEditor/MapDisplayHelp.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void sendDirections() throws MessagingException, Exception {
        String email = "nkobayashi@wpi.edu";
        if (!emailField.getText().contains("@")) {
            emailField.setPromptText("Invalid email entered.");
            return;
        } else{
            email = emailField.getText();
        }

        String emailDirections = generateHeirarchicalDirections();

        SendEmail se = new SendEmail(email, "brighamandwomenshospital1@gmail.com");
        se.setMessageSubject("Your Directions at Brigham And Womens Hospital");
        se.setMessageText("Please follow these directions from " + mapData.getNodeById(mapData.getStartId()).getShortName() + " to " + mapData.getNodeById(mapData.getStartId()).getShortName() + ". " +
                "\n " +
                "\n " +
                "Directions: " +
                "\n" + emailDirections +
                "\n" +
                "\n" + "Thank you, " +
                "\nTeam Chocolate Cthulhu");
        System.out.println("Directions Sent to " + email);
        se.sendMessage();
    }

    private String generateHeirarchicalDirections(){
        String formatedDirections = "Floor " + directions.get(0).getDirectionAt().getFloor() + ": \n";
        for (TextDirection s : directions) {
            formatedDirections = formatedDirections + "\t" + s.getDirection() + " \n";
            if (s.getDirection().contains("stairs") || s.getDirection().contains("elevator")) { // if we are changing floors, add header
                formatedDirections = formatedDirections + "Floor " + s.getDirection().charAt(s.getDirection().length() - 1) + ": \n";
            }
        }
        return formatedDirections;
    }



    private static void configureFileChooser(final FileChooser fileChooser, String title) {
        fileChooser.setTitle(title);
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
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
            dbAccess.addEdge(edge);
        }
        sc.close();
    }

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
            dbAccess.addNode(node);
            System.out.println("Nodes populated");
        }
        sc.close();
    }

    @FXML
    public void resetCSV() throws SQLException {
        File file = new File("src/main/resources/nodesReset.csv");
        if (file != null) {
            dbAccess.resetNodes(file);
            System.out.println("Nodes reset");
        }
        file = new File("src/main/resources/edgesReset.csv");
        if (file != null) {
            dbAccess.resetEdges(file);
            System.out.println("Edges reset");
        }
        file = new File("src/main/resources/nodesReset.csv");
        if (file != null) {
            dbAccess.resetNodes(file);
            System.out.println("Nodes reset");
        }
        System.out.println("CSV Reset.");
        clearButtonPressed();
    }
    @FXML
    public void loadCSV() throws SQLException {
        Stage stage = null;
        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser, "Select Edges CSV");
        File edgesFile = fileChooser.showOpenDialog(stage);

        configureFileChooser(fileChooser, "Select Nodes CSV");
        File nodesFile = fileChooser.showOpenDialog(stage);

        if (nodesFile != null) {
            dbAccess.resetNodes(nodesFile);
        }
        if(edgesFile != null) {
            dbAccess.resetEdges(edgesFile);
        }
        if(nodesFile != null){
            dbAccess.resetNodes(nodesFile);
        }
        System.out.println("CSV loaded.");
        clearButtonPressed();
    }
}



