package la.xiaosiwo.laught.appliaction;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.litepal.LitePalApplication;

import java.util.ArrayList;

import la.xiaosiwo.laught.BuildConfig;
import la.xiaosiwo.laught.common.Constant;
import la.xiaosiwo.laught.manager.DatabaseManager;
/**
 * Created by Administrator on 2015/6/26.
 */
public class LaughterApplication extends Application {

    private static Context mContext;
    private String TAG = "LaughterApplication";
    private SparseArray<Activity> mActivities;
    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }
//test git protection
    private void init(){
        mContext = getApplicationContext();
        mActivities = new SparseArray<>();
        Constant.SDCARD_ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
        initImageLoader();
        LitePalApplication.initialize(this);
        try{
            DatabaseManager.getInstance().init();
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.i(TAG,"current channel:"+getChannel());
        Log.i(TAG,"build config,need auto update app:"+ BuildConfig.AUTO_UPDATES);
    }


    public void addOneActivity(Activity aty){
        if (mActivities == null){
            mActivities = new SparseArray<>();
        }
        mActivities.put(aty.getLocalClassName().hashCode(), aty);
    }
    public void removeOneActivity(Activity aty){
        if (mActivities != null){
            mActivities.remove(aty.getLocalClassName().hashCode());
        }
    }
    public ArrayList<Activity> getAppActivities(){
        ArrayList<Activity> list = new ArrayList<>();
        for (int i = 0;i < mActivities.size();i++){
            list.add(mActivities.get(mActivities.keyAt(i)));
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

//                .writeDebugLogs() // Remove for release app
                .build();
        ImageLoader.getInstance().init(config);

    }

    private String getChannel() {
        try {
            PackageManager pm = getPackageManager();
            ApplicationInfo appInfo = pm.getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.getString("UMENG_CHANNEL");
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return "";

    }

    @Override
    public void onTerminate() {
        DatabaseManager.getInstance().destroy();
        super.onTerminate();
    }
}
