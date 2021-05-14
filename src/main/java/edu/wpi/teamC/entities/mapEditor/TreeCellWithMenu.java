package edu.wpi.teamC.entities.mapEditor;

import edu.wpi.teamC.views.mapEditor.MapDisplay;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.input.MouseEvent;

public class TreeCellWithMenu extends TextFieldTreeCell<String> {

    ContextMenu men;
    MapDisplay mapDisplay;
    public TreeCellWithMenu(MapDisplay m) {
        //ContextMenu with one entry
        men = new ContextMenu();
        mapDisplay=m;
    }
    public TreeCellWithMenu(MapDisplay m, ContextMenu menu) {
        //ContextMenu with one entry
        men = menu;
        mapDisplay=m;
    }

    @Override
    public void updateItem(String t, boolean bln) {
        //Call the super class so everything works as before
        super.updateItem(t, bln);
        //Check to show the context menu for this TreeItem
        if (showMenu(t, bln)) {

            //setContextMenu(men);
            setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    try {
                        mapDisplay.legendClicked(t,event.getScreenX(),event.getScreenY());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }else{
            //If no menu for this TreeItem is used, deactivate the menu
            setContextMenu(null);
        }
    }

    //Deccide if a menu should be shown or not
    protected boolean showMenu(String t, boolean bln){
        if (t != null && !t.equals("Map")&& !(t.length()==4 && t.toUpperCase().equals(t))) {
            return true;
        }
        return false;
    }

}
