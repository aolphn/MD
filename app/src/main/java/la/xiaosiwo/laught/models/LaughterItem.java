package la.xiaosiwo.laught.models;

import org.litepal.crud.DataSupport;

/**
 * Created by 克虎 on 2015/6/24 0024.
 */
public class LaughterItem extends DataSupport{
    public static final String TYPE_OF_TEXT = "text";
    public static final String TYPE_OF_IMAGE = "image";
    public static final String TYPE_OF_VIDEO = "video";

    public LaughterItem(){

    }
    public LaughterItem(String type){
        mType = type;
    }
    protected String mType;

    public String getmType() {
        return mType;
    }
    public void setmType(String mType) {
        this.mType = mType;
    }

}
