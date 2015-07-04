package la.xiaosiwo.laught.appliaction;

import android.app.Activity;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.litepal.LitePalApplication;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.WeakHashMap;

import la.xiaosiwo.laught.manager.DatabaseManager;
import la.xiaosiwo.laught.manager.ImagesManager;
import la.xiaosiwo.laught.manager.TextsManager;

/**
 * Created by Administrator on 2015/6/26.
 */
public class LaughterApplication extends LitePalApplication {

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
        initImageLoader();
        LitePalApplication.initialize(this);
        try{
            DatabaseManager.getInstance().init();
            TextsManager.getInstance().init();
            ImagesManager.getInstance().init();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void addOneActivity(Activity aty){
        if (mActivities == null){
            mActivities = new WeakHashMap<>();
        }
        mActivities.put(aty.getLocalClassName(), aty);
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

    private void initImageLoader() {

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        ImageLoader.getInstance().init(config);

    }

    @Override
    public void onTerminate() {
        TextsManager.getInstance().destroy();
        ImagesManager.getInstance().destroy();
        DatabaseManager.getInstance().destroy();
        super.onTerminate();
    }
}
