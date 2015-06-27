package la.xiaosiwo.laught.appliaction;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2015/6/26.
 */
public class LaughterApplication extends Application {

    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init(){
        mContext = getApplicationContext();
    }

    /**
     * The developmenter should get APP's context from here.
     * @return
     */
    public static Context getAppContext(){
        return mContext;
    }
}
