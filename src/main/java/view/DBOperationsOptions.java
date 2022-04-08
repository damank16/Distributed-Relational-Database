package view;

import Util.Printer;
import features.controller.SQLDumpExportController;
import session.Session;

import java.util.Scanner;

public class DBOperationsOptions {

    private  Session userSession;
    public DBOperationsOptions(Session userSession) {
        this.userSession = userSession;
    }
    public void displayMainMenu() {

        Printer.printTitle("DB Operations Menu");
        while (true) {
            Printer.printContent("1. Write Queries");
            Printer.printContent("2. Export");
            Printer.printContent("3. Data Model");
            Printer.printContent("4. Analytics");
            Printer.printContent("5. Logout");
            Printer.printContent("Select an option:");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    break;
                case "2":
                /*    Printer.printContent("Enter database name to generate SQL dump:");
                     String databaseName = scanner.nextLine().trim();
                     SQLDumpExportController sqlDumpExportController = new SQLDumpExportController();
                    try {
                        sqlDumpExportController.generateDump(databaseName);
                    } catch (SQLDumpGenratorException e) {
                        Printer.printContent(e.toString());
                    }*/
                    break;
                case "3":
                    break;
                case "4":
                    break;
                case "5":
                    userSession.destroyDBUserSession();
                    return;
                default:
                    break;
            }
        }
    }

}
