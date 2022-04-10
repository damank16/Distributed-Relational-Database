package view;

import Analysis.AnalysisMenuDriven;
import DataModelling.ReverseEngineering;
import Logger.Log;
import Util.Printer;
import exceptions.SQLDumpGenratorException;
import features.controller.SQLDumpExportController;
import queryprocessor.QueryParser;
import replication.SFTP;
import session.Session;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Scanner;

import static Util.Constants.BASE_PATH_DIRECTORY;

public class DBOperationsOptions {

    public static boolean isDistributed;
    private final Session userSession;
    private final Log log = Log.getLogInstance();

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

                    String query = "";
                    do {
                        try {
                            Printer.printContent("Enter input or exit:");

                            query = br.readLine();
                            if (!query.equalsIgnoreCase("exit")) {
                                queryParser.parseQuery(query);
                                Printer.printContent("completed...");
                            }
                        } catch (Exception e) {

                            Printer.printContent(e.getMessage());
                        }
                    }
                    while (!query.equalsIgnoreCase("exit"));
                    break;
                }
                case "2":
                    Printer.printContent("Enter database name to generate SQL dump:");
                    String databaseName = scanner.nextLine().trim();
                    SQLDumpExportController sqlDumpExportController = new SQLDumpExportController();
                    try {
                        sqlDumpExportController.generateDump(databaseName);
                    } catch (SQLDumpGenratorException e) {

                        Printer.printContent(e.toString());
                    }
                    break;
                case "3":
                    Printer.printContent("Please enter database name");
                    File directory = new File(BASE_PATH_DIRECTORY + "/database");
                    File[] fileList = directory.listFiles();
                    int count = 1;
                    for (File file : fileList) {
                        Printer.printContent(count + ". " + file.getName());
                        count++;
                    }
                    int dbName = scanner.nextInt();
                    ReverseEngineering re = new ReverseEngineering();
                    re.readTables(fileList[dbName - 1].getName());
                    Printer.printContent("ERD generated in data/ERD.txt file");
                    break;
                case "4":
                    AnalysisMenuDriven analysisMenuDriven = new AnalysisMenuDriven();
                    try {
                        analysisMenuDriven.analytics_input();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "5":
                    log.addGeneralLog(0,0,0, SFTP.REMOTE_HOST,userSession.getLoggedInDBUser().getUserName(),"", "Logout successfull");
                    userSession.destroyDBUserSession();
                    return;
                default:
                    break;
            }
        }
    }

}
