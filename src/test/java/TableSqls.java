//import com.jcraft.jsch.IO;
//import entities.DBUser;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import queryprocessor.QueryParser;
//import session.Session;
//
//import java.io.IOException;
//
//
//public class TableSqls {
//    QueryParser queryParser = new QueryParser();
//
//    static {
//        DBUser user = new DBUser(1,"dummy","dummy",null);
//        Session.getInstance().createDBUserSession(user);
//    }
//
//
//    @Test
//    public void testCreateDatabase() throws IOException {
//        Assertions.assertTrue(queryParser.parseQuery("create database a"));
//    }
//    @Test
//    public void testCreateSql() throws IOException {
//        queryParser.parseQuery("use database a");
//        queryParser.parseQuery("create table person (id int, name varchar(255), address varchar(255), PRIMARY KEY (id), FOREIGN KEY (name) REFERENCES Persons (name))");
//        queryParser.parseQuery("create table item (id int, price float)");
//    }
////
//    @Test
//    public void testInsertSql() throws IOException {
//        queryParser.parseQuery("use database a");
//        queryParser.parseQuery("insert into person() values (1,\"a\",\"ab\")");
//        queryParser.parseQuery("insert into person() values (2,\"ab\",\"ab\")");
//    }
//
//    @Test
//    public void testCompanyDatabase() throws IOException{
//        queryParser.parseQuery("create database company");
//        queryParser.parseQuery("use database company");
//        queryParser.parseQuery("CREATE TABLE Employee_tax_details (tax_id int, total_tax float, PRIMARY KEY (tax_id))");
//        queryParser.parseQuery("CREATE TABLE Project (proj_id int, dep_id int, Name varchar(255), PRIMARY KEY (proj_id), FOREIGN KEY (dep_id) REFERENCES Department (dep_id))");
//        queryParser.parseQuery("CREATE TABLE Department (dep_id int, Name varchar(255), PRIMARY KEY (dep_id))");
//        queryParser.parseQuery("CREATE TABLE Employee (emp_id int, dep_id int, tax_id int, LastName varchar(255), FirstName varchar(255), City varchar(255), PRIMARY KEY (emp_id), FOREIGN KEY (dep_id) REFERENCES Department (dep_id), FOREIGN KEY (tax_id) REFERENCES Employee_tax_details (tax_id))");
//
//    }
//
//    @Test
//    public void testInsertCompany() throws IOException {
//        queryParser.parseQuery("use database company");
//        queryParser.parseQuery("INSERT INTO Project() VALUES (1,\"Data Analytics\",1)");
//        queryParser.parseQuery("INSERT INTO Project() VALUES (2,\"Image processing\",1)");
//        queryParser.parseQuery("INSERT INTO Project() VALUES (3,\"Visual Analytics\",2)");
//        queryParser.parseQuery("INSERT INTO Employee_tax_details() values (1,55.69)");
//        queryParser.parseQuery("INSERT INTO Employee_tax_details() values (2,54.69)");
//        queryParser.parseQuery("INSERT INTO Employee_tax_details() values (3,21.69)");
//        queryParser.parseQuery("INSERT INTO Employee_tax_details() values (4,67.69)");
//        queryParser.parseQuery("INSERT INTO Department() values (1,\"Business\")");
//        queryParser.parseQuery("INSERT INTO Department() values (3,\"Marketing\")");
//        queryParser.parseQuery("INSERT INTO Department() values (4,\"Finance\")");
//        queryParser.parseQuery("INSERT INTO Department() values (5,\"Managment\")");
//        queryParser.parseQuery("INSERT INTO Employee() values (1,3,1,\"John\",\"Doe\",\"Vancouver\")");
//        queryParser.parseQuery("INSERT INTO Employee() values (2,2,2,\"Alice\",\"Bob\",\"Halifax\")");
//        queryParser.parseQuery("INSERT INTO Employee() values (3,2,3,\"Eve\",\"Charlie\",\"Toronto\")");
//        queryParser.parseQuery("INSERT INTO Employee() values (4,1,4,\"Dave\",\"Felix\",\"NY\")");
//
//    }
//
//    @Test
//    public void testSelectSql() throws IOException {
//        queryParser.parseQuery("use database a");
//        queryParser.parseQuery("select * from person where name = \"a\"");
//
//    }
//    @Test
//    public void testUpdateSql() throws IOException {
//        queryParser.parseQuery("use database a");
//        queryParser.parseQuery("update person set address = \"c\" where name = \"a\"");
//        queryParser.parseQuery("select * from person where name = \"a\"");
//
//    }
//    @Test
//    public void testDeleteSql() throws IOException {
//        queryParser.parseQuery("use database a");
//        queryParser.parseQuery("delete from person where name = \"a\"");
//
//    }
//
//    @Test
//    public void testDropTableSql() throws IOException {
//        queryParser.parseQuery("use database a");
//        queryParser.parseQuery("drop table item");
//        queryParser.parseQuery("drop table person");
//
//    }
//
//    @Test
//    public void testDropDatabase() throws IOException {
//        Assertions.assertTrue(queryParser.parseQuery("drop database a"));
//    }
//
//    /*@Test
//    public void testCompanyDatabase() throws IOException{
//        queryParser.parseQuery("create database company");
//        queryParser.parseQuery("use database company");
//        queryParser.parseQuery("CREATE TABLE Employee_tax_details (tax_id int, total_tax float, PRIMARY KEY (tax_id))");
//        queryParser.parseQuery("CREATE TABLE Project (proj_id int, dep_id int, Name varchar(255), PRIMARY KEY (proj_id), FOREIGN KEY (dep_id) REFERENCES Department (dep_id))");
//        queryParser.parseQuery("CREATE TABLE Department (dep_id int, Name varchar(255), PRIMARY KEY (dep_id))");
//        queryParser.parseQuery("CREATE TABLE Employee (emp_id int, dep_id int, tax_id int, LastName varchar(255), FirstName varchar(255), City varchar(255), PRIMARY KEY (emp_id), FOREIGN KEY (dep_id) REFERENCES Department (dep_id), FOREIGN KEY (tax_id) REFERENCES Employee_tax_details (tax_id))");
//
//    }
//
//    @Test
//    public void testInsertCompany() throws IOException {
//        queryParser.parseQuery("use database company");
//        queryParser.parseQuery("INSERT INTO Project() VALUES (1,\"Data Analytics\",1)");
//        queryParser.parseQuery("INSERT INTO Project() VALUES (2,\"Image processing\",1)");
//        queryParser.parseQuery("INSERT INTO Project() VALUES (3,\"Visual Analytics\",2)");
//        queryParser.parseQuery("INSERT INTO Employee_tax_details() values (1,55.69)");
//        queryParser.parseQuery("INSERT INTO Employee_tax_details() values (2,54.69)");
//        queryParser.parseQuery("INSERT INTO Employee_tax_details() values (3,21.69)");
//        queryParser.parseQuery("INSERT INTO Employee_tax_details() values (4,67.69)");
//        queryParser.parseQuery("INSERT INTO Department() values (1,\"Business\")");
//        queryParser.parseQuery("INSERT INTO Department() values (3,\"Marketing\")");
//        queryParser.parseQuery("INSERT INTO Department() values (4,\"Finance\")");
//        queryParser.parseQuery("INSERT INTO Department() values (5,\"Managment\")");
//        queryParser.parseQuery("INSERT INTO Employee() values (1,3,1,\"John\",\"Doe\",\"Vancouver\")");
//        queryParser.parseQuery("INSERT INTO Employee() values (2,2,2,\"Alice\",\"Bob\",\"Halifax\")");
//        queryParser.parseQuery("INSERT INTO Employee() values (3,2,3,\"Eve\",\"Charlie\",\"Toronto\")");
//        queryParser.parseQuery("INSERT INTO Employee() values (4,1,4,\"Dave\",\"Felix\",\"NY\")");
//
//    }*/
//
//    @Test
//    public void testSelect()throws IOException {
//        queryParser.parseQuery("use database company");
//        queryParser.parseQuery("select * from employee");
//        queryParser.parseQuery("select * from Department where name = \"Management\"");
//        queryParser.parseQuery("update Employee_tax_details set total_tax = 100 where tax_id = 1");
//        queryParser.parseQuery("select * from employee_tax_details");
//        queryParser.parseQuery("delete from Department where name = \"Management\"");
//        queryParser.parseQuery("select * from Department");
//
//    }
//
//}
