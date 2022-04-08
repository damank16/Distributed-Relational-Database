package exceptions;


public class NoSuchDatabaseObject extends RuntimeException{
    private String message;
    public NoSuchDatabaseObject(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
