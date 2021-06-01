package classes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TripRequest {
    private String passengerName;
    private String origin;
    private String destination;
    private Time time;
    private boolean isAssigned;
    private static int counter =0;
    //private static int dayCounter =0;
    private int TripRequestNumber;
    private Map<Integer,Trip> tripHm;
    private boolean isDepartureTime;
    private int dayNumber;

    public TripRequest(String passengerName, String origin, String destination, Time time,boolean isDepartureTime,int day) {
        this.passengerName = passengerName;
        this.origin = origin;
        this.destination = destination;
        this.time = time;
        this.time.roundHour();
        this.isDepartureTime = isDepartureTime;
        dayNumber=day;
        TripRequestNumber = ++counter;
        tripHm = new HashMap<>();///
    }

    public void updateTripHm(Trip trip){
        tripHm.put(trip.getTripNumber(),trip);
    }

    public Map<Integer,Trip> getTripHm() {
        return tripHm;
    }

    public int getRequestDayNumber() {
        return dayNumber;
    }

    public int getTripRequestNumber() {
        return TripRequestNumber;
    }

    public boolean isAssigned() {
        return isAssigned;
    }

    public void setAssigned(boolean assigned) {
        isAssigned = assigned;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public boolean isDepartureTime() {
        return isDepartureTime;
    }

    static public String showTripRequestInfo(TripRequest tr){
            String string;
            string= "Passenger Name: " + tr.getPassengerName() +
                    "\nOrigin: " + tr.getOrigin() +
                    "\nDestination: " + tr.getDestination();
            if(tr.isDepartureTime()==true) {
                string+="\nDeparture time: " + tr.getTime();
            }
            else{
                string+="\nArrival time: " + tr.getTime();
            }
            string+="\nRequested day: " + tr.getRequestDayNumber();
            if(tr.isAssigned()) {
                string += "\nTrip request is assigned to "; // ((Trip)tr.getTripHm().get(tr.getTripRequestNumber())).getTripNumber();
                for(Map.Entry<Integer,Trip> entry: tr.getTripHm().entrySet()) {
                    string+="\n"+entry.getValue().getTripNumber();
                }
            }
            else
                string+="\nTrip is not assigned yet.";
            return string;
        }

}
