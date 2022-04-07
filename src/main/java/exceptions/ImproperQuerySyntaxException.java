package exceptions;


public class ImproperQuerySyntaxException extends RuntimeException{
    private String message;
    public ImproperQuerySyntaxException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
