package edu.wpi.teamC.entities.requests;

import edu.wpi.teamC.repository.NodeDB;
import edu.wpi.teamC.views.serviceRequest.ITreeMenuController;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;

public class TreeCellFactory extends TextFieldTreeCell<NodeDB> {
    ITreeMenuController setCell;
    static HashMap<String,String> categoryToEnglish;

    public TreeCellFactory(ITreeMenuController setCell) {
        this.setCell=setCell;
    }
    public TreeCellFactory(){
    initHash();
    }
    private void initHash(){
        categoryToEnglish=new HashMap<>();
        categoryToEnglish.put("HALL","Hallway");
        categoryToEnglish.put("CONF","Conference");
        categoryToEnglish.put("DEPT","Department");
        categoryToEnglish.put("ELEV","Elevator");
        categoryToEnglish.put("EXIT","Exit");
        categoryToEnglish.put("INFO","Information");
        categoryToEnglish.put("LABS","Laboratory");
        categoryToEnglish.put("REST","Restroom");
        categoryToEnglish.put("RETL","Retail");
        categoryToEnglish.put("STAI","Stairs");
        categoryToEnglish.put("SERV","Service");
        categoryToEnglish.put("EMER","Emergency Room");
        categoryToEnglish.put("BATH","Bathing Room");
        categoryToEnglish.put("PARK","Parking lot");
        categoryToEnglish.put("WALK","WalkWay");
    }

    @Override
    public void updateItem(NodeDB n, boolean bln) {
        super.updateItem(n, bln);
        if (showMenu(n, bln)) {
            setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    setCell.setLocation(n.getNodeID());

                }
            });


        } else {
            //If no menu for this TreeItem is used, deactivate the menu
            setContextMenu(null);
            setOnMousePressed(null);
        }


    }
    protected boolean showMenu(NodeDB n, boolean bln){
        if (n!=null&&!categoryToEnglish.values().contains(n.getShortName())) {
            return true;
        }
        return false;
    }
    public void addToLegend(NodeDB legendNode, TreeView<NodeDB> legendTable, String color) {
        boolean categoryFound = false;
        if(!legendNode.getNodeType().equals("HALL")) {
            for (TreeItem<NodeDB> category : legendTable.getRoot().getChildren()) {
                if (category.getValue().getNodeType().equals(legendNode.getNodeType())) {
                    TreeItem<NodeDB> legendElement = new TreeItem<NodeDB>(legendNode);
                    ObservableList<TreeItem<NodeDB>> list = category.getChildren();
                    int i = 0;
                    for (TreeItem<NodeDB> compare : list) {
                        int minimum = Math.min(compare.getValue().getShortName().length(), legendNode.getShortName().length());
                        if (compare.getValue().getShortName().toUpperCase().substring(0, minimum).compareTo(legendNode.getShortName().toUpperCase().substring(0, minimum)) < 1) {
                            i += 1;
                        } else {
                            break;
                        }
                    }

                    category.getChildren().add(i, legendElement);
                    categoryFound = true;
                }

            }
            if (!categoryFound) {
                System.out.println(legendNode.getNodeType());
                System.out.println(categoryToEnglish);
                TreeItem<NodeDB> c = new TreeItem<NodeDB>(new NodeDB("Category", "0", "0", "", "", legendNode.getNodeType(), "", categoryToEnglish.get(legendNode.getNodeType())));
                Rectangle r = new Rectangle(20, 10);
                //c.setGraphic(r);
                r.setStyle("-fx-fill: " + color);
                c.getChildren().add(new TreeItem<NodeDB>(legendNode));
                legendTable.getRoot().getChildren().add(c);


            }
        }
    }

}
