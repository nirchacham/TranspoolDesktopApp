package classes;

import java.util.LinkedList;
import java.util.Map;


public class Matcher {

   public Matcher(TranspoolSystem system){
        data=system;
    }
    private TranspoolSystem data;

    public LinkedList<LinkedList<MatchedRide>> makeAMatch(TripRequest request, int numberOfTripsToOffer) {  //By departure or arrival
        if (request.isDepartureTime())
            return collectMatchingTripsByDepartureTime(request,numberOfTripsToOffer);
        else
            return collectMatchingTripsByArrivalTime(request,numberOfTripsToOffer);

    }

    private LinkedList<LinkedList<MatchedRide>> collectMatchingTripsByDepartureTime(TripRequest request, int numberOfTripsToOffer) {

        int index;
        LinkedList<MatchedRide> combinedTrip=new LinkedList<>();
        LinkedList<LinkedList<MatchedRide>> res=new LinkedList<>();

        for(Trip trip : data.getTrips().getTripList()) {
            int size=trip.getCourse().getCourse().size()-1;
            for(int i=0;i<size;i++) {
                if (trip.getCourse().getCourse().get(i).equalsIgnoreCase(request.getOrigin()) &&
                        isTripTimeEqual(request.getRequestDayNumber(), request.getTime(), data.getMap().getStations().getStationByName(trip.getCourse().getCourse().get(i)), trip, true)) {
                    combinedTrip.add(new MatchedRide(trip, data.getMap().getStations().getStationByName(trip.getCourse().getCourse().get(i)), data.getMap().getStations().getStationByName(trip.getCourse().getCourse().get(i+1)), request.getRequestDayNumber(), request.getRequestDayNumber()));
                    buildMatchingTripsByDeparture(combinedTrip, request, res, numberOfTripsToOffer);
                    combinedTrip.removeFirst();
                    break;
                }
            }
        }

        return res;
    }

    private void buildMatchingTripsByDeparture(LinkedList<MatchedRide> ride, TripRequest request, LinkedList<LinkedList<MatchedRide>> res, int numberOfTripsToOffer) {

        if(res.size()==numberOfTripsToOffer)
            return;

        Station current=ride.getLast().getRoute().getLast().getSecondStation();
        for(MatchedRide mr:ride){
            for(Pair pair:mr.getRoute()){
                if(pair.getFirstStation().getName().equalsIgnoreCase(current.getName()))
                    return;
            }
        }

        if(request.getDestination().equalsIgnoreCase(current.getName())) {
            res.add(copyLinkedList(ride));
        }
        else{
            LinkedList<MatchedRide> matchingRides=findTripsForNextStations(ride.getLast(),request);
            for(MatchedRide matchedRide : matchingRides){
                LinkedList<MatchedRide> newRide=copyLinkedList(ride);
                if(matchedRide.getTrip().getTripNumber()==ride.getLast().getTrip().getTripNumber()) {////
                    newRide.getLast().setEndStationInRoute(matchedRide);
                    buildMatchingTripsByDeparture(copyLinkedList(newRide),request,res,numberOfTripsToOffer);
                    newRide.getLast().getRoute().removeLast();
                }else{
                newRide.add(new MatchedRide(matchedRide.getTrip(),current,matchedRide.getRoute().getLast().getSecondStation(),matchedRide.getRoute().getLast().getDayInSecondStation() ,matchedRide.getRoute().getLast().getDayInSecondStation()));
                //newRide.add(new MatchedRide(matchedRide.getTrip(),current,matchedRide.getRoute().getLast().getSecondStation(),ride.getLast().getRoute().getLast().getDayInSecondStation() ,findClosestDayFromAbove(ride.getLast().getRoute().getLast(), matchedRide.getTrip())));
                    buildMatchingTripsByDeparture(copyLinkedList(newRide),request,res,numberOfTripsToOffer);
                    newRide.removeLast();
                }
            }
        }
    }

    private LinkedList<MatchedRide> copyLinkedList(LinkedList<MatchedRide> ride) {
         LinkedList newList = new LinkedList<MatchedRide>();
        for(MatchedRide matchedRide : ride) { newList.add(new MatchedRide(matchedRide)); }
        return newList;
    }


