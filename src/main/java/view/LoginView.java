package view;

import Logger.Log;
import Util.Printer;
import Util.SecurityQuestions;
import entities.DBUser;
import exceptions.DBUserAuthenticationException;
import features.controller.LoginController;
import org.junit.platform.engine.support.descriptor.FileSystemSource;
import replication.SFTP;
import session.Session;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class LoginView {

    static Log log = Log.getLogInstance();
    public DBUser performLogin()
    {
        Scanner sc = new Scanner(System.in);
        Printer.printContent("Enter username/email :");
        String username = sc.nextLine();

        Printer.printContent("Enter password :");
        String password = sc.nextLine();

        int questionIndex = SecurityQuestions.getRandomSecurityQuestionIndex();
        String securityQuestion = SecurityQuestions.getQuestionByIndex(questionIndex);
        Printer.printContent(securityQuestion);
        String securityAnswer = sc.nextLine();

        try {
             LoginController loginController = new LoginController();
             Long startTime = System.currentTimeMillis();
             DBUser loggedInUser = loginController.login(username, password, questionIndex,securityAnswer);
             if(loggedInUser != null) {
                 Printer.printContent("Logged in as: " + loggedInUser.getUserName());
                 Long endTime = System.currentTimeMillis();
                 log.addGeneralLog(endTime- startTime,0,0, SFTP.REMOTE_HOST, username,"", "Login successfull");
                 return loggedInUser;
             }
             else
             throw new DBUserAuthenticationException("Invalid username/password");
        }
        catch (DBUserAuthenticationException e) {
            Printer.printContent(e.toString());
            return null;
        }/* catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }*/
    }
}
