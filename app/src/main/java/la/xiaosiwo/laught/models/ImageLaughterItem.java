package la.xiaosiwo.laught.models;

/**
 * Created by OF on 2015/6/24 0024.
 * 2015-06-27 18:19:12
 */
public class ImageLaughterItem extends LaughterItem {

    public ImageLaughterItem(){
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
}
