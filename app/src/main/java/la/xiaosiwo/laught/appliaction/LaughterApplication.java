package la.xiaosiwo.laught.appliaction;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.WeakHashMap;

/**
 * Created by Administrator on 2015/6/26.
 */
public class LaughterApplication extends Application {

    private static Context mContext;
    private WeakHashMap<String,Activity> mActivities;
    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init(){
        mContext = getApplicationContext();
        mActivities = new WeakHashMap<>();
    }

    public void addOneActivity(Activity aty){
        if (mActivities == null){
            mActivities = new WeakHashMap<>();
        }
        mActivities.put(aty.getLocalClassName(),aty);
    }
    public void removeOneActivity(Activity aty){
        if (mActivities != null){
            mActivities.remove(aty.getLocalClassName());
        }
    }
    public ArrayList<Activity> getAppActivities(){
        Iterator<String> iterator = mActivities.keySet().iterator();
        ArrayList<Activity> list = new ArrayList<>();
        while (iterator.hasNext()){
            list.add(mActivities.get(iterator.next()));
        }
        return  list;
    }
    /**
     * The developmenter should get APP's context from here.
     * @return
     */
    public static Context getAppContext(){
        return mContext;
    }
}
