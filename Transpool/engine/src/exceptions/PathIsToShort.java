package exceptions;

public class PathIsToShort extends Exception {
    final String EXCEPTION_MESSAGE="The xml path is too short.\nPlease enter a valid path.\n";
    @Override
    public String getMessage() {return EXCEPTION_MESSAGE;}
}

