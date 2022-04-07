import org.junit.jupiter.api.Test;
import queryprocessor.QueryParser;

public class selectSqlsTest {
    QueryParser queryParser = new QueryParser();

    @Test
    public void testSelectSql(){
        queryParser.parseQuery("select * from person where name = \"a\"");

    }
}
