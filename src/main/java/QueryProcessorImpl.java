

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import exceptions.DatabaseAlreadyExistingException;
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
    public boolean createTable(Table table, String dbName) {
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
                System.out.println("Table created");
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
    public boolean insertIntoTable(String tableName, String rowValues) {
        try {
            Path path = Paths.get("./a/" + tableName + "_metadata.txt");
            long lines = Files.lines(path).count();
            String rowArray[] = rowValues.split(",");
            if (lines == rowArray.length){
                FileWriter fileWriter = new FileWriter("./a/" + tableName + ".txt",true);
                String rowLine = "";
                for ( String row: rowArray){
                    rowLine+= row + "|";
                }
                rowLine = rowLine.substring(0,rowLine.length()-1) + "\n";
                fileWriter.write(rowLine);
                fileWriter.flush();
                fileWriter.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean selectFromTable(String tableName, String whereColumn, String whereValue) {
        try{
            BufferedReader br = new BufferedReader(new FileReader("./a/" + tableName + ".txt"));
            List<List<String>> rows = new LinkedList<>();
            List<List<String>> result = new LinkedList<>();
            String  line = br.readLine();
            String header = line;
            String headerArray[] = header.split("\\|");
            result.add(Arrays.asList(headerArray));


            while ((line = br.readLine())!=null){
                String rowArray[] = line.split("\\|");
                rows.add(Arrays.asList(rowArray));
            }

            int whereColIndex = Arrays.asList(headerArray).indexOf(whereColumn);
            ;
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
    public boolean useDatabase(String s) {
        return false;
    }

    @Override
    public boolean dropTable(String s) {
        return false;
    }
}
