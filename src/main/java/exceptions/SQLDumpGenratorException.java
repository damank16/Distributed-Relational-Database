package exceptions;

public class SQLDumpGenratorException extends Exception {

    private String errorMessage;

    public SQLDumpGenratorException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return "SQLDumpGenratorException {" + "errorMessage='" + errorMessage + '\'' + '}';
    }
}

