import java.util.regex.Pattern;

public class QueryParser {
    QueryProcessor queryProcessor = new QueryProcessorImpl();
    private static final String CREATE_TABLE_REGEX = "CREATE TABLE ";
    private static final String DROP_TABLE_REGEX = "DROP TABLE ";
    private static final String DROP_DATABASE_REGEX = "CREATE DATABASE ";
    private static final String CREATE_DATABASE_REGEX = "DROP DATABASE ";
    public String[] parseQuery(String inputQuery){
        Pattern createTablePattern = Pattern.compile(CREATE_TABLE_REGEX,Pattern.CASE_INSENSITIVE);
        Pattern dropTablePattern = Pattern.compile(DROP_TABLE_REGEX,Pattern.CASE_INSENSITIVE);
        Pattern createDatabasePattern = Pattern.compile(DROP_DATABASE_REGEX,Pattern.CASE_INSENSITIVE);
        Pattern dropDatabasePattern = Pattern.compile(CREATE_DATABASE_REGEX,Pattern.CASE_INSENSITIVE);

        if (createDatabasePattern.matcher(inputQuery).find()){
            String [] query = inputQuery.split(" ");
            if (query.length > 3){
                throw new IllegalArgumentException("Query syntax is improper");
            }
             queryProcessor.createDatabase(query[2]);
        }
        else if (dropDatabasePattern.matcher(inputQuery).find()){
            String [] query = inputQuery.split(" ");
            if (query.length > 3){
                throw new IllegalArgumentException("Query syntax is improper");
            }
            queryProcessor.dropDatabase(query[2]);
        }

        if (createTablePattern.matcher(inputQuery).find()){
            //TODO
        }


        return null;
    }
}
