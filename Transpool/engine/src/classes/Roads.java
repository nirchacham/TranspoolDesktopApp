package classes;

import java.util.List;

public class Roads {
    private List<Road> roadList;

    public Roads(List<Road> roadList) {
        this.roadList = roadList;
    }

    public Road checkIfRoadExists(String from,String to){
        for (Road road:roadList){
            if (road.getOrigin().equals(from) && road.getDestination().equals(to))
                return road;
            if (road.getDestination().equals(from) && road.getOrigin().equals(to) && road.isOneWay()==false)
                return road;
        }
        return null;
    }



    public List<Road> getRoadList() {
        return roadList;
    }

    public void setRoadList(List<Road> roadList) {
        this.roadList = roadList;
    }
}
