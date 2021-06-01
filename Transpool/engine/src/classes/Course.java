package classes;

import java.util.ArrayList;

public class Course  {
    private ArrayList<String> course;
    private java.util.Map hm;

    public Course(ArrayList<String> course, java.util.Map hm) {
        this.course = course;
        this.hm = hm;
    }

    public double calculateCourseLength(String from, String to){
        double sum=0;
        boolean isOk=false;
        Road road = new Road();
        for(int i=0;i<course.size()-1;i++){
           road= (Road)hm.get(course.get(i)+","+course.get(i+1));
           if(road==null)
               road= (Road)hm.get(course.get(i+1)+","+course.get(i));
           if(road.getOrigin().equalsIgnoreCase(from) || (road.getDestination().equalsIgnoreCase(from)&&!road.isOneWay()))
               isOk=true;
           if(isOk)
               sum+=road.getRoadLength();
           if(road.getDestination().equalsIgnoreCase(to)||(road.getOrigin().equalsIgnoreCase(to)&&!road.isOneWay())) {
               isOk = false;
               break;
           }
        }
        return sum;
    }

    public double calculateFuel(String from, String to){
        double sum=0;
        int count=0;
        boolean isOk=false;
        Road road = new Road();
        for(int i=0;i<course.size()-1;i++){
            road= (Road)hm.get(course.get(i)+","+course.get(i+1));
            if(road==null)
                road= (Road)hm.get(course.get(i+1)+","+course.get(i));
            if(road.getOrigin().equalsIgnoreCase(from) || (road.getDestination().equalsIgnoreCase(from)&&!road.isOneWay()))
                isOk=true;
            if(isOk) {
                sum += road.getGasConsumption();
                count++;
            }
            if(road.getDestination().equalsIgnoreCase(to)||(road.getOrigin().equalsIgnoreCase(to)&&!road.isOneWay()))  {
                isOk = false;
                break;
            }
        }
        return sum;
    }

    public Course(ArrayList<String> course) {
        this.course = course;
    }

    public ArrayList<String> getCourse() {
        return course;
    }

    public void setCourse(ArrayList<String> course) {
        this.course = course;
    }
}
