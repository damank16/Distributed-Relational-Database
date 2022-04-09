package Logger;

import Util.Constants;
import Util.FileUtility;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Log {
    FileUtility fileUtility = new FileUtility();
    public static final String log = "log";
    private static Log logInstance = null;

    private Log() {
        File directory = new File(Constants.BASE_PATH_DIRECTORY + log);
        if(!directory.exists()) {
            directory.mkdir();
        }
    }

    public static Log getLogInstance() {
        if(logInstance == null) {
            logInstance = new Log();
        }
        return logInstance;

    }

    private String getTimeStamp() {
        LocalDate date = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String logTime = LocalTime.now().format(dateTimeFormatter);
        return date + " - " + logTime;
    }

    private String createMessage(String... message) {
        return String.join("|", message);
    }

    public void addGeneralLog(long executionTime, int numberOfTables, int numberOfRecords, String virtualMachine, String user, String dbName, String message) {
        String record = createMessage(getTimeStamp(), Long.toString(executionTime), user, dbName, Integer.toString(numberOfTables), Integer.toString(numberOfRecords),
                virtualMachine, message);
        fileUtility.writeDataToFile("general", record);
    }

    public void addEventLog(String databaseName, String virtualMachine, String user, String info) {
        String message = createMessage(getTimeStamp(), user, databaseName, virtualMachine, info);
        fileUtility.writeDataToFile("events", message);
    }

    public void addQueryLog(String databaseName, String query, String queryType, String user, String virtualMachine, String tableName) {
        String message = createMessage(getTimeStamp(), user, databaseName,tableName, queryType, query, virtualMachine);
        fileUtility.writeDataToFile("query", message);
    }
}
