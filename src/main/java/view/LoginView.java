package view;

import Util.Printer;
import Util.SecurityQuestions;
import entities.DBUser;
import exceptions.DBUserAuthenticationException;
import features.controller.LoginController;


import java.util.Scanner;

public class LoginView {

    public DBUser performLogin()
    {
        Scanner sc = new Scanner(System.in);
        Printer.printContent("Enter username/email :");
        String username = sc.nextLine();

        Printer.printContent("Enter password (Should be between 8-20 chars, must contain letter, number and special character :");
        String password = sc.nextLine();

        int questionIndex = SecurityQuestions.getRandomSecurityQuestionIndex();
        String securityQuestion = SecurityQuestions.getQuestionByIndex(questionIndex);
        Printer.printContent(securityQuestion);
        String securityAnswer = sc.nextLine();

        try {
             LoginController loginController = new LoginController();
             DBUser loggedInUser = loginController.login(username, password, questionIndex,securityAnswer);
             if(loggedInUser != null) {
                 Printer.printContent("Logged in as: " + loggedInUser.getUserName());
                 return loggedInUser;
             }
             else
             throw new DBUserAuthenticationException("Invalid username/password");
        }
        catch (DBUserAuthenticationException e) {
            Printer.printContent(e.toString());
            return null;
        }
    }
}
