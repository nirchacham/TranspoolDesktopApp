package classes;

import java.util.List;

public class Stations {
    private List<Station> stationList;

    public Stations(List<Station> stationList) {
        this.stationList = stationList;
    }

    public List<Station> getStationList() {
        return stationList;
    }

    public void setStationList(List<Station> stationList) {
        this.stationList = stationList;
    }

    public void printStations() {
        System.out.println("These are all the stations existing in the system:");
        for(Station station:stationList) {
            System.out.println(station.getName());
        }
    }

    public boolean checkStationExistence(String name) {
        for(Station station:stationList) {
            if(station.getName().equalsIgnoreCase(name))
                return true;
        }
        return false;
    }

    public Station getStationByName(String name) {
        for(Station station:stationList) {
            if(station.getName().equalsIgnoreCase(name))
                return station;
        }
        return null;
    }

}
