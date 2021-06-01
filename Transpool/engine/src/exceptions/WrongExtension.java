package exceptions;

public class WrongExtension extends Exception {
    final String EXCEPTION_MESSAGE="Wrong file type.";
    @Override
    public String getMessage() {return EXCEPTION_MESSAGE;}
}
