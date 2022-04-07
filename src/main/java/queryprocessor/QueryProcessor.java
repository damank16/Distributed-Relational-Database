package queryprocessor;

import entities.Table;

public interface QueryProcessor {


    boolean createDatabase(String name);

    boolean dropDatabase(String name);

    boolean createTable(String databaseName,Table table);

    boolean insertIntoTable(String databaseName,String tableName, String rowValues);

    boolean selectFromTable(String databaseName,String tableName, String whereColumn, String whereValue);

    boolean useDatabase(String s);

    boolean dropTable(String database, String tableName);

    boolean deletefromTable(String database, String tableName, String whereColumn, String whereValue);

    boolean updateTable(String database, String tableName, String updateColumn, String updateValue, String whereColumn, String whereValue);
}
