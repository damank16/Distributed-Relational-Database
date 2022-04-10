package queryprocessor;

import Logger.Log;
import Util.Constants;
import Util.Printer;
import entities.Column;
import entities.Datatype;
import entities.Table;
import exceptions.ImproperQuerySyntaxException;
import replication.SFTP;
import session.Session;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class QueryParser {
    private static final String CREATE_TABLE_REGEX = "CREATE\\s*TABLE\\s*";
    private static final String INSERT_TABLE_REGEX = "INSERT\\s* INTO\\s*";
    private static final String DROP_TABLE_REGEX = "DROP\\s*TABLE\\s*";
    private static final String CREATE_DATABASE_REGEX = "CREATE\\s*DATABASE\\s*";
    private static final String DROP_DATABASE_REGEX = "DROP\\s*DATABASE\\s*";
    private static final String USE_DATABASE_REGEX = "USE\\s*DATABASE\\s*";
    private static final String SELECT_TABLE_REGEX = "select\\s+(.*)from\\s+(.*)(where)\\s+(.*)";
    private static final String SIMPLE_SELECT_REGEX = "select\\s+(.*)from\\s+(.*)";
    private static final String DELETE_ROW = "delete\\s+from\\s+.*where\\s+";
    private static final String UPDATE_ROW = "update\\s+.*set\\s+.*where\\s+.*";
    private static final String START_TRANSACTION_REGEX = "^(?i)START TRANSACTION$";
    private static final String START_TRANSACTION = "start transaction";
    private static final String ROLLBACK_REGEX = "^(?i)ROLLBACK$";
    private static final String ROLLBACK = "rollback";
    private static final String COMMIT_REGEX = "^(?i)COMMIT$";
    private static final String COMMIT = "commit";
    public static String database;
    QueryProcessor queryProcessor = new QueryProcessorImpl();
    Log log = Log.getLogInstance();

    static boolean checkFileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    static boolean writeIntoFile(String filePath, String inputQuery) throws IOException {
        File file = new File(filePath);
        FileWriter fileWriter = new FileWriter(file, true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        bufferedWriter.write(inputQuery);
        bufferedWriter.newLine();
        bufferedWriter.close();
        return true;
    }

    public boolean parseQuery(String inputQuery) throws IOException {
        Pattern createTablePattern = Pattern.compile(CREATE_TABLE_REGEX, Pattern.CASE_INSENSITIVE);
        Pattern dropTablePattern = Pattern.compile(DROP_TABLE_REGEX, Pattern.CASE_INSENSITIVE);
        Pattern createDatabasePattern = Pattern.compile(CREATE_DATABASE_REGEX, Pattern.CASE_INSENSITIVE);
        Pattern useDatabasePattern = Pattern.compile(USE_DATABASE_REGEX, Pattern.CASE_INSENSITIVE);
        Pattern dropDatabasePattern = Pattern.compile(DROP_DATABASE_REGEX, Pattern.CASE_INSENSITIVE);
        Pattern selectPattern = Pattern.compile(SELECT_TABLE_REGEX, Pattern.CASE_INSENSITIVE);
        Pattern simpleSelectPattern = Pattern.compile(SIMPLE_SELECT_REGEX, Pattern.CASE_INSENSITIVE);
        Pattern insertTablePattern = Pattern.compile(INSERT_TABLE_REGEX, Pattern.CASE_INSENSITIVE);
        Pattern deleteTablePattern = Pattern.compile(DELETE_ROW, Pattern.CASE_INSENSITIVE);
        Pattern updateTablePattern = Pattern.compile(UPDATE_ROW, Pattern.CASE_INSENSITIVE);
        Pattern startTransactionPattern = Pattern.compile(START_TRANSACTION_REGEX, Pattern.CASE_INSENSITIVE);
        Pattern rollbackTransactionPattern = Pattern.compile(ROLLBACK_REGEX, Pattern.CASE_INSENSITIVE);
        Pattern commitTransactionPattern = Pattern.compile(COMMIT_REGEX, Pattern.CASE_INSENSITIVE);

        // change to lowercase because string.contains() is case sensitive
        inputQuery = inputQuery.toLowerCase(Locale.ROOT);

        String transactionFilePath = Constants.BASE_PATH_DIRECTORY + "transaction.txt";

        try{
        if (createDatabasePattern.matcher(inputQuery).find()) {
            if (checkFileExists(transactionFilePath)) {
                return writeIntoFile(transactionFilePath, inputQuery);
            } else {
                String[] query = inputQuery.split(" ");
                if (query.length > 3) {
                    throw new IllegalArgumentException("Query syntax is improper");
                }

                return queryProcessor.createDatabase(inputQuery, query[2]);
            }
        } else if (dropDatabasePattern.matcher(inputQuery).find()) {
            if (checkFileExists(transactionFilePath)) {
                return writeIntoFile(transactionFilePath, inputQuery);
            } else {
                String[] query = inputQuery.split(" ");
                if (query.length > 3) {
                    throw new IllegalArgumentException("Query syntax is improper");
                }
                return queryProcessor.dropDatabase(inputQuery, query[2]);
            }
        } else if (useDatabasePattern.matcher(inputQuery).find()) {
            if (checkFileExists(transactionFilePath)) {
                return writeIntoFile(transactionFilePath, inputQuery);
            } else {
                String[] query = inputQuery.split(" ");
                if (query.length > 3) {
                    throw new IllegalArgumentException("Query syntax is improper");
                }
                return queryProcessor.useDatabase(inputQuery, query[2]);
            }
        } else if (createTablePattern.matcher(inputQuery).find()) {
            if (checkFileExists(transactionFilePath)) {
                return writeIntoFile(transactionFilePath, inputQuery);
            } else {
                    int indexOfFirstParanthesis = inputQuery.indexOf("(");
                    int indexOfLastParanthesis = inputQuery.lastIndexOf(")");
                    if (indexOfFirstParanthesis == -1 || indexOfLastParanthesis == -1) {
                        throw new ImproperQuerySyntaxException("");
                    }
                    String tableName = inputQuery.split("\\s+")[2];
                    String columnNames = inputQuery.substring(indexOfFirstParanthesis + 1, indexOfLastParanthesis);
                    String[] columnArray = columnNames.split(",\\s*");
                    Set<Column> columns = new LinkedHashSet<>();
                    for (String col : columnArray) {

                        if (col.contains("primary key")) {
                            int indexOfOpenParan = col.indexOf("(");
                            int indexOfCloseParan = col.indexOf(")");
                            String pkCol = col.substring(indexOfOpenParan + 1, indexOfCloseParan);
                            Column pkColumn = columns.stream().filter(c -> c.getName().equals(pkCol)).findFirst().orElse(null);
                            if (pkColumn != null) {
                                pkColumn.setPk(true);
                                continue;
                            }
                        }

                        //FOREIGN KEY (PersonID) REFERENCES Persons (PersonID)

                        if (col.contains("foreign key")) {
                            int indexOfOpenParan = col.indexOf("(");
                            int indexOfCloseParan = col.indexOf(")");
                            String fkCol = col.substring(indexOfOpenParan + 1, indexOfCloseParan);
                            Column fkColumn = columns.stream().filter(c -> c.getName().equals(fkCol)).findFirst().orElse(null);

                            String colArray[] = col.split("\\s+");

                            if (fkColumn != null) {
                                fkColumn.setFk(true);
                                Table fktable = new Table();
                                fktable.setName(colArray[4]);

                                Column fkTableFkCol = new Column();
                                String fkColName = colArray[5].replace("(", "");
                                fkColName = fkColName.replace(")", "");
                                fkTableFkCol.setName(fkColName);
                                fkColumn.setForeignKeyTable(fktable);
                                fkColumn.setForeignKeyTableCol(fkTableFkCol);
                                continue;
                            }
                        }

                        String[] colArray = col.split("\\s+");
                        String colName = colArray[0];
                        int indexOfOpenParan = colArray[1].indexOf("(");
                        int indexOfCloseParan = colArray[1].indexOf(")");
                        String datatype;
                        int constraint;
                        if (indexOfCloseParan == -1 && indexOfCloseParan == -1) {
                            colName = colArray[0];
                            datatype = colArray[1];

                            if (datatype.equalsIgnoreCase(String.valueOf(Datatype.INT)) || datatype.equalsIgnoreCase(String.valueOf(Datatype.FLOAT))) {
                                constraint = 9;
                            } else {
                                throw new ImproperQuerySyntaxException("Improper query");
                            }
                            Column column = new Column(colName, datatype, constraint);
                            columns.add(column);
                            continue;

                        }

                        datatype = colArray[1].substring(0, indexOfOpenParan);
                        constraint = Integer.parseInt(colArray[1].substring(indexOfOpenParan + 1, indexOfCloseParan));
                        Column column = new Column(colName, datatype, constraint);
                        columns.add(column);
                    }
                    Table table = new Table(tableName, columns);
                    queryProcessor.createTable(inputQuery, database, table);
                return true;
            }
        } else if (insertTablePattern.matcher(inputQuery).find()) {
            if (checkFileExists(transactionFilePath)) {
                return writeIntoFile(transactionFilePath, inputQuery);
            } else {
                // third element of array
                String tableName = inputQuery.split("\\s+")[2];
                //remove ()
                tableName = tableName.substring(0, tableName.length() - 2);

                int indexOfCloseParan = inputQuery.lastIndexOf(")");
                int indexOfOpenParan = inputQuery.lastIndexOf("(");
                String rowValues = inputQuery.substring(indexOfOpenParan + 1, indexOfCloseParan);
                queryProcessor.insertIntoTable(inputQuery, database, tableName, rowValues);

                return true;
            }
        } else if (selectPattern.matcher(inputQuery).find()) {
            if (checkFileExists(transactionFilePath)) {
                return writeIntoFile(transactionFilePath, inputQuery);
            } else {
                String[] query = inputQuery.split("\\s+");
                List<String> selectQueryLiterals = new LinkedList<>();
                for (String s : query) {
                    selectQueryLiterals.add(s);
                }
                String tableName = selectQueryLiterals.get(selectQueryLiterals.indexOf("from") + 1);
                String whereColumn = selectQueryLiterals.get(selectQueryLiterals.indexOf("where") + 1);
                String whereValue = selectQueryLiterals.get(selectQueryLiterals.indexOf(whereColumn) + 2);

                queryProcessor.selectFromTable(inputQuery, database, tableName, whereColumn, whereValue);
                return true;
            }
        } else if (simpleSelectPattern.matcher(inputQuery).find()) {
            if (checkFileExists(transactionFilePath)) {
                return writeIntoFile(transactionFilePath, inputQuery);
            } else {
                String[] query = inputQuery.split("\\s+");
                List<String> selectQueryLiterals = new LinkedList<>();
                for (String s : query) {
                    selectQueryLiterals.add(s);
                }
                String tableName = selectQueryLiterals.get(selectQueryLiterals.indexOf("from") + 1);

                queryProcessor.simpleSelectFromTable(inputQuery, database, tableName);
                return true;
            }
        } else if (deleteTablePattern.matcher(inputQuery).find()) {
            if (checkFileExists(transactionFilePath)) {
                return writeIntoFile(transactionFilePath, inputQuery);
            } else {
                String[] query = inputQuery.split("\\s+");
                List<String> selectQueryLiterals = new LinkedList<>();
                for (String s : query) {
                    selectQueryLiterals.add(s);
                }
                String tableName = selectQueryLiterals.get(selectQueryLiterals.indexOf("from") + 1);
                String whereColumn = selectQueryLiterals.get(selectQueryLiterals.indexOf("where") + 1);
                String whereValue = selectQueryLiterals.get(selectQueryLiterals.indexOf(whereColumn) + 2);
                queryProcessor.deletefromTable(inputQuery, database, tableName, whereColumn, whereValue);
                return true;
            }
        } else if (updateTablePattern.matcher(inputQuery).find()) {
            if (checkFileExists(transactionFilePath)) {
                return writeIntoFile(transactionFilePath, inputQuery);
            } else {
                String[] query = inputQuery.split("\\s+");
                List<String> selectQueryLiterals = new LinkedList<>();
                for (String s : query) {
                    selectQueryLiterals.add(s);
                }
                String tableName = selectQueryLiterals.get(selectQueryLiterals.indexOf("update") + 1);
                String updateColumn = selectQueryLiterals.get(selectQueryLiterals.indexOf("set") + 1);
                String updateValue = selectQueryLiterals.get(selectQueryLiterals.indexOf(updateColumn) + 2);
                String whereColumn = selectQueryLiterals.get(selectQueryLiterals.indexOf("where") + 1);
                String whereValue = selectQueryLiterals.get(selectQueryLiterals.indexOf(whereColumn) + 2);
                queryProcessor.updateTable(inputQuery, database, tableName, updateColumn, updateValue, whereColumn, whereValue);
                return true;
            }
        } else if (dropTablePattern.matcher(inputQuery).find()) {
            if (checkFileExists(transactionFilePath)) {
                return writeIntoFile(transactionFilePath, inputQuery);
            } else {
                String[] query = inputQuery.split(" ");
                if (query.length > 3) {
                    throw new IllegalArgumentException("Query syntax is improper");
                }
                return queryProcessor.dropTable(inputQuery, database, query[2]);
            }
        } else if (startTransactionPattern.matcher(inputQuery).find()) {
            if (inputQuery.equalsIgnoreCase(START_TRANSACTION)) {
                File transactionFile = new File(transactionFilePath);

                if (transactionFile.exists()) {
                    Printer.printContent("File already exists!");
                } else {
                    Printer.printContent("File created!");
                    transactionFile.createNewFile();
                }

                return true;
            } else {
                return false;
            }
        } else if (rollbackTransactionPattern.matcher(inputQuery).find()) {
            if (inputQuery.equalsIgnoreCase(ROLLBACK)) {
                File transactionFile = new File(transactionFilePath);

                if (transactionFile.exists()) {
                    Printer.printContent("File exists and is deleted!");
                    transactionFile.delete();
                } else {
                    Printer.printContent("File doesn't exists!");
                }
                return true;
            } else {
                return false;
            }
        } else if (commitTransactionPattern.matcher(inputQuery).find()) {
            if (inputQuery.equalsIgnoreCase(COMMIT)) {
                File transitionFile = new File(transactionFilePath);
                List<String> transactionQuery = new ArrayList<String>();

                if (transitionFile.exists()) {
                    Scanner scanner = new Scanner(transitionFile);
                    while (scanner.hasNextLine()) {
                        String query = scanner.nextLine();
                        transactionQuery.add(query);
                    }
                    scanner.close();
                    transitionFile.delete();
                    for (String query : transactionQuery) {
                        parseQuery(query);
                    }
                    return true;
                } else {
                    Printer.printContent("There is no ongoing transition");
                    return false;
                }
            } else {
                Printer.printContent("Invalid Query!!");
                return false;
            }
        } else {
            throw new IllegalArgumentException("Query syntax is improper");
        }
    }
        catch (Exception e){
            log.addEventLog(database, SFTP.REMOTE_HOST,Session.getInstance().getLoggedInDBUser().getUserName(), e.toString());
            throw e;
        }
    }
}
