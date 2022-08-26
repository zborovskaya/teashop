package by.zborovskaya.final_project.dao.pool;

import java.util.ResourceBundle;

public class DBResourceManager {
    private static final DBResourceManager instance= new DBResourceManager();
    private static final String BUNDLE_NAME = "database";

    private ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME);

    public static DBResourceManager getInstance(){
        return instance;
    }

    public String getValue(String key){
        return bundle.getString(key);
    }
}
