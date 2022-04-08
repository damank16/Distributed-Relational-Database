package Util;

import java.util.Map;
import java.util.regex.Pattern;

public class UserValidationUtils {

    public static boolean isUserNameInValid(String username)
    {
        if (username == null || username.isEmpty()) {
            return true;
        }
        return  !Pattern.matches("[A-Za-z\\d]+", username);
    }

    public static boolean isPasswordInValid(String password)
    {
        if (password == null || password.isEmpty()) {
           return true;
        }
        return !Pattern.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$",
                    password);
    }

    public static boolean isSecurityAnswersInValid(Map<Integer,String > securityQuestionsAndAnswers)
    {
        for(String answer  : securityQuestionsAndAnswers.values())
        {
            if (answer == null || answer.isEmpty()) {
                return true;
            }
            if(!Pattern.matches("[A-Za-z\\d]+", answer))
            return true;
        }
        return false;
    }


}
