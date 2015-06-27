package la.xiaosiwo.laught.manager;

import android.util.Log;

import com.ypy.eventbus.EventBus;

import java.util.ArrayList;

import la.xiaosiwo.laught.R;
import la.xiaosiwo.laught.appliaction.LaughterApplication;
import la.xiaosiwo.laught.events.PrepareImageContentEvent;
import la.xiaosiwo.laught.events.UpdateImageContentUIEvent;
import la.xiaosiwo.laught.events.UpdateTextContentUIEvent;
import la.xiaosiwo.laught.models.ImageLaughterItem;

/**
 * Created by OF on 2015/6/24 0024.
 */
public class ImagesManager {
    private final static String TAG = "TextsManager";

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
        EventBus.getDefault().register(this);
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

    private void initData(){
        mImageItems.clear();
        for(String content: LaughterApplication.getAppContext().getResources().getStringArray(R.array.test_items)){
            ImageLaughterItem item = new ImageLaughterItem();
            item.setmContent(content);
            mImageItems.add(item);
        }
    }
    private void refreshData(){
        mImageItems.clear();
        for(String content: LaughterApplication.getAppContext().getResources().getStringArray(R.array.test_items2)){
            ImageLaughterItem item = new ImageLaughterItem();
            item.setmContent(content);
            mImageItems.add(item);
        }
        EventBus.getDefault().post(new UpdateImageContentUIEvent());
    }
    private void loadMoreData(){
        mImageItems.clear();
        for(String content: LaughterApplication.getAppContext().getResources().getStringArray(R.array.test_items)){
            ImageLaughterItem item = new ImageLaughterItem();
            item.setmContent(content);
            mImageItems.add(item);
        }
        for(String content: LaughterApplication.getAppContext().getResources().getStringArray(R.array.test_items2)){
            ImageLaughterItem item = new ImageLaughterItem();
            item.setmContent(content);
            mImageItems.add(item);
        }
        EventBus.getDefault().post(new UpdateTextContentUIEvent());
    }
    public void destroy(){
        EventBus.getDefault().unregister(this);

    }
}
