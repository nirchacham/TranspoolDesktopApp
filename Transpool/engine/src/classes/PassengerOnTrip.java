package classes;

public class PassengerOnTrip {

    private boolean isCurrentlyOnTrip;
    private TripRequest passenger;
    private int dayStart;
    private int dayEnd;
    private Station upStation;
    private Station downStation;
    private Time pickupTime;
    private Time dropTime;

    public PassengerOnTrip(TripRequest passenger, int dayStart, int dayEnd, Station upStation, Station downStation, Time pickupTime, Time dropTime) {
        this.passenger = passenger;
        this.dayStart = dayStart;
        this.dayEnd = dayEnd;
        this.upStation = upStation;
        this.downStation = downStation;
        this.pickupTime = pickupTime;
        this.dropTime = dropTime;
        isCurrentlyOnTrip=false;
    }

    public boolean isCurrentlyOnTrip() {
        return isCurrentlyOnTrip;
    }

    public void setCurrentlyOnTrip(boolean currentlyOnTrip) {
        isCurrentlyOnTrip = currentlyOnTrip;
    }


    public TripRequest getPassenger() {
        return passenger;
    }

    public void setPassenger(TripRequest passenger) {
        this.passenger = passenger;
    }

    public int getDayStart() {
        return dayStart;
    }

    public void setDayStart(int dayStart) {
        this.dayStart = dayStart;
    }

    public int getDayEnd() {
        return dayEnd;
    }

    public void setDayEnd(int dayEnd) {
        this.dayEnd = dayEnd;
    }

    public Station getUpStation() {
        return upStation;
    }

    public void setUpStation(Station upStation) {
        this.upStation = upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public void setDownStation(Station downStation) {
        this.downStation = downStation;
    }

    public Time getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(Time pickupTime) {
        this.pickupTime = pickupTime;
    }

    public Time getDropTime() {
        return dropTime;
    }

    public void setDropTime(Time dropTime) {
        this.dropTime = dropTime;
    }

}
