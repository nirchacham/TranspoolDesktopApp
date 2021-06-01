package exceptions;

public class StopBoundaries extends Exception {
    private final String EXCEPTION_MESSAGE;

    public StopBoundaries(String name,int length,int width) {
        EXCEPTION_MESSAGE=name + "has to be between " + length + " and " + width;
    }
    @Override
    public String getMessage() {return EXCEPTION_MESSAGE;}
}
