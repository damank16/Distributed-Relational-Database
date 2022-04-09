package queryprocessor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import Util.Constants;
import entities.Column;
import entities.Table;
import exceptions.*;
import replication.SFTP;
import view.DBOperationsOptions;

public class QueryProcessorImpl implements QueryProcessor {

    private SFTP fileTransfer = new SFTP();

    @Override
    public boolean createDatabase( String name) {
        File directory = new File(Constants.BASE_PATH_DIRECTORY + name);
        File metaDatadirectory = new File(Constants.BASE_PATH_DIRECTORY + name + "/metadata/");
        if(!directory.exists()){
            directory.mkdir();
            metaDatadirectory.mkdir();
            if (DBOperationsOptions.isDistributed) {
                fileTransfer.replicate(name, false, false, true, false, false);
                fileTransfer.replicate(name, true, false, true, false, false);
            }

            return true;
        }
        else {
            throw new DatabaseAlreadyExistingException("Database " + name + " exits");
        }

    }

    @Override
    public boolean dropDatabase( String name) {
        File directory = new File(Constants.BASE_PATH_DIRECTORY + name);
        recursivelyDeleteFiles(directory);
        if (DBOperationsOptions.isDistributed) {
            fileTransfer.replicate(name, false, false, false, false, true);
        }
        return true;
    }

    private void recursivelyDeleteFiles(File directory) {
        if (!directory.exists()){
            return;
        }
        if (directory.isDirectory()){
            Arrays.stream(directory.listFiles()).forEach(f -> recursivelyDeleteFiles(f));
        }
        directory.delete();
    }

    @Override
    public boolean createTable(String dbName,Table table) {

        //FOREIGN KEY (PersonID) REFERENCES Persons(PersonID)
        String tableFile = Constants.BASE_PATH_DIRECTORY + dbName + "/" + table.getName() + ".txt";
        String tableMetaDataFile = Constants.BASE_PATH_DIRECTORY + dbName + "/metadata/" + table.getName() + "_metadata.txt";
        File file = new File(tableFile);
        File metadataFile = new File(tableMetaDataFile);
        try {
            if(file.createNewFile()){
                FileWriter tableWriter = new FileWriter(file);
                FileWriter metaWriter = new FileWriter(metadataFile);
                String header = "";
                String metadata="";
                for (Column column : table.getColumnNames()){
                    header += column.getName() + "|";
                    metadata+=column.getName() + "|"+ column.getType() + "|" + column.getConstraint();
                    if (column.isPk()){
                        metadata+="|PK";
                    }

                    if (column.isFk()){
                        metadata+="|FK|"+ column.getForeignKeyTable().getName() +"|" + column.getForeignKeyTableCol().getName();
                    }
                    metadata+="\n";
                }
                header = header.substring(0, header.length()-1);
                tableWriter.write(header + "\n");
                metaWriter.write(metadata);
                tableWriter.flush();
                tableWriter.close();
                metaWriter.flush();
                metaWriter.close();
                if (DBOperationsOptions.isDistributed) {
                    fileTransfer.replicate(tableFile, false, true, false, false, false);
                    fileTransfer.replicate(tableMetaDataFile, true, true, false, false, false);

                }
                return true;
            }else{
                throw  new TableAlreadyExistingException();
            }
        } catch (IOException e) {
            throw new NoDatabaseSelected("no database was selected");
        }
    }




    @Override
    public boolean insertIntoTable(String dbName, String tableName, String rowValues) {
        try {
            String tableFile = Constants.BASE_PATH_DIRECTORY + dbName + "/" + tableName + ".txt";
            String tableMetaDataFile = Constants.BASE_PATH_DIRECTORY + dbName + "/metadata/" + tableName + "_metadata.txt";

            Path path = Paths.get(Constants.BASE_PATH_DIRECTORY + dbName + "/metadata/" + tableName + "_metadata.txt");
            // number of columns in table
            long totalColsInTable = Files.lines(path).count();

            // get existing rows from table to check uniqueness on PK
            String fileName = Constants.BASE_PATH_DIRECTORY + dbName + "/" + tableName + ".txt";
            List<List<String>> existingRows = getRowsOfTable(fileName);

            // add existing Pk values to a list
            List<String> pkValues = new ArrayList<>();
            for (List<String> row :existingRows){
                // add first element of the row  (assuming this is PK)
                pkValues.add(row.get(0));
            }

            String rowArray[] = rowValues.split(",");

            // if number of values inserted is same as number of table columns
            if (totalColsInTable == rowArray.length){
                // check if PK value is already in table
                if (!pkValues.contains(rowArray[0])) {
                    FileWriter fileWriter = new FileWriter(Constants.BASE_PATH_DIRECTORY + dbName + "/" + tableName + ".txt", true);
                    String rowLine = "";
                    for (String row : rowArray) {

                        rowLine += row + "|";
                    }
                    rowLine = rowLine.substring(0, rowLine.length() - 1) + "\n";
                    fileWriter.write(rowLine);
                    fileWriter.flush();
                    fileWriter.close();
                    if (DBOperationsOptions.isDistributed) {
                        fileTransfer.replicate(tableFile, false, true, false, false, false);
                        fileTransfer.replicate(tableMetaDataFile, true, true, false, false, false);
                    }
                }
                else{
                    throw new PrimaryKeyContraintViolationException("Duplicate primary key");
                }
            }



        }
        catch (NoSuchFileException e){
            throw new NoSuchDatabaseObject("No such table");
        }catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }



