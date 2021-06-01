package classes;

public class Map {
    private int length;
    private int width;
    private Roads roads;
    private Stations stations;

    public Map(int length, int width) {
        this.length = length;
        this.width = width;
    }
    public Map() { }

    public void setLength(int length) {
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Roads getRoads() {
        return roads;
    }

    public void setRoads(Roads roads) {
        this.roads = roads;
    }

    public Stations getStations() {
        return stations;
    }

    public void setStations(Stations stations) {
        this.stations = stations;
    }

}
