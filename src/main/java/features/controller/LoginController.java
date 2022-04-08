package features.controller;

import Util.Constants;
import entities.DBUser;
import exceptions.DBUserAuthenticationException;
import org.apache.commons.codec.digest.DigestUtils;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoginController {

    public DBUser login(String username, String password, int securityQuestionIndex, String securityAnswer) throws DBUserAuthenticationException
    {
        try
        {
           if(isEmptyOrNull(username) || isEmptyOrNull(password) || isEmptyOrNull(securityAnswer))
           {
               throw new DBUserAuthenticationException("Invalid Credentials Entered!");
           }

            BufferedReader usersFileReader = new BufferedReader(new FileReader(Constants.BASE_PATH_DIRECTORY+ Constants.USERS_DETAILS_FILE));
            String userDetails;
            while ((userDetails = usersFileReader.readLine()) != null) {
                String[] detailsArray = userDetails.split(Constants.COMMA_DELIMITER);
                boolean userExists = validateUser(username, password, securityAnswer, securityQuestionIndex, detailsArray);
                if (userExists) {
                    DBUser user  = createDBUserObject(detailsArray);
                    return user;
                }
            }
           return  null;
        } catch (IOException e)
        {
            throw new DBUserAuthenticationException("Something went wrong. Please try again after sometime!");
        }
    }

    private boolean validateUser(String user, String password, String securityAnswer, int securityAnswerIndex, String[] detailsArray) {
        boolean isSameUsername = detailsArray[1].equals(user);
        boolean isSamePassword = detailsArray[2].equals(DigestUtils.sha256Hex(password));
        boolean isSameSecurityAnswer = detailsArray[securityAnswerIndex + 2].equals(securityAnswer);
        return (isSameUsername && isSamePassword && isSameSecurityAnswer);
    }

    public boolean isEmptyOrNull(String s)
    {
        return (s == null || s.isEmpty());
    }

    private DBUser createDBUserObject(String[] detailsArray) {
        return new DBUser( Integer.parseInt(detailsArray[0]),
                detailsArray[1],
                detailsArray[2],
                null);
    }
}

