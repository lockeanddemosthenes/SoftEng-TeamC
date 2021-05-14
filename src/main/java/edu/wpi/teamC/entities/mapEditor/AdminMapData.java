package edu.wpi.teamC.entities.mapEditor;

import edu.wpi.teamC.repository.DBAccess;
import edu.wpi.teamC.views.mapEditor.MapDisplay;

import java.util.LinkedList;

public class AdminMapData extends MapData{

    public AdminMapData(MapDisplay m, DBAccess dbAccess,boolean isEmergency) {
        super(m,dbAccess,isEmergency);
        navTypes = new LinkedList<String>();
        navTypes.add("A-Star");
        navTypes.add("Depth-First");
        navTypes.add("Breadth-First");
        navTypes.add("Best-First");
        navTypes.add("Dijkstra");
    }


}

