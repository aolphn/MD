package la.xiaosiwo.laught.events;

/**
 * This class is used to prepare text content's data.
 * Created by 克虎 on 2015/6/24 0024.
 */
public class PrepareTextContentEvent {

    public static final int INIT_DATA = 1;
    public static final int LOAD_MORE_DATA = 2;
    public static final int REFRESH_DATA = 3;
    public int getmType() {
        return mType;
    }

    private int mType;
    public PrepareTextContentEvent(int type){
        mType = type;
    }


}
