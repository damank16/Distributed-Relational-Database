package session;

import entities.DBUser;

public class Session {

    private static Session instance = null;
    private DBUser user = null;

    private Session() {
    }

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public void createDBUserSession( DBUser user) {
        this.user = user;
    }

    public DBUser getLoggedInDBUser() {
        return this.user;
    }

    public void destroyDBUserSession() {
        this.user = null;
    }

}
