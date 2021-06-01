package exceptions;

public class InvalidRoute extends Exception {
    private final String EXCEPTION_MESSAGE;
    public InvalidRoute(String from, String to) {
        EXCEPTION_MESSAGE="There is no path between " + from + " and " + to;
    }
    @Override
    public String getMessage() {return EXCEPTION_MESSAGE;}
}
