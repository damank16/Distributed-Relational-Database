import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import queryprocessor.QueryParser;


public class TableSqls {
    QueryParser queryParser = new QueryParser();

    @Test
    public void testCreateDatabase(){
        Assertions.assertTrue(queryParser.parseQuery("create database a"));
    }
    @Test
    public void testCreateSql(){
        queryParser.parseQuery("use database a");
        queryParser.parseQuery("create table person (id int, name varchar(255), address varchar(255), PRIMARY KEY (id))");
        queryParser.parseQuery("create table item (id int, price float)");
    }

    @Test
    public void testInsertSql(){
        queryParser.parseQuery("use database a");
        queryParser.parseQuery("insert into person() values (1,\"a\",\"ab\")");
        queryParser.parseQuery("insert into person() values (2,\"ab\",\"ab\")");
    }

    @Test
    public void testSelectSql(){
        queryParser.parseQuery("use database a");
        queryParser.parseQuery("select * from person where name = \"a\"");

    }
    @Test
    public void testUpdateSql(){
        queryParser.parseQuery("use database a");
        queryParser.parseQuery("update person set address = \"c\" where name = \"a\"");
        queryParser.parseQuery("select * from person where name = \"a\"");

    }
    @Test
    public void testDeleteSql(){
        queryParser.parseQuery("use database a");
        queryParser.parseQuery("delete from person where name = \"a\"");

    }

    @Test
    public void testDropTableSql(){
        queryParser.parseQuery("use database a");
        queryParser.parseQuery("drop table item");
        queryParser.parseQuery("drop table person");

    }

    @Test
    public void testDropDatabase(){
        Assertions.assertTrue(queryParser.parseQuery("drop database a"));
    }

}