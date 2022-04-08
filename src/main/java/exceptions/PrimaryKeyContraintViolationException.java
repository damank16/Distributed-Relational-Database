package exceptions;

public class PrimaryKeyContraintViolationException extends RuntimeException {
    private String message;
    public PrimaryKeyContraintViolationException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
