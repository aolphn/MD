package la.xiaosiwo.laught.appliaction;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.yixia.camera.demo.service.AssertService;
import com.yixia.weibo.sdk.VCamera;

import org.litepal.LitePalApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.WeakHashMap;

import la.xiaosiwo.laught.common.Constant;
import la.xiaosiwo.laught.manager.DatabaseManager;
import la.xiaosiwo.laught.manager.ImagesManager;
import la.xiaosiwo.laught.manager.TextsManager;
import la.xiaosiwo.laught.utils.FileUtil;
/**
 * Created by Administrator on 2015/6/26.
 */
public class LaughterApplication extends Application {

    private static Context mContext;
    private String TAG = "LaughterApplication";
    private WeakHashMap<String,Activity> mActivities;
    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init(){
        mContext = getApplicationContext();
        mActivities = new WeakHashMap<>();
        Constant.SDCARD_ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
        initImageLoader();
        LitePalApplication.initialize(this);
        try{
            DatabaseManager.getInstance().init();
            TextsManager.getInstance().init();
            ImagesManager.getInstance().init();
        }catch (Exception e){
            e.printStackTrace();
        }
        initVCamera();
    }

    /**
     * @author:OF,time:2015-07-06 23:41:16
     */
    private void initVCamera(){
        // set cache folder
        //step-1 create folders
        FileUtil.createDir(new File(Constant.APP_ROOT_PATH));
        FileUtil.createDir(new File(Constant.CACHE_PATH));
        VCamera.setVideoCachePath(Constant.CACHE_PATH + "/");
        Log.i(TAG,"create laughter folder");
        // 开启log输出,ffmpeg输出到logcat
        VCamera.setDebugMode(false);
        // 初始化拍摄SDK，必须
        VCamera.initialize(this);
        //解压assert里面的文件
        startService(new Intent(this, AssertService.class));
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

//                .writeDebugLogs() // Remove for release app
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
