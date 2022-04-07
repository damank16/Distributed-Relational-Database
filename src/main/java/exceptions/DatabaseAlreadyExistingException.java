package exceptions;

public class DatabaseAlreadyExistingException extends RuntimeException {
    private String message;
    public DatabaseAlreadyExistingException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
