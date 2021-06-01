package exceptions;

public class WrongTimeFormat extends Exception {
    final String EXCEPTION_MESSAGE="Input is invalid.\nPlease enter according to the format.";
    @Override
    public String getMessage() {return EXCEPTION_MESSAGE;}
}