    public  LinkedList<MatchedRide> findTripsForNextStations(MatchedRide ride,TripRequest request){

        LinkedList<MatchedRide> matchingRides = new LinkedList<>();
        Station currentStation=ride.getRoute().getLast().getSecondStation();

        for (Trip trip:currentStation.getTrips()) {//all the trips that pass through this station
            for (int i = 0; i < trip.getCourse().getCourse().size() - 1; i++) {
                if (trip.getCourse().getCourse().get(i).equalsIgnoreCase(currentStation.getName()) &&
                        isTripTimeBigger(ride.getRoute().getLast(), trip)) {
                    matchingRides.add(new MatchedRide(trip, ride.getRoute().getLast().getSecondStation(), data.getMap().getStations().getStationByName(trip.getCourse().getCourse().get(i+1)),ride.getRoute().getLast().getDayInSecondStation(), findClosestDayFromAbove(ride.getRoute().getLast(), trip)));
                }
            }
        }
        return matchingRides;
    }

    private boolean isTripTimeEqual(int day,Time requestTime,Station tripStation, Trip trip,boolean isDeparture) {
        int recurrences = trip.getTripSchedule().recurrencesToInteger();
        if(isDeparture) {
            if (trip.getTripDayStart() <= day) {
                if ((trip.getTripDayStart() * 24 * 60) + (trip.getDepartureTime().getHour() * 60) + (trip.getDepartureTime().getMinutes()) < ((day * 60 * 24) + (requestTime.getHour() * 60) + (requestTime.getMinutes())) && trip.getTripSchedule().getSchedule().equals("One Time"))
                    return false;
                if ((trip.getTripDayStart() % recurrences) == day % recurrences &&
                        trip.estimatedArrivalTimeToStation(tripStation.getName()).equals(requestTime) &&
                      (trip.getCapacityPerTime(day,"one",new Pair(tripStation,tripStation,requestTime,requestTime,day,day),true)))
                    return true;
            }

        }
        else{
            if (trip.getTripDayStart() <= day) {
                if ((trip.getTripDayStart() * 24 * 60) + (trip.getArrivalTime().getHour() * 60) + (trip.getArrivalTime().getMinutes()) > ((day * 60 * 24) + (requestTime.getHour() * 60) + (requestTime.getMinutes())) && trip.getTripSchedule().getSchedule().equals("One Time"))
                    return false;
                if ((trip.getTripDayStart() % recurrences) == day % recurrences &&
                        trip.estimatedArrivalTimeToStation(tripStation.getName()).equals(requestTime)&&(trip.getCapacityPerTime(day,"one",new Pair(tripStation,tripStation,requestTime,requestTime,day,day),false)))
                    return true;

            }
        }
        return false;
    }

