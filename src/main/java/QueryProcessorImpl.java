import exceptions.DatabaseAlreadyExistingException;

import java.io.File;
import java.util.Arrays;

public class QueryProcessorImpl implements QueryProcessor {

    @Override
    public void createDatabase( String name) {
        File directory = new File(Constants.BASE_PATH_DIRECTORY + name);
        if(!directory.exists()){
            directory.mkdir();
        }
        else {
            throw new DatabaseAlreadyExistingException("Database " + name + " exits");
        }

    }

    @Override
    public void dropDatabase( String name) {
        File directory = new File(Constants.BASE_PATH_DIRECTORY + name);
        recursivelyDeleteFiles(directory);
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
    public void createTable(String name) {

    }

    @Override
    public void insertIntoTable(String insertQuery) {

    }

    @Override
    public void selectFromTable(String selectQuery) {

    }

    @Override
    public void useDatabase(String s) {

    }

    @Override
    public void dropTable(String s) {

    }
}
