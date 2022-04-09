package entities;

import java.util.*;

public class Table {
    private String name;
    private Set<Column> columnNames;

    public Table(){

    }

    public Table(String name, Set<Column> columnNames) {
        this.name = name;
        this.columnNames = columnNames;
    }

    @Override
    public String toString() {
        return "entities.Table{" +
                "name='" + name + '\'' +
                ", columnNames=" + columnNames +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Column> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(Set<Column> columnNames) {
        this.columnNames = columnNames;
    }
}