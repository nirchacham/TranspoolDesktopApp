package exceptions;

public class OutOfRange extends  Exception {
    final String EXCEPTION_MESSAGE="The time that was entered is not in range.\n";
    @Override
    public String getMessage() {return EXCEPTION_MESSAGE;}
}
