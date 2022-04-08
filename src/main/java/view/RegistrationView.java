package view;


import Util.Constants;
import Util.Printer;
import Util.SecurityQuestions;
import entities.DBUser;
import features.controller.RegistrationController;


import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class RegistrationView {

    public void registerUser() {
        DBUser user = createDBUserObject();
        RegistrationController controller = new RegistrationController();
        if(controller.registerUser(user))
        {
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

        Printer.printContent("Enter Password: ");
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
