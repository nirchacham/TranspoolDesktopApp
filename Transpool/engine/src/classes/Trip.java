package classes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Trip {
    private String driverName;
    private int passengersNumber;
    private double PPK;
    private double averageFuelConsumption;
    private Course course;
    private int tripNumber;
    private static int counter = 100;
    private Time departureTime;
    private Time arrivalTime;
    private java.util.Map hm;/////
    private Set<TripRequest> currPassengers;
    private List<PassengerOnTrip> passengersManager;
    private Schedule tripSchedule;
    private int tripDayStart;
    private Feedbacks feedbacks;


    public Trip(String driverName, int passengersNumber, double PPK, Course course,int departure,java.util.Map hm) {
        this.driverName = driverName;
        this.passengersNumber = passengersNumber;
        this.PPK = PPK;
        this.course = course;
        this.hm=hm;////
        double length=course.calculateCourseLength(course.getCourse().get(0),course.getCourse().get(course.getCourse().size()-1));
        averageFuelConsumption=course.calculateFuel(course.getCourse().get(0),course.getCourse().get(course.getCourse().size()-1))/length;
        departureTime=new Time(departure,0);
        arrivalTime=estimatedArrivalTimeToStation(course.getCourse().get(course.getCourse().size()-1));
        //tripSchedule=schedule;
        tripNumber=++counter;
        currPassengers = new HashSet<>();
        feedbacks = new Feedbacks();
        passengersManager = new ArrayList<>();
    }

    public Trip(String driverName, int passengersNumber, double PPK, Course course,Time departure,
                Schedule schedule,int dayStart,java.util.Map hm) {
        this.driverName = driverName;
        this.passengersNumber = passengersNumber;
        this.PPK = PPK;
        this.course = course;
        this.hm=hm;////
        double length=course.calculateCourseLength(course.getCourse().get(0),course.getCourse().get(course.getCourse().size()-1));
        averageFuelConsumption=course.calculateFuel(course.getCourse().get(0),course.getCourse().get(course.getCourse().size()-1))/length;
        departureTime= departure;
        tripDayStart=dayStart;
        arrivalTime=estimatedArrivalTimeToStation(course.getCourse().get(course.getCourse().size()-1));
        tripSchedule=schedule;

        tripNumber=++counter;
        currPassengers = new HashSet<>();
        feedbacks = new Feedbacks();
        passengersManager = new ArrayList<>();

    }

    public List<PassengerOnTrip> getPassengersManager() {
        return passengersManager;
    }

    public int getTripDayStart() {
        return tripDayStart;
    }

    public void calculateArrivalTime(){
        int sum=0;
        Road road = new Road();
        for(int i=0;i<course.getCourse().size()-1;i++){
            road= (Road)hm.get(course.getCourse().get(i)+","+course.getCourse().get(i+1));
            if(road==null)
                road= (Road)hm.get(course.getCourse().get(i+1)+","+course.getCourse().get(i));
            sum+=(road.getRoadLength()/road.getMaxSpeed())*60;
        }
        this.arrivalTime = new Time(departureTime);
        this.arrivalTime.updateTime(sum/60,sum%60);
        this.arrivalTime.roundHour();
    }

    public boolean findPossibleTrip(TripRequest request){
        int from=-1;
        int to=-1;
        boolean isOk=false;
        ArrayList<String> courseArr = course.getCourse();
        for(int i=0;i<courseArr.size();i++) {
            if (courseArr.get(i).equalsIgnoreCase(request.getOrigin()))
                from = i;
            if (courseArr.get(i).equalsIgnoreCase(request.getDestination())) {
                to = i;
                break;
            }
        }
        if (from<to&&from!=-1&&to!=-1)
            isOk= true;
        if(isOk) {
            if (request.isDepartureTime() == true) {
                return request.getTime().equals(estimatedArrivalTimeToStation(request.getOrigin()));
            }
            else {
                return request.getTime().equals(estimatedArrivalTimeToStation(request.getDestination()));
            }
        }
        return false;
    }

    public void updateCurrentPassengers(TranspoolTime time){
        for(PassengerOnTrip passenger:passengersManager){
            if(passenger.getDayStart()==time.getDayNumber()) {
                if (time.getAppTime().getHour() >= passenger.getPickupTime().getHour() && time.getAppTime().getHour() <= passenger.getDropTime().getHour()) {

                    if(passenger.getPickupTime().getHour()==passenger.getDropTime().getHour()){
                        if(time.getAppTime().getMinutes()>=passenger.getPickupTime().getMinutes() &&time.getAppTime().getMinutes()<=passenger.getDropTime().getMinutes()){
                            if (passenger.isCurrentlyOnTrip() == false) {
                                passengersNumber++;
                                passenger.setCurrentlyOnTrip(true);
                                break;
                            }
                            break;
                        }
                        else
                            passenger.setCurrentlyOnTrip(false);
                    }

                    if (time.getAppTime().getHour() == passenger.getPickupTime().getHour()) {
                        if (time.getAppTime().getMinutes() >= passenger.getPickupTime().getMinutes()) {
                            if (passenger.isCurrentlyOnTrip() == false) {
                                passengersNumber++;
                                passenger.setCurrentlyOnTrip(true);
                                break;
                            }
                        }
                        else
                            passenger.setCurrentlyOnTrip(false);
                        break;
                    }

                    if (time.getAppTime().getHour() == passenger.getDropTime().getHour()) {
                        if (time.getAppTime().getMinutes() <= passenger.getDropTime().getMinutes()) {
                            if (passenger.isCurrentlyOnTrip() == false) {
                                passengersNumber++;
                                passenger.setCurrentlyOnTrip(true);
                                break;
                            }
                        }
                        else
                            passenger.setCurrentlyOnTrip(false);
                        break;
                    }
//                    if (passenger.isCurrentlyOnTrip() == false) {
//                        passengersNumber++;
//                        passenger.setCurrentlyOnTrip(true);
//                        break;
                    }
                else
                    passenger.setCurrentlyOnTrip(false);

            }
                else
                    passenger.setCurrentlyOnTrip(false);
            }

    }

    public void addPassengersManager(PassengerOnTrip passenger){
        passengersManager.add(passenger);
    }

    public void deletePassengersManager(PassengerOnTrip passenger){
                passengersManager.remove(passenger);
    }

    public Time estimatedArrivalTimeToStation(String station){
        int sum=0;
        Road road = new Road();
        for(int i=0;i<course.getCourse().size()-1;i++){
            road= (Road)hm.get(course.getCourse().get(i)+","+course.getCourse().get(i+1));
            if(road==null)
                road= (Road)hm.get(course.getCourse().get(i+1)+","+course.getCourse().get(i));
            if(course.getCourse().get(i).equalsIgnoreCase(station))
                break;
            sum+=(road.getRoadLength()/road.getMaxSpeed())*60;
        }
        Time tempTime= new Time(departureTime);
        tempTime.updateTime(sum/60,sum%60);
        tempTime.roundHour();
        return tempTime;
    }

    public int getTripNumber() {
        return tripNumber;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public int getPassengersNumber() {
        return passengersNumber;
    }

    public double getAverageFuelConsumption() {
        return averageFuelConsumption;
    }

    public void setPassengersNumber(int passengersNumber) {
        this.passengersNumber = passengersNumber;
    }

    public double getPPK() {
        return PPK;
    }

    public void setPPK(double PPK) {
        this.PPK = PPK;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Time getDepartureTime() {
        return departureTime;
    }

    public Time getArrivalTime() {
        return arrivalTime;
    }


    public Set<TripRequest> getCurrPassengers() {
        return currPassengers;
    }

    public void updateCurrPassengers(TripRequest request) {
        currPassengers.add(request);
    }

    public boolean timeMatches(Time appTime) {
        if(departureTime.getHour()<=appTime.getHour()&&arrivalTime.getHour()>=appTime.getHour()) {
            if (departureTime.getHour() == appTime.getHour()) {
                if (departureTime.getMinutes() <= appTime.getMinutes()) {
                    return true;
                }
                return false;
            }
            if (arrivalTime.getHour() == appTime.getHour()) {
                if (arrivalTime.getMinutes() >= appTime.getMinutes()) {
                    return true;
                }
                return false;
            }
            return true;
        }
        return false;
    }
    public boolean dayMatches(int tripRequestDay) {

        if (tripRequestDay < tripDayStart)
            return false;

        if (tripSchedule.getSchedule().equals("One time")) {
            if (tripDayStart == tripRequestDay)
                return true;
            return false;
        } else if (tripSchedule.getSchedule().equals("Daily")) {
            return true;
        } else if (tripSchedule.getSchedule().equals("Alternatly")) {
            if ((tripRequestDay - tripDayStart) % 2 == 0)
                return true;
            return false;
        } else if (tripSchedule.getSchedule().equals("Weekly")) {
            if ((tripRequestDay - tripDayStart) % 7 == 0)
                return true;
            return false;
        } else if (tripSchedule.getSchedule().equals("Monthly")) {
            if ((tripRequestDay - tripDayStart) % 30 == 0)
                return true;
            return false;
        }
        return false;
    }

    static public String showTripInfo(Trip trip){
        String string;
        string= "Driver's name: " + trip.getDriverName() +
                "\nCourse: " + trip.getCourse().getCourse() +
                "\nTotal trip price: " + trip.getCourse().calculateCourseLength(trip.getCourse().getCourse().get(0),trip.getCourse().getCourse().get(trip.getCourse().getCourse().size()-1)) * trip.getPPK() +
                "\nDeparture time: " + trip.getDepartureTime() +
                "\nArrival time: " + trip.getArrivalTime() +
                "\nPassenger's capacity: " + trip.getPassengersNumber() +
                "\nAverage fuel consumption: " + trip.getAverageFuelConsumption()+
                "\nTrip's beginning day: " + trip.getTripDayStart() +
                "\nTrip's scheduling: " + trip.getTripSchedule().getSchedule()+
                "\nPassengers details: ";
        string = showPassengersInfo(string,trip);
        string = showStationsInfo(string,trip);
        string = showDriverFeedback(string,trip);
        return string;

    }

    static public String showPassengersInfo(String string,Trip trip ) {
        if(trip.getCurrPassengers().size() == 0) {
            return string += "\nThere are no passengers yet in this trip.";
        }
        for(TripRequest tr:trip.getCurrPassengers()) {
            string+= "\n#" + tr.getTripRequestNumber() + " " + tr.getPassengerName();
        }
        return string;
    }

    static public String showStationsInfo(String string,Trip trip ) {
        boolean isOk=false;
        if(trip.getCurrPassengers().size() == 0)
            return string;
        string+="\nDropping off/Picking up stations:";
        for(String stations:trip.getCourse().getCourse()) {
            for(PassengerOnTrip passenger:trip.getPassengersManager()) {
                if (stations.equalsIgnoreCase(passenger.getUpStation().getName()) && !isOk) {
                    string+="\nStation " + stations + ":\n" + passenger.getPassenger().getPassengerName() + " is being picked up.";
                    isOk = true;
                } else if (stations.equalsIgnoreCase(passenger.getDownStation().getName()) && !isOk) {
                    string+="\nStation " + stations + ":\n" + passenger.getPassenger().getPassengerName() + " is being dropped off.";
                    isOk = true;
                } else if (stations.equalsIgnoreCase(passenger.getUpStation().getName()) && isOk) {
                    string+=passenger.getPassenger().getPassengerName() + "\nis being picked up.";
                } else if (stations.equalsIgnoreCase(passenger.getDownStation().getName()) && isOk) {
                    string+=passenger.getPassenger().getPassengerName() + "\nis being dropped off.";
                }
            }
            isOk = false;
        }
        return string;
    }

    static public String showDriverFeedback(String string,Trip trip ){
        int i=1;
        string+="\nDriver's Average score is: "+trip.getFeedbacks().getAverageRate()+"\nFeedbacks:";
        if(trip.feedbacks.getFeedbacks().size()==0)
            return string+="\nThere are currently no feedbacks for "+trip.getDriverName();
        for(Feedback feedback :trip.feedbacks.getFeedbacks()){
            string+="\nFeedback #"+i+": "+feedback.getFeedback()+"\n";
            i++;
        }
        return string;
    }

    public Feedbacks getFeedbacks() {
        return feedbacks;
    }

    public Schedule getTripSchedule() {
        return tripSchedule;
    }


    public boolean getCapacityPerTime(int dayInStation,String reccurence,Pair station,boolean isDeparture) {
        int num=0;
        Time time;
        if(isDeparture) {
             time = estimatedArrivalTimeToStation(station.getSecondStation().getName());
        }
        else{
            time = estimatedArrivalTimeToStation(station.getFirstStation().getName());
        }
        for(PassengerOnTrip passenger:passengersManager) {
            if (passenger.getDayStart() == dayInStation && time.isBetween(passenger.getPickupTime(),passenger.getDropTime(),isDeparture))
                num++;
        }
        if(num-passengersNumber<0)//////////
            return true;
        else
            return false;

    }



}
