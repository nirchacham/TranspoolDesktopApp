package exceptions;

public class TripDoesntExists extends Exception {
    private final String EXCEPTION_MESSAGE;

    public TripDoesntExists(int trip) {
        EXCEPTION_MESSAGE="Trip #" + trip + " does not exists.\n";
    }
    @Override
    public String getMessage() {return EXCEPTION_MESSAGE;}
}
