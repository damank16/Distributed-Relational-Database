import Logger.Log;
import org.junit.jupiter.api.Test;

public class LogTest {
    Log log = Log.getLogInstance();

    @Test
    public void addGeneralLog(){
        log.addGeneralLog(12, 1,"VM1", "user1", "db1", "sql dump generated successfully");
    }

    @Test
    public void addEventLog(){
        log.addEventLog("db1", "t01", false, "vm1", "user1");
    }

    @Test
    public void addQueryLog(){
        log.addQueryLog("db1", true, "select * from user", "select", "user1", "vm1", "table1");
    }
}
