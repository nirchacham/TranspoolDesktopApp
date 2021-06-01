package exceptions;

public class LettersException extends Exception {
    final String EXCEPTION_MESSAGE="Input is invalid. It can not contain a letter/special character.";
    @Override
    public String getMessage() {return EXCEPTION_MESSAGE;}
}
