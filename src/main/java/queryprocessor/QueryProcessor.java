package queryprocessor;

import entities.Table;

public interface QueryProcessor {

    boolean createDatabase(String name);

    boolean dropDatabase(String name);

    boolean createTable(Table table, String dbName);

    boolean insertIntoTable(String tableName, String rowValues);

    boolean selectFromTable(String tableName, String whereColumn, String whereValue);

    boolean useDatabase(String s);

    boolean dropTable(String s);
}
