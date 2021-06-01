package classes;

public class Pair {

    private Time timeInFirstStation;
    private Time timeInSecondStation;
    private int dayInFirstStation;
    private int dayInSecondStation;
    private Station firstStation;
    private Station secondStation;

    public Pair( Station firstStationName,Station secondStationName,Time firstStationTime,Time secondStationTime,int dayInFirst,int dayInSecond ) {
        this.timeInFirstStation = firstStationTime;
        this.timeInSecondStation = secondStationTime;
        this.dayInFirstStation = dayInFirst;
        this.dayInSecondStation=dayInSecond;
        this.firstStation = firstStationName;
        this.secondStation=secondStationName;
    }

    public Time getTimeInFirstStation() {
        return timeInFirstStation;
    }

    public Time getTimeInSecondStation() {
        return timeInSecondStation;
    }

//    public void setTimeInStation(Time timeInStation) {
//        this.timeInStation = timeInStation;
//    }

    public int getDayInFirstStation() {
        return dayInFirstStation;
    }

    public int getDayInSecondStation() {
        return dayInSecondStation;
    }

//    public void setDayInStation(int dayInStation) {
//        this.dayInStation = dayInStation;
//    }

    public Station getFirstStation() {
        return firstStation;
    }

    public Station getSecondStation() {
        return secondStation;
    }

    public void setTimeInFirstStation(Time timeInFirstStation) {
        this.timeInFirstStation = timeInFirstStation;
    }

    public void setTimeInSecondStation(Time timeInSecondStation) {
        this.timeInSecondStation = timeInSecondStation;
    }

    public void setDayInFirstStation(int dayInFirstStation) {
        this.dayInFirstStation = dayInFirstStation;
    }

    public void setDayInSecondStation(int dayInSecondStation) {
        this.dayInSecondStation = dayInSecondStation;
    }

    public void setFirstStation(Station firstStation) {
        this.firstStation = firstStation;
    }

    public void setSecondStation(Station secondStation) {
        this.secondStation = secondStation;
    }

//    public void setStationName(Station stationName) {
//        this.station = stationName;
//    }




}
