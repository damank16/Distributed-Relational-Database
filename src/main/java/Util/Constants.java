package Util;

import exceptions.DatabaseAlreadyExistingException;

public class Constants {
    public static final String BASE_PATH_DIRECTORY = "./data/";
    public static final String DATABASE_BASE_PATH = BASE_PATH_DIRECTORY + "database/";
    public static final String SQL_DUMP_PATH_DIRECTORY = BASE_PATH_DIRECTORY+ "./dumps";
    public static final String USER_REGISTRATION_SUCCESS_MSG = "User %s is registered successfully";
    public static final String USER_REGISTRATION_ERROR_MSG = "User %s registration failed" ;
    public static final String USER_SAVE_ERROR = "Some error occurred while saving user details in DB. Please try again.";
    public static final String INVALID_USERNAME_ERROR = "Invalid username entered!";
    public static final String INVALID_PASSWORD_ERROR = "Invalid password entered!";
    public static final String INVALID_SECURITY_ANSWER_ERROR = "Invalid security answer entered!";
    public static final String USER_ALREADY_EXISTS_ERROR ="User with same username already exists!";
    public static final String USERS_DETAILS_FILE = "users.txt" ;
    public static final String COMMA_DELIMITER = ",";
    public static final String CREATE = "create";
    public static final String SELECT = "select";
    public static final String INSERT = "insert";
    public static final String DELETE = "delete";

    public static final String DROP = "drop";
    public static final String USE = "use";
    public static final String UPDATE = "update";
}
