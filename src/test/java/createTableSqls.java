import org.junit.jupiter.api.Test;


public class createTableSqls {
    QueryParser queryParser = new QueryParser();
    @Test
    public void testCreateSql(){
        queryParser.parseQuery("create table person (id int, name varchar(255), address varchar(255))");
        queryParser.parseQuery("create table item (id int, price float)");
    }

    @Test
    public void testInsertSql(){
        queryParser.parseQuery("insert into person() values (1,\"a\",\"ab\")");
    }


}
