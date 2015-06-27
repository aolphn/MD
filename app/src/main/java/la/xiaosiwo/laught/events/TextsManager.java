package la.xiaosiwo.laught.events;

import android.util.Log;

import com.ypy.eventbus.EventBus;

import java.util.ArrayList;

import la.xiaosiwo.laught.R;
import la.xiaosiwo.laught.appliaction.LaughterApplication;
import la.xiaosiwo.laught.models.TextLaughterItem;

/**
 * Created by 克虎 on 2015/6/24 0024.
 */
public class TextsManager {
    private final static String TAG = "TextsManager";

    private ArrayList<TextLaughterItem> mTextItems;
    private static class Loader {
        static TextsManager INSTANCE = new TextsManager();
    }

    private TextsManager() {
        if (mTextItems == null){
            mTextItems = new ArrayList<TextLaughterItem>(100);
        }

    }
    public static TextsManager getInstance() {
        return Loader.INSTANCE;
    }
    public void init(){
        EventBus.getDefault().register(this);
    };
    public ArrayList<TextLaughterItem> getmTextItems(){
        if (mTextItems == null){
            mTextItems = new ArrayList<TextLaughterItem>();
        }
        return mTextItems;
    }
    public void onEventBackgroundThread(PrepareTextContentEvent event) {
        Log.i(TAG, "on event -backgroud-thread");
//        TextsManager.getInstance().init();
        int type = event.getmType();
        switch (type){
            case PrepareTextContentEvent.INIT_DATA:
                initData();
                break;
            case PrepareTextContentEvent.REFRESH_DATA:
                refreshData();
                break;
            case PrepareTextContentEvent.LOAD_MORE_DATA:
                loadMoreData();
                break;

            default:
                break;
        }
        switch (type){
            case  1:

        }

    }

    private void initData(){
        mTextItems.clear();
        for(String content: LaughterApplication.getAppContext().getResources().getStringArray(R.array.test_items)){
            TextLaughterItem item = new TextLaughterItem();
            item.setmContent(content);
            mTextItems.add(item);
        }
    }
    private void refreshData(){
        mTextItems.clear();
        for(String content: LaughterApplication.getAppContext().getResources().getStringArray(R.array.test_items2)){
            TextLaughterItem item = new TextLaughterItem();
            item.setmContent(content);
            mTextItems.add(item);
        }
        EventBus.getDefault().post(new UpdateTextContentUIEvent());
    }
    private void loadMoreData(){
        mTextItems.clear();
        for(String content: LaughterApplication.getAppContext().getResources().getStringArray(R.array.test_items)){
            TextLaughterItem item = new TextLaughterItem();
            item.setmContent(content);
            mTextItems.add(item);
        }
        for(String content: LaughterApplication.getAppContext().getResources().getStringArray(R.array.test_items2)){
            TextLaughterItem item = new TextLaughterItem();
            item.setmContent(content);
            mTextItems.add(item);
        }
        EventBus.getDefault().post(new UpdateTextContentUIEvent());
    }
    public void destroy(){
        EventBus.getDefault().unregister(this);

    }
}
