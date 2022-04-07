
public class Column {
    private String name;
    private String type;
    private int constraint;

    public Column(String name, String type, int constraint) {
        this.name = name;
        this.type = type;
        this.constraint = constraint;
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

    @Override
    public String toString() {
        return "Column{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", constraint=" + constraint +
                '}';
    }
}
