package la.xiaosiwo.laught.manager;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.ypy.eventbus.EventBus;
import com.ypy.eventbus.EventBusException;

import java.util.ArrayList;

import la.xiaosiwo.laught.appliaction.LaughterApplication;
import la.xiaosiwo.laught.events.PrepareImageContentEvent;
import la.xiaosiwo.laught.events.UpdateImageContentUIEvent;
import la.xiaosiwo.laught.models.ImageLaughterItem;

/**
 * Created by OF on 2015/6/24 0024.
 */
public class ImagesManager {
    private final static String TAG = "ImagesManager";

    private ArrayList<ImageLaughterItem> mImageItems;
    private static class Loader {
        static ImagesManager INSTANCE = new ImagesManager();
    }

    private ImagesManager() {
        if (mImageItems == null){
            mImageItems = new ArrayList<ImageLaughterItem>(100);
        }

    }
    public static ImagesManager getInstance() {
        return Loader.INSTANCE;
    }
    public void init(){
        try {
            EventBus.getDefault().register(this);
        }catch (EventBusException e){
            e.printStackTrace();
        }
        EventBus.getDefault().post(new PrepareImageContentEvent(PrepareImageContentEvent.INIT_DATA));
    };
    public ArrayList<ImageLaughterItem> getmImageItems(){
        if (mImageItems == null){
            mImageItems = new ArrayList<ImageLaughterItem>();
        }
        return mImageItems;
    }
    public void onEventBackgroundThread(PrepareImageContentEvent event) {
        Log.i(TAG, "on event -backgroud-thread");
//        TextsManager.getInstance().init();
        int type = event.getmType();
        switch (type){
            case PrepareImageContentEvent.INIT_DATA:
                initData();
                break;
            case PrepareImageContentEvent.REFRESH_DATA:
                refreshData();
                break;
            case PrepareImageContentEvent.LOAD_MORE_DATA:
                loadMoreData();
                break;

            default:
                break;
        }
    }
    public  final String [] IMAGE_COLUMN = {MediaStore.Images.Media.DATA};
    private void initData(){
        mImageItems.clear();
        ContentResolver cr = LaughterApplication.getAppContext().getContentResolver();
        Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_COLUMN, null, null, null);
        int count = 0;
        while (cursor.moveToNext()){
            String uri = cursor.getString(0);
//            if(!uri.endsWith(".gif")){
//                continue;
//            }
            ImageLaughterItem item = new ImageLaughterItem();
            Log.i(TAG, "we get image uri:" + uri);
            item.setmContent(uri);
            item.setmUrl(uri);
            mImageItems.add(item);
            count++;
        }
    }
    private void refreshData(){
        mImageItems.clear();
        ContentResolver cr = LaughterApplication.getAppContext().getContentResolver();
        Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_COLUMN, null, null, null);
        int count = 0;
        while (cursor.moveToNext() && count < 10){
            if(count < 5){
                count++;
                continue;
            }
            String uri = cursor.getString(0);
            ImageLaughterItem item = new ImageLaughterItem();
            Log.i(TAG,"we get image uri:"+uri);
            item.setmContent(uri);
            item.setmUrl(uri);
            mImageItems.add(item);
            count++;
        }
        EventBus.getDefault().post(new UpdateImageContentUIEvent());
    }
    private void loadMoreData(){
        mImageItems.clear();
        ContentResolver cr = LaughterApplication.getAppContext().getContentResolver();
        Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_COLUMN, null, null, null);
        int count = 0;
        while (cursor.moveToNext() && count < 10){

            String uri = cursor.getString(0);
            ImageLaughterItem item = new ImageLaughterItem();
            Log.i(TAG,"we get image uri:"+uri);
            item.setmContent(uri);
            item.setmUrl(uri);
            mImageItems.add(item);
            count++;
        }
        EventBus.getDefault().post(new UpdateImageContentUIEvent());
    }
    public void destroy(){
        EventBus.getDefault().unregister(this);

    }
}
