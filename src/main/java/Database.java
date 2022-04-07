import java.util.ArrayList;
import java.util.List;

public class Database {
    private String name;
    private List<String> tableList;

    public Database(String name){
        this.name = name;
        this.tableList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTableList() {
        return tableList;
    }

    public void setTableList(List<String> tableList) {
        this.tableList = tableList;
    }
}
