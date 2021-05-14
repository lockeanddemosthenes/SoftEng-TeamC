package edu.wpi.teamC.entities.mapEditor;

public class SearchContext {
    ISearch searchAlg;

    public SearchContext(ISearch searchAlg) {
        this.searchAlg = searchAlg;
    }

    public MultiFloorPath search(Node start, Node goal,boolean isEmergency){
        return searchAlg.search(start, goal,isEmergency);
    }
}
