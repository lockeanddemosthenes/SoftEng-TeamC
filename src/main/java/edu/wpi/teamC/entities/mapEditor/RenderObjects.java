package edu.wpi.teamC.entities.mapEditor;

import edu.wpi.teamC.views.mapEditor.MapDisplay;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;

public class RenderObjects {

    public Button drawMainButtons(Node node, int buttonSize, Group buttonGroup, MapDisplay m,boolean editMode,boolean addEdgeMode) {



        Button button = new Button();
        button.setId(node.getID());

        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                    m.buttonClicked(button,event.getScreenX(),event.getScreenY());

            }
        });
        button.setPrefSize(buttonSize, buttonSize);

        button.setStyle("-fx-border-color: " + m.getColorMap((node.getNodeType())) + ";-fx-border-width: 1px;-fx-background-color: Transparent;");


        button.setMinSize(1, 1);
        button.setLayoutX(node.getXcoord() - buttonSize / 2);
        button.setLayoutY(node.getYcoord() - buttonSize / 2);

        buttonGroup.getChildren().add(button);
        return button;
    }
    public static void drawStartButton(Button button, Group g, int buttonSize,boolean isStart){
        g.getChildren().clear();
        Button startButton=new Button();
        int deltaSize=2;
        startButton.setLayoutX(button.getLayoutX()-deltaSize/2);
        startButton.setLayoutY(button.getLayoutY()-deltaSize/2);
        startButton.setOnMouseClicked(button.getOnMouseClicked());
        startButton.setOnMousePressed(button.getOnMousePressed());
        startButton.setMinSize(1,1);
        startButton.setPrefSize(buttonSize+deltaSize,buttonSize+deltaSize);
        if(isStart) {
            startButton.setStyle("-fx-border-color: Black;-fx-border-width: 1px;-fx-background-color: Green;");
        }
        else {
            startButton.setStyle("-fx-border-color: Black;-fx-border-width: 1px;-fx-background-color: Red;");
        }

        g.getChildren().add(startButton);
    }
    public static void addToLegend(String nodeId, String legendString, String type, TreeView<String> legendTable,String color) {
        boolean categoryFound = false;
        for (TreeItem<String> category : legendTable.getRoot().getChildren()) {

            if (category.getValue().equals(type)) {
                TreeItem<String> legendElement = new TreeItem<String>(legendString);
                ObservableList<TreeItem<String>> list=category.getChildren();
                int i=0;
                for(TreeItem<String> compare:list){
                    int minimum=Math.min(compare.getValue().length(),legendString.length());
                    if(compare.getValue().toUpperCase().substring(0,minimum).compareTo(legendString.toUpperCase().substring(0,minimum))<1){
                        i+=1;
                    }
                    else{
                        break;
                    }
                }

                category.getChildren().add(i,legendElement);
                categoryFound = true;
            }

        }
        if (!categoryFound) {
            TreeItem<String> c = new TreeItem<>(type);
            Rectangle r = new Rectangle(20, 10);
            c.setGraphic(r);
            r.setStyle("-fx-fill: " +color);
            c.getChildren().add(new TreeItem<String>(legendString));
            legendTable.getRoot().getChildren().add(c);


        }

    }
    public static void switchImage(String floor, ImageView mapImg){
        URL url = RenderObjects.class.getResource("/edu/wpi/teamC/fxml/img/" + floor + ".png");
        try {
            Image image = SwingFXUtils.toFXImage(ImageIO.read(url), null);
            mapImg.setImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void renderMultiFloorPath(MultiFloorPath m,String currentFloor, Group g){


        if(m.getPaths().get(currentFloor)!=null){
        for(Path p:m.getPaths().get(currentFloor)){

            renderPath(p,g);
        }}
    }
    public static void renderPath(Path p,Group g){
        LinkedList<Line> lines=pathToLines(p);
        for(Line l:lines){
            drawAnimatedLine(l,g);
        }
    }
    public static LinkedList<Line> pathToLines(Path p) {
        Transforms transforms = Transforms.get_Instance();
        LinkedList<Line> lines = new LinkedList<>();
        LinkedList<Node> pathNodes = p.getPath();
        for (int i = 0; i < pathNodes.size() - 1; i++) {
            Node start = pathNodes.get(i);
            Node end = pathNodes.get(i + 1);
            double startX = start.getXcoord();
            double startY = start.getYcoord();
            double endX = end.getXcoord();
            double endY = end.getYcoord();
            int scaledStartX = transforms.transformX(startX);
            int scaledStartY = transforms.transformY(startY);
            int scaledEndX = transforms.transformX(endX);
            int scaledEndY = transforms.transformY(endY);

            Line l = new Line(scaledStartX, scaledStartY, scaledEndX, scaledEndY);
            l.setStrokeWidth(2.0);
            lines.add(l);
        }
        return lines;
    }

    public static void drawLine(Line l,Group g){
        g.getChildren().add(l);
    }
    public static void drawAnimatedLine(Line l,Group g) {

        AnimatedLine animatedLine = new AnimatedLine();
        l = animatedLine.animateLine(l);
        g.getChildren().add(l);
    }


}



