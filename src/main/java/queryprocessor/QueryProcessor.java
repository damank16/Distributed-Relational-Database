package queryprocessor;

import entities.Table;

public interface QueryProcessor {


    boolean createDatabase(String query,String name);

    boolean dropDatabase(String query,String name);

    boolean createTable(String query,String databaseName,Table table);

    boolean insertIntoTable(String query,String databaseName,String tableName, String rowValues);

    boolean selectFromTable(String query,String databaseName,String tableName, String whereColumn, String whereValue);

    boolean useDatabase(String query,String s);

    boolean dropTable(String query,String database, String tableName);

    boolean deletefromTable(String query,String database, String tableName, String whereColumn, String whereValue);

    boolean updateTable(String query,String database, String tableName, String updateColumn, String updateValue, String whereColumn, String whereValue);

    void simpleSelectFromTable(String query,String database, String tableName);
}
