package exceptions;


public class NoDatabaseSelected extends RuntimeException {
    private String message;
    public NoDatabaseSelected(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
