package queryprocessor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import Util.Constants;
import entities.Column;
import entities.Table;
import exceptions.DatabaseAlreadyExistingException;
import exceptions.NoSuchDatabaseObject;
import exceptions.TableAlreadyExistingException;

public class QueryProcessorImpl implements QueryProcessor {


    @Override
    public boolean createDatabase( String name) {
        File directory = new File(Constants.BASE_PATH_DIRECTORY + name);
        if(!directory.exists()){
            directory.mkdir();
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
        File file = new File(Constants.BASE_PATH_DIRECTORY + dbName + "/" + table.getName() + ".txt");
        File metadataFile = new File(Constants.BASE_PATH_DIRECTORY + dbName + "/" + table.getName() + "_metadata.txt");
        try {
            if(file.createNewFile()){
                FileWriter tableWriter = new FileWriter(file);
                FileWriter metaWriter = new FileWriter(metadataFile);
                String header = "";
                String metadata="";
                for (Column column : table.getColumnNames()){
                    header += column.getName() + "|";
                    metadata+=column.getName() + "|"+ column.getType() + "|" + column.getConstraint() + "\n";
                }
                header = header.substring(0, header.length()-1);
                tableWriter.write(header + "\n");
                metaWriter.write(metadata);
                tableWriter.flush();
                tableWriter.close();
                metaWriter.flush();
                metaWriter.close();
                return true;
            }else{
                throw  new TableAlreadyExistingException();
            }
        } catch (IOException e) {
            return false;
        }
    }

    public boolean createTable(String tableQuery){

        return false;
    }



    @Override
    public boolean insertIntoTable(String dbName, String tableName, String rowValues) {
        try {
            Path path = Paths.get(Constants.BASE_PATH_DIRECTORY + dbName + "/" + tableName + "_metadata.txt");
            long lines = Files.lines(path).count();
            String rowArray[] = rowValues.split(",");
            if (lines == rowArray.length){
                FileWriter fileWriter = new FileWriter(Constants.BASE_PATH_DIRECTORY+dbName +"/" + tableName + ".txt",true);
                String rowLine = "";
                for ( String row: rowArray){
                    rowLine+= row + "|";
                }
                rowLine = rowLine.substring(0,rowLine.length()-1) + "\n";
                fileWriter.write(rowLine);
                fileWriter.flush();
                fileWriter.close();
            }

        }
        catch (NoSuchFileException e){
            throw new NoSuchDatabaseObject();
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
            throw  new NoSuchDatabaseObject();
        }
    }

    @Override
    public boolean dropTable(String database, String tableName) {
        String fileName = Constants.BASE_PATH_DIRECTORY + database + "/" + tableName + ".txt";
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
            File metadata = new File(Constants.BASE_PATH_DIRECTORY + database + "/" + tableName + "_metadata.txt");
            metadata.delete();
            return true;
        }
        else {
            throw new NoSuchDatabaseObject();
        }

    }

    @Override
    public boolean deletefromTable(String database, String tableName, String whereColumn, String whereValue) {
        try{
            String fileName = Constants.BASE_PATH_DIRECTORY + database + "/" + tableName + ".txt";
            List<List<String>> result = new LinkedList<>();
            List<List<String>> rows = getRowsOfTable(fileName);
            List<String> headerRow = rows.get(0);
            result.add(headerRow);
            int whereColIndex = headerRow.indexOf(whereColumn);
            rows.remove(0);
            for (List<String> row : rows){
                if (!row.get(whereColIndex).equals(whereValue)){
                    result.add(row);
                }
            }

            FileWriter writer = new FileWriter(fileName);
            for (List<String> row: result){
                String rowString ="";
                for (String val : row){
                    rowString += val + "|";
                }
                rowString = rowString.substring(0,rowString.length()-1) + "\n";
                writer.write(rowString);
                //System.out.println();
            }
            writer.flush();
            writer.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public boolean updateTable(String database, String tableName, String updateColumn, String updateValue, String whereColumn, String whereValue) {
        try{

            String fileName = Constants.BASE_PATH_DIRECTORY + database + "/" + tableName + ".txt";
            List<List<String>> result = new LinkedList<>();
            List<List<String>> rows = getRowsOfTable(fileName);
            List<String> headerRow = rows.get(0);
            result.add(headerRow);
            int whereColIndex = headerRow.indexOf(whereColumn);
            int updateColIndex = headerRow.indexOf(updateColumn);
            rows.remove(0);
            for (List<String> row : rows){
                if (row.get(whereColIndex).equals(whereValue)){
                    row.set(updateColIndex,updateValue);
                }
                result.add(row);

            }

            FileWriter writer = new FileWriter(fileName);
            for (List<String> row: result){
                String rowString ="";
                for (String val : row){
                    rowString += val + "|";
                }
                rowString = rowString.substring(0,rowString.length()-1) + "\n";
                writer.write(rowString);
                //System.out.println();
            }
            writer.flush();
            writer.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return true;
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
