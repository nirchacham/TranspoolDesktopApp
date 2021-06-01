package exceptions;

public class PathNotFound extends Exception {
    final String EXCEPTION_MESSAGE="File path not found.";
    @Override
    public String getMessage() {return EXCEPTION_MESSAGE;}
}
