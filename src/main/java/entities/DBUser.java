package entities;

import java.util.Map;

public class DBUser {

    private int id;
    private String userName;
    private String password;
    private Map<Integer,String> securityQuesAns;

    public DBUser(int id, String userName, String password, Map<Integer, String> securityQuesAns) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.securityQuesAns = securityQuesAns;
    }

    public DBUser(String userName, String password, Map<Integer, String> securityQuesAns) {
        this.userName = userName;
        this.password = password;
        this.securityQuesAns = securityQuesAns;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map<Integer, String> getSecurityQuesAns() {
        return securityQuesAns;
    }

    public void setSecurityQuesAns(Map<Integer, String> securityQuesAns) {
        this.securityQuesAns = securityQuesAns;
    }
}
