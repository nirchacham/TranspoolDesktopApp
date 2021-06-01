package exceptions;

public class MapBoundariesInvalid extends Exception {
    final String EXCEPTION_MESSAGE="The map boundaries are invalid.";
    @Override
    public String getMessage() {return EXCEPTION_MESSAGE;}
}
