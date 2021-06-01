package exceptions;

public class HasNumber extends Exception {
    final String EXCEPTION_MESSAGE="Input is invalid. It can not contain a number.";
    @Override
    public String getMessage() {return EXCEPTION_MESSAGE;}
}
