import Util.Printer;
import entities.DBUser;
import session.Session;
import view.DBOperationsOptions;
import view.LoginView;
import view.RegistrationView;

import java.util.Scanner;

public class DistributedDatabaseMain {

    public static void main(String[] args)
    {
        DistributedDatabaseMain distributedDatabase = new DistributedDatabaseMain();
        distributedDatabase.displayUserOptions();
    }

    private void displayUserOptions()
    {
         Printer.printTitle("Welcome to dgp11 Distributed Database");
         Scanner scanner = new Scanner(System.in);
         Session dbUserSession = Session.getInstance();
         while (true) {
             Printer.printContent("1. User Registration.");
             Printer.printContent("2. User Login.");
             Printer.printContent("3. Exit.");
             Printer.printContent("Select an option:");
            final String input = scanner.nextLine();

            switch (input) {
                case "1":
                    RegistrationView registrationView = new RegistrationView();
                    registrationView.registerUser();
                    break;
                case "2":
                    LoginView userLoginView = new LoginView();
                    DBUser user =  userLoginView.performLogin();
                    if (user != null) {
                        dbUserSession.createDBUserSession(user);
                        DBOperationsOptions dbOperations = new DBOperationsOptions(dbUserSession);
                        dbOperations.displayMainMenu();
                    }
                    break;
                case "3":
                    dbUserSession.destroyDBUserSession();
                    System.exit(0);
                default:
                    break;
            }
        }
    }

    // 
}
