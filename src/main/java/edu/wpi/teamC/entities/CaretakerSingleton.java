package edu.wpi.teamC.entities;


import edu.wpi.teamC.entities.mapEditor.Caretaker;

public class CaretakerSingleton {
    public static CaretakerSingleton single_instance = null;
    Caretaker caretaker;
    Long prevTime;
    Integer timeoutSeconds;

    public CaretakerSingleton() {
        this.caretaker = new Caretaker();
        this.prevTime = System.currentTimeMillis();
        timeoutSeconds = 300000;
    }

    public Integer getTimeoutSeconds(){
        return timeoutSeconds;
    }

    public Memento pop() {
        return caretaker.pop();
    }

    public void push(Memento memento){
        caretaker.push(memento);
    }

    public static CaretakerSingleton getInstance()
    {
        if (single_instance == null)
            single_instance = new CaretakerSingleton();

        return single_instance;
    }

    // set prev time
    public void setPrevTime(){
        this.prevTime = System.currentTimeMillis();
    }

    public Memento goHome(){
        return caretaker.goHome();
    }

    // get prev time
    public Long getPrevTime(){
        return prevTime;
    }
}
