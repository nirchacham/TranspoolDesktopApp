package exceptions;

public class NotUniqueLocation extends Exception {
    private final String EXCEPTION_MESSAGE;

    public NotUniqueLocation(String name1, String name2) {
        EXCEPTION_MESSAGE="The stops: " + name1 + " , " + name2 + " have the same coordinates.";
    }
    @Override
    public String getMessage() {return EXCEPTION_MESSAGE;}
}