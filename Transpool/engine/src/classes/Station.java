package classes;

import java.util.ArrayList;

public class Station {
    private String name;
    private int x;
    private int y;
    private ArrayList<Trip> trips;

    public Station(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
        trips=new ArrayList<Trip>();
    }

    public Station() { }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }


    public ArrayList<Trip> getTrips() {
        return trips;
    }

    public void setTrips(ArrayList<Trip> trips) {
        this.trips = trips;
    }

    public void addTripToStation(Trip trip) {
        trips.add(trip);
    }
}
