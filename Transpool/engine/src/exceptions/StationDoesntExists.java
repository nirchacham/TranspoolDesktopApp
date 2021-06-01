package exceptions;

public class StationDoesntExists extends Exception {
    private final String EXCEPTION_MESSAGE;

    public StationDoesntExists(String station) {
        EXCEPTION_MESSAGE="The station " + station + " does not exists.\n";
    }
    @Override
    public String getMessage() {return EXCEPTION_MESSAGE;}
}
