package edu.wpi.teamC.entities.mapEditor;

import edu.wpi.teamC.views.mapEditor.MapDisplay;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;

import java.util.HashMap;

public class RenderObjectsAdmin extends RenderObjects{
    @Override
    public Button drawMainButtons(Node node, int buttonSize, Group buttonGroup, MapDisplay m,boolean editMode,boolean addEdgeMode) {



        Button button = new Button();
        button.setId(node.getID());

        if(!editMode) {
            button.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if(event.isPrimaryButtonDown()) {
                        m.buttonClicked(button, event.getScreenX(), event.getScreenY());
                    }

                }
            });
        }
        else{
            MenuItem deleteNode=new MenuItem("Delete Node");
            deleteNode.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    m.deleteNode(node.getShortName());
                }
            });



            MenuItem addEdgeTo=new MenuItem("Add edge to...");
            addEdgeTo.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    m.addEdges(node.getShortName());
                }
            });
            ContextMenu editor=new ContextMenu(deleteNode,addEdgeTo);
            button.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                @Override
                public void handle(ContextMenuEvent event) {

                    System.out.println("Context menu of button requested");
                    editor.show(button,event.getScreenX(),event.getScreenY());
                }
            });
            if(addEdgeMode){
                button.setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        m.addEdgeTo(button.getId());
                    }
                });
            }
            else {
                final double[] origX = {0};
                final double[] origY = {0};
                button.setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        System.out.println("Mouse pressed");
                        if (event.isPrimaryButtonDown()) {
                            m.editNode(node.getShortName());
                            origX[0] =(event.getScreenX() - m.zoomPane.getLayoutX()) / m.zoomPane.getCurrentScale() + (m.zoomPane.targetPointAtViewportCentre().getX() - ((m.zoomPane.getWidth()) / (2.0 * m.zoomPane.getCurrentScale())))-button.getLayoutX()-buttonSize/2;
                            origY[0] =(event.getScreenY() - m.zoomPane.getLayoutY()) / m.zoomPane.getCurrentScale() + (m.zoomPane.targetPointAtViewportCentre().getY() - ((m.zoomPane.getHeight() )/ (2.0 * m.zoomPane.getCurrentScale())))-button.getLayoutY()-buttonSize/2;

                        }
                        if(event.isControlDown()){
                            m.addToSelection(node.getID());
                            button.setStyle("-fx-background-color: "+m.getColorMap(node.getNodeType()));
                        }
                    }
                });

                button.setOnMouseDragged(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {

                        double mapX = (event.getScreenX() - m.zoomPane.getLayoutX()) / m.zoomPane.getCurrentScale() + (m.zoomPane.targetPointAtViewportCentre().getX() - ((m.zoomPane.getWidth()) / (2.0 * m.zoomPane.getCurrentScale())))-origX[0];
                        double mapY = (event.getScreenY() - m.zoomPane.getLayoutY()) / m.zoomPane.getCurrentScale() + (m.zoomPane.targetPointAtViewportCentre().getY() - ((m.zoomPane.getHeight() )/ (2.0 * m.zoomPane.getCurrentScale())))-origY[0];
                        System.out.println("node being dragged!");
                        m.dragNode(button, mapX, mapY);

                    }
                });
            }
        }


        button.setPrefSize(buttonSize, buttonSize);

        button.setStyle("-fx-border-color: " + m.getColorMap(node.getNodeType()) + ";-fx-border-width: 1px;-fx-background-color: Transparent;");


        button.setMinSize(1, 1);
        button.setLayoutX(node.getXcoord() - buttonSize / 2);
        button.setLayoutY(node.getYcoord() - buttonSize / 2);

        buttonGroup.getChildren().add(button);
        return button;
    }
}
