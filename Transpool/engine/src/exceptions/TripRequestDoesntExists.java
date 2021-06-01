package exceptions;

public class TripRequestDoesntExists extends Exception {
    private final String EXCEPTION_MESSAGE;

    public TripRequestDoesntExists(int request) {
        EXCEPTION_MESSAGE="The trip request #" + request + " does not exists.\n";
    }
    @Override
    public String getMessage() {return EXCEPTION_MESSAGE;}
}
