package exceptions;

public class NoChoiceException extends Exception {
    private final String EXCEPTION_MESSAGE;
    public NoChoiceException(String string){
        EXCEPTION_MESSAGE="You must choose "+string;
    }
    @Override
    public String getMessage() {return EXCEPTION_MESSAGE;}
}
