//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import queryprocessor.QueryParser;
//
//import java.io.IOException;
//
//public class DatabaseSqlsTest {
//    QueryParser queryParser = new QueryParser();
//
//    @Test
//    public void testCreateDatabase() throws IOException {
//        Assertions.assertTrue(queryParser.parseQuery("create database a"));
//    }
//    @Test
//    public void testCreateDatabaseNegative(){
//        Assertions.assertThrows(IllegalArgumentException.class, () -> queryParser.parseQuery("create datbase a"));
//        Assertions.assertThrows(IllegalArgumentException.class, () -> queryParser.parseQuery("create database a b"));
//
//    }
//    @Test
//    public void testDropDatabase() throws IOException {
//        Assertions.assertTrue(queryParser.parseQuery("drop database a"));
//    }
//    @Test
//    public void testDropDatabaseNegative(){
//        Assertions.assertThrows(IllegalArgumentException.class, () -> queryParser.parseQuery("drop datbase a"));
//        Assertions.assertThrows(IllegalArgumentException.class, () -> queryParser.parseQuery("drop database a b"));
//
//    }
//
//
//}
