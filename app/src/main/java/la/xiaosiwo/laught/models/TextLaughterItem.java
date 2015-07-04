package la.xiaosiwo.laught.models;

/**
 * Created by 克虎 on 2015/6/24 0024.
 */
public class TextLaughterItem extends LaughterItem {

    public TextLaughterItem(){
        super(LaughterItem.TYPE_OF_TEXT);
    }
    public String getmSrc() {
        return mSrc;
    }

    public void setmSrc(String mSrc) {
        this.mSrc = mSrc;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    private String mContent;
    private String mSrc;
    private String mUrl;

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    private String uId;
}
