package edu.wpi.teamC.entities.mapEditor;

public abstract class AbsSearch {

    abstract MultiFloorPath search(Node start, Node goal,boolean isEmergency);

    public final MultiFloorPath Search(Node start, Node goal,boolean isEmergency) {
        return search(start, goal,isEmergency);
    }
}
