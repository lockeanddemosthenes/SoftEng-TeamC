package edu.wpi.teamC.entities.mapEditor;

import edu.wpi.teamC.views.mapEditor.MapDisplay;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.input.MouseEvent;

public class TreeCellWithAdminMenu extends TextFieldTreeCell<String> {
    TreeCellWithMenu cell;


    Boolean editMode;
    public void setEditMode(Boolean editMode) {
        this.editMode = editMode;
    }


    public TreeCellWithAdminMenu(MapDisplay m) {
        MenuItem editNode = new MenuItem("Edit Node...");

        MenuItem delNode = new MenuItem("Delete Node");

        MenuItem delEdges = new MenuItem("Delete Edges...");

        MenuItem addEdges =new MenuItem("Add Edge to...");



        ContextMenu menu = new ContextMenu(editNode,delNode, delEdges,addEdges);
        cell = new TreeCellWithMenu(m, menu);
    }

    @Override
    public void updateItem(String t, boolean bln) {
        super.updateItem(t, bln);
        if (cell.showMenu(t, bln)) {
            setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.isControlDown()) {
                        cell.mapDisplay.legendSelection(t);

                    } else {
                        try {
                            cell.mapDisplay.legendClicked(t, event.getScreenX(), event.getScreenY());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
            });


        } else {
            //If no menu for this TreeItem is used, deactivate the menu
            setContextMenu(null);
            setOnMousePressed(null);
        }


    }

}
