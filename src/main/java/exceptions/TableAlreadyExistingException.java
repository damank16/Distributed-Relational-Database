package exceptions;


public class TableAlreadyExistingException extends RuntimeException{

    private String message;
    public TableAlreadyExistingException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