    @Override
    public boolean selectFromTable(String databaseName,String tableName, String whereColumn, String whereValue) {
        try{
            String fileName = Constants.BASE_PATH_DIRECTORY + databaseName + "/" + tableName + ".txt";
            List<List<String>> result = new LinkedList<>();
            List<List<String>> rows = getRowsOfTable(fileName);
            List<String> headerRow = rows.get(0);
            result.add(headerRow);
            int whereColIndex = headerRow.indexOf(whereColumn);
            for (List<String> row : rows){
                if (row.get(whereColIndex).equals(whereValue)){
                    result.add(row);
                }
            }

            for (List<String> row: result){
                for (String val : row){
                    System.out.print( val + " ");
                }
                System.out.println();
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public boolean useDatabase(String name) {
        File directory = new File(Constants.BASE_PATH_DIRECTORY + name);
        if (directory.exists()){
            QueryParser.database = name;
            return true;
        }
        else {
            throw  new NoSuchDatabaseObject("No such database");
        }
    }

    @Override
    public boolean dropTable(String database, String tableName) {
        String fileName = Constants.BASE_PATH_DIRECTORY + database + "/" + tableName + ".txt";
        String metadataFileName = Constants.BASE_PATH_DIRECTORY + database + "/metadata/" + tableName + "_metadata.txt";
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
            File metadata = new File(metadataFileName);
            metadata.delete();
            if (DBOperationsOptions.isDistributed) {
                fileTransfer.replicate(fileName, false, false, false, true, false);
                fileTransfer.replicate(metadataFileName, true, false, false, true, false);
            }
            return true;
        }
        else {
            throw new NoSuchDatabaseObject("No such table");
        }

    }

    @Override
    public boolean deletefromTable(String database, String tableName, String whereColumn, String whereValue) {
        String tableFile = Constants.BASE_PATH_DIRECTORY + database + "/" + tableName + ".txt";
        String tableMetaDataFile = Constants.BASE_PATH_DIRECTORY + database + "/metadata/" + tableName + "_metadata.txt";

        try {
            String fileName = Constants.BASE_PATH_DIRECTORY + database + "/" + tableName + ".txt";
            List<List<String>> result = new LinkedList<>();
            List<List<String>> rows = getRowsOfTable(fileName);
            List<String> headerRow = rows.get(0);
            result.add(headerRow);
            int whereColIndex = headerRow.indexOf(whereColumn);
            rows.remove(0);
            for (List<String> row : rows) {
                if (!row.get(whereColIndex).equals(whereValue)) {
                    result.add(row);
                }
            }

            FileWriter writer = new FileWriter(fileName);
            for (List<String> row : result) {
                String rowString = "";
                for (String val : row) {
                    rowString += val + "|";
                }
                rowString = rowString.substring(0,rowString.length()-1) + "\n";
                writer.write(rowString);
                //System.out.println();
            }
            writer.flush();
            writer.close();
            if (DBOperationsOptions.isDistributed) {
                fileTransfer.replicate(tableFile, false, true, false, false, false);
                fileTransfer.replicate(tableMetaDataFile, true, true, false, false, false);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public boolean updateTable(String database, String tableName, String updateColumn, String updateValue, String whereColumn, String whereValue) {
        String tableFile = Constants.BASE_PATH_DIRECTORY + database + "/" + tableName+ ".txt";
        String tableMetaDataFile = Constants.BASE_PATH_DIRECTORY + database + "/metadata/" + tableName + "_metadata.txt";

        try {

            String fileName = Constants.BASE_PATH_DIRECTORY + database + "/" + tableName + ".txt";
            List<List<String>> result = new LinkedList<>();
            List<List<String>> rows = getRowsOfTable(fileName);
            List<String> headerRow = rows.get(0);
            result.add(headerRow);
            int whereColIndex = headerRow.indexOf(whereColumn);
            int updateColIndex = headerRow.indexOf(updateColumn);
            rows.remove(0);
            for (List<String> row : rows) {
                if (row.get(whereColIndex).equals(whereValue)) {
                    row.set(updateColIndex, updateValue);
                }
                result.add(row);

            }

            FileWriter writer = new FileWriter(fileName);
            for (List<String> row : result) {
                String rowString = "";
                for (String val : row) {
                    rowString += val + "|";
                }
                rowString = rowString.substring(0, rowString.length() - 1) + "\n";
                writer.write(rowString);
                //System.out.println();
            }
            writer.flush();
            writer.close();
            if (DBOperationsOptions.isDistributed) {
                fileTransfer.replicate(tableFile, false, true, false, false, false);
                fileTransfer.replicate(tableMetaDataFile, true, true, false, false, false);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public void simpleSelectFromTable(String database, String tableName) {
        try{
            String fileName = Constants.BASE_PATH_DIRECTORY + database + "/" + tableName + ".txt";
            List<List<String>> rows = getRowsOfTable(fileName);
            List<String> headerRow = rows.get(0);

            for (List<String> row: rows){
                for (String val : row){
                    System.out.print( val + " ");
                }
                System.out.println();
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    private List<List<String>> getRowsOfTable(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        List<List<String>> rows = new LinkedList<>();
        String  line = br.readLine();
        String header = line;
        String headerArray[] = header.split("\\|");
        rows.add(Arrays.asList(headerArray));


        while ((line = br.readLine())!=null){
            String rowArray[] = line.split("\\|");
            rows.add(Arrays.asList(rowArray));
        }
        return rows;
    }


}
