package exceptions;

public class NotUniqueName extends Exception{
    private final String EXCEPTION_MESSAGE;
    public NotUniqueName(String name) {
        EXCEPTION_MESSAGE="There are more than one stop with the name " + name;
    }
    @Override
    public String getMessage() {return EXCEPTION_MESSAGE;}
}
