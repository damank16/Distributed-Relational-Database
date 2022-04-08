package view;

import Util.Printer;
import features.controller.SQLDumpExportController;
import queryprocessor.QueryParser;
import session.Session;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class DBOperationsOptions {

    private  Session userSession;
    public static boolean isDistributed;
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
                case "1": {
                    isDistributed = false;
                    QueryParser queryParser = new QueryParser();
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

                    String query = ""; ;
                    do {
                        try {
                            Printer.printContent("Enter input or exit:");

                            query = br.readLine();
                            if (!query.equalsIgnoreCase("exit")) {
                                queryParser.parseQuery(query);
                                Printer.printContent("completed...");
                            }
                        }
                        catch (Exception e){
                            Printer.printContent(e.getMessage());
                        }
                    }
                    while (!query.equalsIgnoreCase("exit"));
                    break;
                }
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
