package features.controller;

import Logger.Log;
import Util.Constants;
import exceptions.SQLDumpGenratorException;
import replication.SFTP;
import session.Session;

import java.io.*;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class SQLDumpExportController {

    static Log log = Log.getLogInstance();
    public boolean generateDump(String databaseName) throws SQLDumpGenratorException {
        StringBuilder databasePath = new StringBuilder(Constants.DATABASE_BASE_PATH);
        if (isDatabaseExists(databaseName, databasePath)) {
            File databaseDirectory = new File(databasePath.toString());
            File[] files = databaseDirectory.listFiles();
            List<File> tables = new ArrayList<>();
            for(File file : files) {
                if(file.getName().contains("metadata")) {
                   continue;
                }
                tables.add(file);
            }
            createSQLDumpFile(tables, databaseName);
            return true;
        }
        return false;
    }

    public boolean isDatabaseExists(String databaseName, StringBuilder databasePath) throws SQLDumpGenratorException {
        if (!isDatabaseNameValid(databaseName)) {
            String message = "Error: Failed to export SQL Dump for database " + databaseName + ". Invalid database name!";
            //TODO Loggig the error
            throw new SQLDumpGenratorException(message);
        }
        databasePath = databasePath.append(databaseName);
        if (!Files.exists(Paths.get(databasePath.toString()))) {
            String message = "Error: Failed to export SQL Dump for database " + databaseName + ". Database does not exist!";
            throw new SQLDumpGenratorException(message);
        }
        return true;
    }

    public boolean isDatabaseNameValid(String databaseName) {
        if (databaseName == null || databaseName.isEmpty()) {
            return false;
        }
        return Pattern.matches("[A-Za-z\\d]+", databaseName);
    }

    public static void createSQLDumpFile(List<File> tables, String databaseName) throws SQLDumpGenratorException {
        long startTime = System.currentTimeMillis();
        File sqlDumpDirectory = new File(Constants.SQL_DUMP_PATH_DIRECTORY);
        if(!sqlDumpDirectory.exists()) {
            sqlDumpDirectory.mkdir();
        }
        Date date = new Date() ;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ddHH-mm-ss") ;
        String outputSQLDumpFile = Constants.SQL_DUMP_PATH_DIRECTORY + "/"+ databaseName + "-" + dateFormat.format(date) + ".sql";
        try {
            FileWriter sqlFileWriter = new FileWriter(outputSQLDumpFile, true);
            String createDatabaseQuery = getCreateDatabaseQuery(databaseName );
            sqlFileWriter.append(createDatabaseQuery);
            sqlFileWriter.append("\n");
            int noOfTables = tables.size();
            for (File table : tables) {
                String tableName = table.getName().split("\\.")[0];
                String createTableQuery = getCreateTableQuery(tableName,databaseName );
                sqlFileWriter.append(createTableQuery);
                sqlFileWriter.append("\n");
                //read the table file from second line ignore header
                File tableFile = new File(table.getAbsolutePath());
                BufferedReader bufferedReader = new BufferedReader(new FileReader(tableFile));
                String tableRow ;
                while ((tableRow = bufferedReader.readLine()) != null) {
                    String insertIntoTableQuery = getInsertIntoTableQuery(tableRow, tableName);
                    sqlFileWriter.append(insertIntoTableQuery);
                    sqlFileWriter.append("\n");
                }
            }
            sqlFileWriter.append("Commit;");
            sqlFileWriter.close();
            long endTime = System.currentTimeMillis();
            long executionTime = (endTime-startTime) ;
            String message = "Success: SQL Dump generated for database";
            log.addGeneralLog(executionTime,noOfTables,0, SFTP.REMOTE_HOST, Session.getInstance().getLoggedInDBUser().getUserName(),databaseName,message);
        } catch (IOException e) {
            String message = "Error: { " + e.getMessage() + " }!";
            throw new SQLDumpGenratorException(e.getMessage());
        }

    }

    private static String getCreateDatabaseQuery(String databaseName) {
        StringBuilder createStringBuilder = new StringBuilder();
        createStringBuilder.append("CREATE")
                .append(" ").append("DATABASE")
                .append(" ").append(databaseName)
                .append(" ").append(";");
        return createStringBuilder.toString();
    }

    private static String getInsertIntoTableQuery(String tableRow, String tableName) {
        StringBuilder insertStringBuilder = new StringBuilder();
        insertStringBuilder.append("INSERT").append(" ").append("INTO").append(" ").append(tableName).append(" ").append("(");
        String[]  columnData = tableRow.split("\\|");
        for(String columnValue : columnData) {
            insertStringBuilder.append(columnValue).append(Constants.COMMA_DELIMITER);
        }
        insertStringBuilder.deleteCharAt(insertStringBuilder.length() - 1).append(");");
        return insertStringBuilder.toString();
    }

    private static String getCreateTableQuery(String tableName, String databaseName) throws IOException {

        List<String> foreignConstraints = new ArrayList<>();
        StringBuilder createStringBuilder = new StringBuilder();
        createStringBuilder.append("CREATE")
                .append(" ").append("TABLE")
                .append(" ").append(tableName)
                .append(" ").append("(");
        File metaDataDirectory = new File(Constants.DATABASE_BASE_PATH +databaseName+ "/metadata");
        File[] allMetaDataFiles = metaDataDirectory.listFiles();
        for (File metaDataFile : allMetaDataFiles) {
            if (!metaDataFile.getName().contains(tableName)) {
                continue;
            }
            BufferedReader bufferedReader = new BufferedReader(new FileReader(metaDataFile));
            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null) {
                String[] columnNameAndDataTypeAndConstraint = currentLine.split("\\|");
                String columnName = columnNameAndDataTypeAndConstraint[0];
                String dataType = columnNameAndDataTypeAndConstraint[1];
                String dataTypeSize = columnNameAndDataTypeAndConstraint[2];
                createStringBuilder.append(columnName).append(" ").append(dataType).append("(").append(dataTypeSize).append(")");
                if(columnNameAndDataTypeAndConstraint.length > 3) {
                    String primaryConstraint = columnNameAndDataTypeAndConstraint[3];
                    if (primaryConstraint.equalsIgnoreCase("PK")) {
                        createStringBuilder.append(" ").append("PRIMARY KEY");
                    }
                    if (columnNameAndDataTypeAndConstraint.length > 4) {
                        String foreignConstraint = "FOREIGN KEY (" + columnName + ") REFERENCES " + columnNameAndDataTypeAndConstraint[5] + "(" + columnNameAndDataTypeAndConstraint[6] + ")";
                        foreignConstraints.add(foreignConstraint);
                    }
                }
                createStringBuilder.append(Constants.COMMA_DELIMITER);
            }
        }
        for(String foreignConstraint : foreignConstraints) {
            createStringBuilder.append(foreignConstraint).append(Constants.COMMA_DELIMITER);
        }
        createStringBuilder.deleteCharAt(createStringBuilder.length() - 1).append(");");
        return createStringBuilder.toString();
    }

}
