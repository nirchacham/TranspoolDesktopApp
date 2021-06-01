package exceptions;

public class NameException extends Exception{
    final String EXCEPTION_MESSAGE="This field cannot contain a number/special character.";
    @Override
    public String getMessage() {return EXCEPTION_MESSAGE;}
}
