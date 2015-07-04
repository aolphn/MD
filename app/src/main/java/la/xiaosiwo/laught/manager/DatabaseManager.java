package la.xiaosiwo.laught.manager;

import android.database.sqlite.SQLiteDatabase;

import org.litepal.tablemanager.Connector;

/**
 * Created by OF on 2015/6/24 0024.
 */
public class DatabaseManager {
    private final static String TAG = "DatabaseManager";
    private SQLiteDatabase mDB;
    private static class Loader {
        static DatabaseManager INSTANCE = new DatabaseManager();
    }

    private DatabaseManager() {

    }
    public static DatabaseManager getInstance() {
        return Loader.INSTANCE;
    }

    public void init(){
        if(mDB == null){
            mDB = Connector.getDatabase();
        }
    }
    public void destroy(){

    }
}