    private boolean isTripTimeBigger(Pair requestStation, Trip trip) {
        if (requestStation.getDayInSecondStation() > trip.getTripDayStart() && trip.getTripSchedule().getSchedule().equalsIgnoreCase("One Time"))
            return false;
        else if(trip.getTripSchedule().getSchedule().equalsIgnoreCase("One Time") && !trip.getCapacityPerTime(requestStation.getDayInSecondStation(),"One Time",requestStation,true))
            return false;
        else
            return true;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~arrival~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    private LinkedList<LinkedList<MatchedRide>> collectMatchingTripsByArrivalTime(TripRequest request, int numberOfTripsToOffer) {

        LinkedList<MatchedRide> combinedTrip = new LinkedList<>();
        LinkedList<LinkedList<MatchedRide>> res = new LinkedList<>(); // save all the optional result of combinedTrips


        for(Trip trip : data.getTrips().getTripList()) {
            int size=trip.getCourse().getCourse().size()-1;
            for(int i=size;i>0;i--){
              if (trip.getCourse().getCourse().get(i).equalsIgnoreCase(request.getDestination()) &&
                    isTripTimeEqual(request.getRequestDayNumber(), request.getTime(), data.getMap().getStations().getStationByName(trip.getCourse().getCourse().get(i)), trip,false))  {
                    combinedTrip.add(new MatchedRide(trip, data.getMap().getStations().getStationByName(trip.getCourse().getCourse().get(i-1)), data.getMap().getStations().getStationByName(trip.getCourse().getCourse().get(i)),request.getRequestDayNumber(),request.getRequestDayNumber()));
                    buildMatchingTripsByArrival(copyLinkedList(combinedTrip),request,res,numberOfTripsToOffer); //send them to a rec function to find all optional trips
                    combinedTrip.removeFirst();
                    break;
                }
            }
        }
        return res;
    }

    private void buildMatchingTripsByArrival(LinkedList<MatchedRide> ride, TripRequest request, LinkedList<LinkedList<MatchedRide>> res, int numberOfTripsToOffer) {

        if(res.size()==numberOfTripsToOffer)
            return;

        Station current=ride.getFirst().getRoute().getFirst().getFirstStation();
        for(MatchedRide mr:ride){
            for(Pair pair:mr.getRoute()){
                if(pair.getSecondStation().getName().equalsIgnoreCase(current.getName()))
                    return;
            }
        }
        if(request.getOrigin().equalsIgnoreCase(current.getName())) { // if we are in the destination
            res.add(ride);
        }

        else{
            LinkedList<MatchedRide> matchingRides=findTripsForPrevStations(ride.getFirst(),request); //gating list of optional Plaid drive
            for(MatchedRide matchedRide : matchingRides){
                LinkedList<MatchedRide> newRide = copyLinkedList(ride);
                if(matchedRide.getTrip().getTripNumber()==ride.getFirst().getTrip().getTripNumber()) {// if its the same drive just chang the end station
                    newRide.getFirst().setStartStationInRoute(matchedRide);
                    buildMatchingTripsByArrival(copyLinkedList(newRide),request,res,numberOfTripsToOffer);
                    //newRide.getFirst().getRoute().removeFirst();
                    newRide.removeFirst();
                }else{
//                    //newRide.addFirst(new MatchedRide(matchedRide.getTrip(),matchedRide.getRoute().getFirst().getFirstStation(),current,findClosestDayFromBelow(matchedRide.getRoute().getFirst(),matchedRide.getTrip()),ride.getFirst().getRoute().getFirst().getDayInFirstStation()));
                newRide.addFirst(new MatchedRide(matchedRide.getTrip(),matchedRide.getRoute().getFirst().getFirstStation(),current,matchedRide.getRoute().getFirst().getDayInFirstStation(),matchedRide.getRoute().getFirst().getDayInFirstStation()));
                buildMatchingTripsByArrival(copyLinkedList(newRide),request,res,numberOfTripsToOffer);
                    newRide.removeFirst();
                }
            }
        }
    }

    private LinkedList<MatchedRide> findTripsForPrevStations(MatchedRide ride,TripRequest request) {

        LinkedList<MatchedRide> matchingRides = new LinkedList<>();
        Station current = ride.getRoute().getFirst().getFirstStation();
        for (Trip trip:current.getTrips()) {//all the trips that pass through this station
            int size=trip.getCourse().getCourse().size()-1;
            for (int index = size ; index > 0 ; index--) {  // 0<i because if it is the lest station it cant continue withe driver
                if (trip.getCourse().getCourse().get(index).equalsIgnoreCase(current.getName()) &&
                        isTripTimeSmaller(ride.getRoute().getFirst(),trip,request))
                    matchingRides.add(new MatchedRide(trip ,data.getMap().getStations().getStationByName(trip.getCourse().getCourse().get(index-1)),current,findClosestDayFromBelow(ride.getRoute().getFirst(),trip),findClosestDayFromBelow(ride.getRoute().getFirst(),trip)));
                //matchingRides.add(new MatchedRide(trip ,data.getMap().getStations().getStationByName(trip.getCourse().getCourse().get(index-1)),current,findClosestDayFromBelow(ride.getRoute().getFirst(),trip),ride.getRoute().getFirst().getDayInFirstStation()));
            }

        }
        return matchingRides;
    }

    private boolean isTripTimeSmaller(Pair requestStation, Trip trip ,TripRequest request) {
//        if(trip.getTripDayStart()>request.getRequestDayNumber())
//            return false;
        int day=0;
        boolean isOk=false;
        if(trip.getTripDayStart()>requestStation.getDayInFirstStation())
            return false;
        else if(trip.getTripSchedule().getSchedule().equalsIgnoreCase("One Time") && trip.getCapacityPerTime(requestStation.getDayInFirstStation(),"One Time",requestStation,false))
            return false;

        day=findClosestDayFromBelow(requestStation,trip);
        if(day==0)
            return false;
        if(day>requestStation.getDayInFirstStation())
            return false;


         return true;

//        }
//        return true;
//
//
//        int newDay=station.getDay();
//        while(((current.getDay()*24*60) + (current.getHour()*60) + (current.getMinutes())) >= ((newDay*24*60) + (station.getHour()*60) + (station.getMinutes()))){
//            if((!trip.getCapacityPerTime().containsKey("" + newDay + station.getHour() + station.getMinutes())) ||
//                    (trip.getCapacityPerTime().containsKey("" + newDay + station.getHour() + station.getMinutes()) && trip.getCapacityPerTime().get("" + newDay + station.getHour() + station.getMinutes()) > 0))
//                return true;
//            else if(trip.getSchedule().getRecurrences().equals("One Time"))
//                    return false;
//            else
//                newDay += trip.getSchedule().recurrencesToInteger();
//        }
//        return false;
    }

    private int findClosestDayFromAbove(Pair currentStation,Trip tripToContinueWith) {
        boolean found=false,fixDay=false;
        int tripDayIndex=tripToContinueWith.getTripDayStart();
        int dayNumber=currentStation.getDayInSecondStation();
//        if(dayNumber<tripToContinueWith.getTripDayStart())//////////////
//            return 0;
        if(dayNumber<=tripToContinueWith.getTripDayStart())
            dayNumber=tripToContinueWith.getTripDayStart();
        else{
            while (!fixDay){
                tripDayIndex+=tripToContinueWith.getTripSchedule().recurrencesToInteger();
                if(tripDayIndex>=dayNumber){
                    dayNumber=tripDayIndex;
                    fixDay=true;
                }
            }
        }

        while(!found){
           found= tripToContinueWith.getCapacityPerTime(dayNumber,tripToContinueWith.getTripSchedule().getSchedule(),currentStation,true);
            if(!found)
                dayNumber=currentStation.getDayInSecondStation() +tripToContinueWith.getTripSchedule().recurrencesToInteger();
        }
        return dayNumber;

    }

    private int findClosestDayFromBelow(Pair currentStation,Trip tripToContinueWith) {
        boolean found=false,fixDay=false;
        int tripDayIndex=tripToContinueWith.getTripDayStart();
        int dayNumber=currentStation.getDayInFirstStation();
        if(dayNumber<tripToContinueWith.getTripDayStart())//////////////
            return 0;

        while(tripDayIndex<dayNumber){
            tripDayIndex+=tripToContinueWith.getTripSchedule().recurrencesToInteger();
        }
        if(tripDayIndex>dayNumber)
            tripDayIndex-=tripToContinueWith.getTripSchedule().recurrencesToInteger();

        while(!found){
            if(tripDayIndex==currentStation.getDayInFirstStation()) {
                Time time = tripToContinueWith.estimatedArrivalTimeToStation(currentStation.getFirstStation().getName());
                if (time.getHour() > currentStation.getTimeInFirstStation().getHour()) {
                    tripDayIndex-= tripToContinueWith.getTripSchedule().recurrencesToInteger();
                    continue;
                }
                else if (time.getHour() == currentStation.getTimeInFirstStation().getHour()) {
                    if (time.getMinutes() > currentStation.getTimeInFirstStation().getMinutes()) {
                        tripDayIndex-= tripToContinueWith.getTripSchedule().recurrencesToInteger();
                        continue;
                    }
                }
            }
            found= tripToContinueWith.getCapacityPerTime(tripDayIndex,tripToContinueWith.getTripSchedule().getSchedule(),currentStation,false);
            if(!found)
                tripDayIndex-= tripToContinueWith.getTripSchedule().recurrencesToInteger();
        }
        return tripDayIndex;



//
//        int newDay=station.getDay();
//        int time = (newDay*24*60) + (station.getHour()*60) + (station.getMinutes());
//
//        if(trip.getSchedule().getRecurrences().equals("One Time"))
//            return 0;
//
//        while (time <= ((day*24*60) + (hour*60) + (minutes))) {
//            newDay += recurrences;
//            time = newDay*24*60 + station.getHour()*60 + station.getMinutes();
//        }
//        while(trip.getCapacityPerTime().containsKey("" + newDay + station.getHour() + station.getMinutes()) && trip.getCapacityPerTime().get("" + newDay + station.getHour() + station.getMinutes()) == 0)
//            newDay -= recurrences;
//
//        if (time > station.getDay()*24*60 + station.getHour()*60 + station.getMinutes())
//            return (newDay - recurrences) - station.getDay();
//        else
//            return 0;
  }

}