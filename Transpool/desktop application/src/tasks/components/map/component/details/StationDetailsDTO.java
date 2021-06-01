package tasks.components.map.component.details;

import java.util.List;

/*
Dummy container to hold information needed to be shown upon clicking a station
 */
public class StationDetailsDTO {

    private String name;
    private int x;
    private int y;
    private List<String> drives;
    private List<String> people;


    public StationDetailsDTO(List<String> drives, List<String> people) {
        this.drives = drives;
        this.people=people;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<String> getDrives() {
        return drives;
    }

    public List<String> getPeople() {
        return people;
    }
}
