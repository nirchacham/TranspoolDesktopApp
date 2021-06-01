package exceptions;

public class SingleStationCourse extends Exception{
    final String EXCEPTION_MESSAGE="The course must contain at least 2 stations.";
    @Override
    public String getMessage() {return EXCEPTION_MESSAGE;}
}
