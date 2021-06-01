package exceptions;

public class PathAlreadyExists extends Exception {
    private final String EXCEPTION_MESSAGE;

    public PathAlreadyExists(String from,String to) {
        EXCEPTION_MESSAGE="The path from " + from + " to " + to + " is not defined";
    }
    @Override
    public String getMessage() {return EXCEPTION_MESSAGE;}
}
