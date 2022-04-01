import java.util.*;

public class Table {
    private String name;
    private List<String> columnNames;
    private Map<String, String> columnConstraints;
    private List<Map<String,String>> tableValues;

    public Table(String name){
        this.name = name;
        columnNames = new LinkedList<>();
        columnConstraints = new HashMap<>();
        tableValues = new ArrayList<>();
    }

    public Table(String name, List<String> columnNames, Map<String, String> columnConstraints) {
        this.name = name;
        this.columnNames = columnNames;
        this.columnConstraints = columnConstraints;
        tableValues = new ArrayList<>();
    }

    public Table(String name, List<String> columnNames, Map<String, String> columnConstraints, List<Map<String, String>> tableValues) {
        this.name = name;
        this.columnNames = columnNames;
        this.columnConstraints = columnConstraints;
        this.tableValues = tableValues;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    public Map<String, String> getColumnConstraints() {
        return columnConstraints;
    }

    public void setColumnConstraints(Map<String, String> columnConstraints) {
        this.columnConstraints = columnConstraints;
    }

    public List<Map<String, String>> getTableValues() {
        return tableValues;
    }

    public void setTableValues(List<Map<String, String>> tableValues) {
        this.tableValues = tableValues;
    }
}
