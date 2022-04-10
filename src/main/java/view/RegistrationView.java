package view;


import Logger.Log;
import Util.Constants;
import Util.Printer;
import Util.SecurityQuestions;
import entities.DBUser;
import features.controller.RegistrationController;
import replication.SFTP;


import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class RegistrationView {

    static Log log = Log.getLogInstance();
    public void registerUser() {
        long startTime = System.currentTimeMillis();
        DBUser user = createDBUserObject();
        RegistrationController controller = new RegistrationController();

        if(controller.registerUser(user))
        {
            long endTime = System.currentTimeMillis();
            log.addGeneralLog(endTime- startTime,0,0, SFTP.REMOTE_HOST,user.getUserName(),"", "Registration successfull");
            Printer.printContent(String.format(Constants.USER_REGISTRATION_SUCCESS_MSG,user.getUserName()));
            return;
        }
        Printer.printContent(String.format(Constants.USER_REGISTRATION_ERROR_MSG,user.getUserName()));
    }

    public DBUser createDBUserObject()
    {
        Printer.printContent("Enter Username: ");
        Scanner sc = new Scanner(System.in);
        String userName = sc.nextLine();

        Printer.printContent("Enter Password (Should be between 8-20 chars, must contain letter, number and special character) : ");
        String password = sc.nextLine();

        Map<Integer, String> questionsMap = SecurityQuestions.questionsMap;
        Map<Integer, String> securityQuestionAnswers = new HashMap<>();
        for (Map.Entry<Integer, String> entry : questionsMap.entrySet()) {
            Printer.printContent(entry.getValue());
            securityQuestionAnswers.put(entry.getKey(), sc.nextLine());
        }
        DBUser user = new DBUser(userName,password,securityQuestionAnswers);
        return user;
    }
}
