package queryprocessor;

import entities.Column;
import entities.Datatype;
import entities.Table;
import exceptions.ImproperQuerySyntaxException;

import java.util.*;
import java.util.regex.Pattern;

public class QueryParser {


    public static String database;
    QueryProcessor queryProcessor = new QueryProcessorImpl();
    private static final String CREATE_TABLE_REGEX = "CREATE\\s*TABLE\\s*";
    private static final String INSERT_TABLE_REGEX = "INSERT\\s* INTO\\s*";
    private static final String DROP_TABLE_REGEX = "DROP\\s*TABLE\\s*";
    private static final String CREATE_DATABASE_REGEX = "CREATE\\s*DATABASE\\s*";
    private static final String DROP_DATABASE_REGEX = "DROP\\s*DATABASE\\s*";
    private static final String USE_DATABASE_REGEX = "USE\\s*DATABASE\\s*";
    private static final String SELECT_TABLE_REGEX = "select\\s+(.*)from\\s+(.*)(where)\\s+(.*)";
    private static final String SIMPLE_SELECT_TABLE_REGEX = "select\\s+(.*)from\\s+(.*)";
    private static final String DELETE_ROW = "delete\\s+from\\s+.*where\\s+";
    private static final String UPDATE_ROW = "update\\s+.*set\\s+.*where\\s+.*";
    public boolean parseQuery(String inputQuery){
        Pattern createTablePattern = Pattern.compile(CREATE_TABLE_REGEX,Pattern.CASE_INSENSITIVE);
        Pattern dropTablePattern = Pattern.compile(DROP_TABLE_REGEX,Pattern.CASE_INSENSITIVE);
        Pattern createDatabasePattern = Pattern.compile(CREATE_DATABASE_REGEX,Pattern.CASE_INSENSITIVE);
        Pattern useDatabasePattern = Pattern.compile(USE_DATABASE_REGEX,Pattern.CASE_INSENSITIVE);
        Pattern dropDatabasePattern = Pattern.compile(DROP_DATABASE_REGEX,Pattern.CASE_INSENSITIVE);
        Pattern selectPattern = Pattern.compile(SELECT_TABLE_REGEX,Pattern.CASE_INSENSITIVE);
        Pattern insertTablePattern = Pattern.compile(INSERT_TABLE_REGEX,Pattern.CASE_INSENSITIVE);
        Pattern deleteTablePattern = Pattern.compile(DELETE_ROW,Pattern.CASE_INSENSITIVE);
        Pattern updateTablePattern = Pattern.compile(UPDATE_ROW,Pattern.CASE_INSENSITIVE);
        Pattern simpleSelectPattern = Pattern.compile(SIMPLE_SELECT_TABLE_REGEX,Pattern.CASE_INSENSITIVE);


        // change to lowercase because string.contains() is case sensitive
        inputQuery = inputQuery.toLowerCase(Locale.ROOT);

        if (createDatabasePattern.matcher(inputQuery).find()){
            String [] query = inputQuery.split(" ");
            if (query.length > 3){
                throw new IllegalArgumentException("Query syntax is improper");
            }
            return queryProcessor.createDatabase(query[2]);
        }
        else if (dropDatabasePattern.matcher(inputQuery).find()){
            String [] query = inputQuery.split(" ");
            if (query.length > 3){
                throw new IllegalArgumentException("Query syntax is improper");
            }
            return queryProcessor.dropDatabase(query[2]);
        }
        else if (useDatabasePattern.matcher(inputQuery).find()){
            String [] query = inputQuery.split(" ");
            if (query.length > 3){
                throw new IllegalArgumentException("Query syntax is improper");
            }
            return queryProcessor.useDatabase(query[2]);
        }


        else if (createTablePattern.matcher(inputQuery).find()){
            try{
                int indexOfFirstParanthesis = inputQuery.indexOf("(");
                int indexOfLastParanthesis = inputQuery.lastIndexOf(")");
                if (indexOfFirstParanthesis == -1 || indexOfLastParanthesis == -1){
                    throw new ImproperQuerySyntaxException("Improper query");
                }
                String tableName = inputQuery.split("\\s+")[2];
                String columnNames = inputQuery.substring(indexOfFirstParanthesis + 1,indexOfLastParanthesis);
                String columnArray[] = columnNames.split(",\\s*");
                Set<Column> columns = new LinkedHashSet<>();
                for (String col : columnArray){

                    if (col.contains("primary key")){
                        int indexOfOpenParan = col.indexOf("(");
                        int indexOfCloseParan = col.indexOf(")");
                        String pkCol = col.substring(indexOfOpenParan+1, indexOfCloseParan);
                        Column pkColumn = columns.stream().filter(c->c.getName().equals(pkCol)).findFirst().orElse(null);
                        if (pkColumn!=null) {
                            pkColumn.setPk(true);
                            break;
                        }
                    }

                    String[] colArray = col.split("\\s+");
                    String colName = colArray[0];
                    int indexOfOpenParan = colArray[1].indexOf("(");
                    int indexOfCloseParan = colArray[1].indexOf(")");
                    String datatype;
                    int constraint;
                    if (indexOfCloseParan == -1 && indexOfCloseParan == -1){
                        colName = colArray[0];
                        datatype = colArray[1];

                        if (datatype.equalsIgnoreCase(String.valueOf(Datatype.INT)) ||
                        datatype.equalsIgnoreCase(String.valueOf(Datatype.FLOAT))){
                            constraint = 9;
                        }
                        else {
                            throw new ImproperQuerySyntaxException("Improper query");
                        }
                        Column column = new Column(colName,datatype,constraint);
                        columns.add(column);
                        continue;

                    }


                    datatype = colArray[1].substring(0,indexOfOpenParan);
                    constraint = Integer.parseInt(colArray[1].substring(indexOfOpenParan+1, indexOfCloseParan));
                    Column column = new Column(colName,datatype,constraint);
                    columns.add(column);


                }
                Table table = new Table(tableName, columns);
                queryProcessor.createTable(database,table);

            }
            catch (ImproperQuerySyntaxException e){
                e.printStackTrace();
            }
            return true;

        }

        else if (insertTablePattern.matcher(inputQuery).find()){
            //insert into person() values (1,"a","ab")


            // third element of array
            String tableName = inputQuery.split("\\s+")[2];
            //remove ()
            tableName = tableName.substring(0,tableName.length()-2);

            int indexOfCloseParan = inputQuery.lastIndexOf(")");
            int indexOfOpenParan = inputQuery.lastIndexOf("(");
            String rowValues = inputQuery.substring(indexOfOpenParan+1, indexOfCloseParan);
            queryProcessor.insertIntoTable(database,tableName, rowValues);

           return true;

        }
        else if (selectPattern.matcher(inputQuery).find()){
            String query[] = inputQuery.split("\\s+");
            List<String> selectQueryLiterals = new LinkedList<>();
            for (String s: query){
                selectQueryLiterals.add(s);
            }
            String tableName = selectQueryLiterals.get(selectQueryLiterals.indexOf("from")+1);
            String whereColumn = selectQueryLiterals.get(selectQueryLiterals.indexOf("where")+1);
            String whereValue = selectQueryLiterals.get(selectQueryLiterals.indexOf(whereColumn)+2);

            queryProcessor.selectFromTable( database,tableName,  whereColumn,  whereValue);
            return true;
        }
        else if (simpleSelectPattern.matcher(inputQuery).find()){
            String query[] = inputQuery.split("\\s+");
            List<String> selectQueryLiterals = new LinkedList<>();
            for (String s: query){
                selectQueryLiterals.add(s);
            }
            String tableName = selectQueryLiterals.get(selectQueryLiterals.indexOf("from")+1);
            queryProcessor.simpleSelectFromTable( database,tableName);
            return true;
        }

        else if (deleteTablePattern.matcher(inputQuery).find()){
            String query[] = inputQuery.split("\\s+");
            List<String> selectQueryLiterals = new LinkedList<>();
            for (String s: query){
                selectQueryLiterals.add(s);
            }
            String tableName = selectQueryLiterals.get(selectQueryLiterals.indexOf("from")+1);
            String whereColumn = selectQueryLiterals.get(selectQueryLiterals.indexOf("where")+1);
            String whereValue = selectQueryLiterals.get(selectQueryLiterals.indexOf(whereColumn)+2);
            queryProcessor.deletefromTable( database,tableName,  whereColumn,  whereValue);
            return true;

        }
        else if (updateTablePattern.matcher(inputQuery).find()){
            String query[] = inputQuery.split("\\s+");
            List<String> selectQueryLiterals = new LinkedList<>();
            for (String s: query){
                selectQueryLiterals.add(s);
            }
            String tableName = selectQueryLiterals.get(selectQueryLiterals.indexOf("update")+1);
            String updateColumn = selectQueryLiterals.get(selectQueryLiterals.indexOf("set")+1);
            String updateValue = selectQueryLiterals.get(selectQueryLiterals.indexOf(updateColumn)+2);
            String whereColumn = selectQueryLiterals.get(selectQueryLiterals.indexOf("where")+1);
            String whereValue = selectQueryLiterals.get(selectQueryLiterals.indexOf(whereColumn)+2);
            queryProcessor.updateTable( database,tableName,updateColumn, updateValue, whereColumn,  whereValue);
            return true;

        }
        else if (dropTablePattern.matcher(inputQuery).find()){
            String [] query = inputQuery.split(" ");
            if (query.length > 3){
                throw new IllegalArgumentException("Query syntax is improper");
            }
            return queryProcessor.dropTable(database,query[2]);
        }
        else {
            throw new IllegalArgumentException("Query syntax is improper");
        }
    }
}
