public interface QueryProcessor {

    void createDatabase(String name);

    void dropDatabase(String name);

    void createTable(String name);

    void insertIntoTable(String insertQuery);

    void selectFromTable(String selectQuery);

    void useDatabase(String s);

    void dropTable(String s);
}
