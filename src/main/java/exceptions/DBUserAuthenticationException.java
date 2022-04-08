package exceptions;

public class DBUserAuthenticationException extends Exception {

    private String errorMessage ;

    public DBUserAuthenticationException(String  errorMessage){
    super(errorMessage);
    this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return "DBUserAuthenticationException{" + "errorMessage='" + errorMessage + '\'' + '}';
    }

}
