package edu.wpi.teamC.entities.mapEditor;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.shape.Line;

import java.util.LinkedList;

public class Path {
    private LinkedList<Node> path;//contains a list of nodes that will be traveled through
    private LinkedList<Double> costToNext;            //the total cost to travel these nodes
    private LinkedList<Double> angleList;
    private LinkedList<Double> absoluteAngle;
    private final Integer slightTurn=20;
    private final Integer normalTurn=70;
    private final Integer sharpTurn=100;
    public Path(){
        this.path = new LinkedList<>();
        costToNext=new LinkedList<>();
        angleList=new LinkedList<>();
        absoluteAngle=new LinkedList<>();
    }

    @Override
    public String toString() {
        return "path=" + path+" \n cost= "+getTotalCost() ;
    }

    public void addNode(Node node){

        path.add(node);
    }
    public void removeNode(Node node){

        path.remove(node);
    }
    public void generateAngleList(){
        angleList.add(0.0);



        double oldTheta=0.0;
        double newTheta=0.0;
        for(int i=1;i<path.size()-1;i++){
            double oldX=path.get(i-1).getXcoord();
            double oldY=path.get(i-1).getYcoord();
            double newX=path.get(i+1).getXcoord();
            double newY=path.get(i+1).getYcoord();
            double x=path.get(i).getXcoord();
            double y=path.get(i).getYcoord();
            oldTheta = 180.0 / Math.PI * Math.atan2(y-oldY,x - oldX);
            newTheta = 180.0 / Math.PI * Math.atan2(newY-y,newX - x);
            if(i==1){
                absoluteAngle.add(oldTheta+90);
            }
            absoluteAngle.add(newTheta+90);
            double angle=newTheta-oldTheta;
            if(angle>180)
                angle=angle-360;
            else if(angle<-180)
                angle=angle+360;

            System.out.println(angle);
            angleList.add(angle);
        }
        angleList.add(0.0);
        absoluteAngle.add(newTheta+90);

    }
    public LinkedList<TextDirection> generateTextDirections(){
        LinkedList<TextDirection> directions=new LinkedList<>();
        double distanceToNextEvent=0;
        Node prevNode=path.get(0);
        directions.add(new TextDirection("Start at "+path.getFirst().getLongName(),path.getFirst(),absoluteAngle.getFirst()));
        for(int i=0;i<angleList.size();i++){
            boolean eventHappened=false;
            double angle=angleList.get(i);
            Node currentNode=path.get(i);
            distanceToNextEvent+= prevNode.rawDistanceTo(currentNode);
            boolean isRight=angle>0;
            String turn="";
            if(isRight){
                turn="right";
            }
           else{
                turn="left";
            }
            if(currentNode.getNodeType().equals("ELEV")&&!prevNode.getFloor().equals(currentNode.getFloor())){
                directions.add(new TextDirection("Travel "+ (int) distanceToNextEvent+" ft then board elevator to Floor "+currentNode.getFloor(),currentNode,absoluteAngle.get(i)));
                eventHappened=true;
            }
            else if(currentNode.getNodeType().equals("STAI")&&!prevNode.getFloor().equals(currentNode.getFloor())){
                directions.add(new TextDirection("Travel "+(int)distanceToNextEvent+" ft then take the stairs to Floor "+currentNode.getFloor(),currentNode,absoluteAngle.get(i)));
                eventHappened=true;
            }
            else if(Math.abs(angle)>sharpTurn){
                directions.add(new TextDirection("Travel "+ (int)distanceToNextEvent+" ft then sharply turn "+turn+".",currentNode,absoluteAngle.get(i)));
                eventHappened=true;
            }
            else if(Math.abs(angle)>normalTurn){
                directions.add(new TextDirection("Travel "+(int) distanceToNextEvent+" ft then turn "+turn+".",currentNode,absoluteAngle.get(i)));
                eventHappened=true;
            }
            else if(Math.abs(angle)>slightTurn){
                directions.add(new TextDirection("Travel "+ (int)distanceToNextEvent+" ft then turn slightly "+turn+".",currentNode,absoluteAngle.get(i)));
                eventHappened=true;
            }
            prevNode=currentNode;
            if(eventHappened)
                distanceToNextEvent=0.0;
        }
        directions.add(new TextDirection("Travel "+ (int)distanceToNextEvent+" ft then arrive at "+path.getLast().getLongName(),path.getLast(),absoluteAngle.getLast()));
        return directions;
    }

    public boolean contains(Node node){
        return path.contains(node);
    }
    public Path reverse(){
        Path p1=new Path();
        for(int i=0;i<path.size();i++){
            p1.addNode(path.get(path.size()-i-1));
        }
        return p1;
    }
    public LinkedList<Line> drawPath(){
        LinkedList<Line> pathLines=new LinkedList<>();
        for (int i=0;i<path.size()-1;i++){
            Node start=path.get(i);
            Node end=path.get(i+1);
            Line pathLine=new Line(start.getXcoord(),start.getYcoord(),end.getXcoord(),end.getYcoord());
            pathLines.add(pathLine);
        }
        return pathLines;
    }

    public LinkedList<Node> getPath() {
        return path;
    }
    public double getTotalCost(){
        double accumulator=0.0;
        for(Double cost:costToNext){
            accumulator+=cost;
        }
        return accumulator;
    }
    public LinkedList<Double> getCostToNext(){
        return costToNext;
    }

    public void addCostToNext(double cost){
        costToNext.add(cost);
    }
    public Point2D getCentre(){
        Transforms transform=Transforms.get_Instance();
        Rectangle2D bounds=pathMinMax();
        System.out.println(bounds);
        int minX= transform.transformX(bounds.getMinX());
        int minY=transform.transformY(bounds.getMinY());
        int maxX= transform.transformX(bounds.getMaxX());
        int maxY= transform.transformY(bounds.getMaxY());

        return new Point2D((maxX+minX)/2,(25+minY+maxY)/2);
    }
    public double getZoom(){
        Rectangle2D bounds=pathMinMax();
        return Math.min(5,Math.min(5000/bounds.getWidth(), (3400*.5)/bounds.getHeight()));

    }
    public Rectangle2D pathMinMax(){
        int minX=10000;
        int minY=10000;
        int maxX=0;
        int maxY=0;
        for(Node n:getPath()){
            if(n.getXcoord()<minX)
                minX=n.getXcoord();
            if(n.getXcoord()>maxX)
                maxX=n.getXcoord();
            if(n.getYcoord()<minY)
                minY=n.getYcoord();
            if(n.getYcoord()>maxY)
                maxY=n.getYcoord();
        }
        return new Rectangle2D(minX,minY,maxX-minX,maxY-minY);
    }
}

