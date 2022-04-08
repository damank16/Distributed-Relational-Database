import Logger.Log;
import org.junit.jupiter.api.Test;

public class LogTest {
    Log log = new Log();

    @Test
    public void addGeneralLog(){
        log.addGeneralLog(12, 1, 1, "VM1", "user1", "db1");
    }

    @Test
    public void addEventLog(){
        log.addEventLog("db1", "t01", false, "vm1", "user1");
    }

    @Test
    public void addQueryLog(){
        log.addQueryLog("db1", true, "select * from user", "user1", "vm1", "table1");
    }
}