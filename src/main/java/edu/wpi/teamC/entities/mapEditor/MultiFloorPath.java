package edu.wpi.teamC.entities.mapEditor;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

public class MultiFloorPath {
            //floor 3: paths on floor 3
    private HashMap<String, LinkedList<Path>> paths;



    private Path originalPath;

    public MultiFloorPath(Path p) {
        paths = pathToMultiFloor(p);
        originalPath=p;
        originalPath.generateAngleList();
    }
    public MultiFloorPath(){
        paths=new HashMap<>();
    }


    private HashMap<String, LinkedList<Path>> pathToMultiFloor(Path p) {
        if(p.getPath().size()==0){
            return new HashMap<>();
        }
        HashMap<String, LinkedList<Path>> paths=new HashMap<>();
        String currentFloor = p.getPath().get(0).getFloor();


        LinkedList<Path> totalFloor = new LinkedList<>();

        int i = 0;
        Node n = p.getPath().get(i);
        Path floorPath = new Path();
        Node previousNode=n;
        while (i < p.getPath().size()) {

            while (n.getFloor().equals(currentFloor)) {
                n = p.getPath().get(i);
                floorPath.addNode(n);
                floorPath.addCostToNext(n.distanceTo(previousNode));
                i++;

                previousNode=n;

                if (i >= p.getPath().size()) {
                    break;
                }
                n = p.getPath().get(i);

            }




            totalFloor.add(floorPath);
            paths.put(currentFloor,totalFloor);
            floorPath=new Path();
            floorPath.addNode(n);
            currentFloor=n.getFloor();
            totalFloor=paths.get(currentFloor);
            if(totalFloor==null)
                totalFloor=new LinkedList<>();
        }

    return paths;
    }
    public Set<String> getActiveFloors(){
        return paths.keySet();
    }

    public HashMap<String, LinkedList<Path>> getPaths() {
        return paths;
    }

    public void setPaths(HashMap<String, LinkedList<Path>> paths) {
        this.paths = paths;
    }
    public double getCosts(){
        double accumulator=0;
        for (LinkedList<Path> pathList: paths.values()){
            for(Path p:pathList){
                accumulator+=p.getTotalCost();
            }
        }
        return accumulator;
    }
    public Path getOriginalPath() {
        return originalPath;
    }
}
