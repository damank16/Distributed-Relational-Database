package entities;

public class Column {
    private String name;
    private String type;
    private int constraint;
    private boolean isPk;
    private boolean isFk;
    private Table foreignKeyTable;
    private Column foreignKeyTableCol;

    public Column(String name, String type, int constraint) {
        this.name = name;
        this.type = type;
        this.constraint = constraint;
    }

    public Column() {

    }

    public Column getForeignKeyTableCol() {
        return foreignKeyTableCol;
    }

    public void setForeignKeyTableCol(Column foreignKeyTableCol) {
        this.foreignKeyTableCol = foreignKeyTableCol;
    }

    public Table getForeignKeyTable() {
        return foreignKeyTable;
    }

    public void setForeignKeyTable(Table foreignKeyTable) {
        this.foreignKeyTable = foreignKeyTable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getConstraint() {
        return constraint;
    }

    public void setConstraint(int constraint) {
        this.constraint = constraint;
    }

    public boolean isPk() {
        return isPk;
    }

    public void setPk(boolean pk) {
        isPk = pk;
    }

    public boolean isFk() {
        return isFk;
    }

    public void setFk(boolean fk) {
        isFk = fk;
    }

    @Override
    public String toString() {
        return "entities.Column{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", constraint=" + constraint +
                '}';
    }
}
