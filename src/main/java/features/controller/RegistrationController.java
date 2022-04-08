package features.controller;


import Util.Constants;
import Util.Printer;
import Util.UserValidationUtils;
import entities.DBUser;
import exceptions.DBUserAuthenticationException;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class RegistrationController {

    public boolean registerUser (DBUser user)
    {
        try {
            return validateUser(user) && !doesUserAlreadyExist(user) && saveUser(user);
        }
        catch (DBUserAuthenticationException exception)
        {
            Printer.printContent(exception.toString());
            return false;
        }
    }

    private boolean saveUser(DBUser user) throws  DBUserAuthenticationException {

        try (final FileWriter fileWriter = new FileWriter(Constants.BASE_PATH_DIRECTORY+ Constants.USERS_DETAILS_FILE, true)) {
             int userId = (int) (Files.lines(Paths.get(Constants.BASE_PATH_DIRECTORY+Constants.USERS_DETAILS_FILE)).count() + 1);
             String newUser = getNewUser(userId, user);
            fileWriter.append(newUser);
            return true;
        } catch (IOException e ) {
            e.printStackTrace();
            throw new DBUserAuthenticationException(Constants.USER_SAVE_ERROR);
        }
    }

    private String getNewUser(int userId, DBUser user)  {
        return userId + Constants.COMMA_DELIMITER+
                user.getUserName() + Constants.COMMA_DELIMITER +
                DigestUtils.sha256Hex(user.getPassword()) +Constants.COMMA_DELIMITER+
                String.join(Constants.COMMA_DELIMITER, user.getSecurityQuesAns().values()) + "\n";
    }

    private boolean doesUserAlreadyExist(DBUser user) throws DBUserAuthenticationException {
        try
        {
            File userFile = new File(Constants.BASE_PATH_DIRECTORY+Constants.USERS_DETAILS_FILE);
            if(!userFile.exists()){
                return false;
            }
            BufferedReader usersFileReader = new BufferedReader(new FileReader(Constants.BASE_PATH_DIRECTORY+Constants.USERS_DETAILS_FILE));
            String userDetails;
            while ((userDetails = usersFileReader.readLine()) != null) {
                String[] userDetailsArr = userDetails.split(Constants.COMMA_DELIMITER);
                if(userDetailsArr[1].equals(user.getUserName()))
                    throw new DBUserAuthenticationException(Constants.USER_ALREADY_EXISTS_ERROR);
            }
            return false;
        } catch (IOException e) {
            throw new DBUserAuthenticationException(Constants.USER_SAVE_ERROR);
        }
    }

    private boolean validateUser(DBUser user) throws DBUserAuthenticationException
    {
        if (UserValidationUtils.isUserNameInValid(user.getUserName())) {
            throw new DBUserAuthenticationException(Constants.INVALID_USERNAME_ERROR);
        }
        if (UserValidationUtils.isPasswordInValid(user.getPassword()))
        {
            throw new DBUserAuthenticationException(Constants.INVALID_PASSWORD_ERROR);
        }
        if (UserValidationUtils.isSecurityAnswersInValid(user.getSecurityQuesAns()))
        {
            throw new DBUserAuthenticationException(Constants.INVALID_SECURITY_ANSWER_ERROR);
        }
        return true;
    }
}
