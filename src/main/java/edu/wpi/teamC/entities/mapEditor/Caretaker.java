package edu.wpi.teamC.entities.mapEditor;

import edu.wpi.teamC.entities.Memento;

import java.util.LinkedList;

public class Caretaker {
    LinkedList<Memento> mementoList = new LinkedList<>();
    Memento memento1 = new Memento("/edu/wpi/teamC/fxml/StartUp.fxml");

    public Memento goHome() {
        return memento1;
    }

    public void push(Memento memento) {
        mementoList.add(memento);
    }

    public Memento pop() {
        if(mementoList.isEmpty()) {
            return memento1;
        } else
            return mementoList.pop();
    }
}
