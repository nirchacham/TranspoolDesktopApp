package classes;

import sun.awt.image.ImageWatched;

import java.util.LinkedList;

public class MatchedRide {

   private Trip trip;
   LinkedList<Station> route;
   LinkedList<Pair> routee;

    public MatchedRide(Trip trip, Station startStation, Station endStation,int firstDay,int secondDay) {
        routee = new LinkedList<>();

        routee.add(new Pair(startStation,endStation, trip.estimatedArrivalTimeToStation(startStation.getName()),trip.estimatedArrivalTimeToStation(endStation.getName()),firstDay,secondDay));
        //routee.add(new Pair(endStation, trip.estimatedArrivalTimeToStation(endStation.getName()),day));
        this.trip = trip;
    }

//    public MatchedRide(MatchedRide matchedRide, int day) {
//        this.trip=matchedRide.trip;
//        setRoute(matchedRide.getRoute(), day);
//    }
//
    public MatchedRide(MatchedRide matchedRide) {
        trip=matchedRide.getTrip();
        setRoute(matchedRide.getRoute(),0);
    }
//
    private void setRoute(LinkedList<Pair> route, int day) {
        this.routee = new LinkedList<>();
        for (Pair station : route)
            this.routee.add(new Pair(station.getFirstStation(),station.getSecondStation(),station.getTimeInFirstStation(),station.getTimeInSecondStation(),station.getDayInFirstStation(),station.getDayInSecondStation()));
    }
//
//    public void setTrip(Trip trip) {
//        this.trip =new Trip(trip,trip.getSchedule());
//    }
//
    public Trip getTrip() {
        return trip;
    }

    public void setEndStationInRoute(MatchedRide matchedRide){
        routee.getLast().setDayInSecondStation(matchedRide.getRoute().getLast().getDayInSecondStation());
        routee.getLast().setTimeInSecondStation(matchedRide.getRoute().getLast().getTimeInSecondStation());
        routee.getLast().setSecondStation(matchedRide.getRoute().getLast().getSecondStation());
    }

    public void setStartStationInRoute(MatchedRide matchedRide){
        routee.getFirst().setDayInFirstStation(matchedRide.getRoute().getFirst().getDayInFirstStation());
        routee.getFirst().setTimeInFirstStation(matchedRide.getRoute().getFirst().getTimeInFirstStation());
        routee.getFirst().setFirstStation(matchedRide.getRoute().getFirst().getFirstStation());
    }

    public LinkedList<Pair> getRoute() {
        return routee;
    }

    public static String showMatchedRideInfo(LinkedList<MatchedRide> matchedRides){
        String string="";
        double sumPrice=0, sumFuel=0, length, totalFuel=0, currLength=0, currFuel=0;
        for(int i=0; i<matchedRides.size(); i++) {
            if(matchedRides.size()==1) {
                return Trip.showTripInfo(matchedRides.get(i).getTrip());
            }
            string+="Trip number: " + matchedRides.get(i).getTrip().getTripNumber() + " day #" + matchedRides.get(i).routee.getFirst().getDayInFirstStation() +
                    "\nPickup from station: " +matchedRides.get(i).routee.getFirst().getFirstStation().getName() +" , at " + matchedRides.get(i).trip.estimatedArrivalTimeToStation(matchedRides.get(i).routee.getFirst().getFirstStation().getName()) +
                    "\nDrop off in station: " +matchedRides.get(i).routee.getLast().getSecondStation().getName() + " , at " + matchedRides.get(i).trip.estimatedArrivalTimeToStation(matchedRides.get(i).routee.getLast().getSecondStation().getName());
            if(!matchedRides.get(i).equals(matchedRides.getLast()))
                    string += "\nWait for your next trip.\n";
            length=matchedRides.get(i).getTrip().getCourse().calculateCourseLength(matchedRides.get(i).routee.getFirst().getFirstStation().getName(),matchedRides.get(i).routee.getLast().getSecondStation().getName());
            sumPrice += length * matchedRides.get(i).getTrip().getPPK();
            sumFuel = matchedRides.get(i).getTrip().getCourse().calculateFuel(matchedRides.get(i).routee.getFirst().getFirstStation().getName(),matchedRides.get(i).routee.getLast().getSecondStation().getName());
            //totalFuel=calculateTotalFuel(length, sumFuel,currLength, currFuel);
            currLength+=length;
            currFuel+=sumFuel;
        }

        string+= "\nTotal trip price: " + String.valueOf(sumPrice);
        string+= "\nAverage fuel consumption: " + String.valueOf(currFuel/currLength);

        return string;

    }

    public static double calculateTotalFuel(double length, double sumFuel, double currLength, double currFuel) {
        return ((currLength*currLength)+(length*sumFuel))/(length+currLength);
    }


}
