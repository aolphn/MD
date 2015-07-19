package la.xiaosiwo.laught.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.ant.liao.GifView;

import la.xiaosiwo.laught.R;
import la.xiaosiwo.laught.utils.ImageUtil;
import la.xiaosiwo.laught.utils.SystemUtil;
import la.xiaosiwo.laught.views.BitmapImageView;

/**
 * author:OF,time:2015-06-28 21:51:12.
 */
public class ImagePreviewActivity extends BaseActivity {

    private final String TAG = "ImagePreviewActivity";
    private BitmapImageView mImageView;
    private GifView mGifView;
    private String mUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.image_priview_layout);
        mImageView = (BitmapImageView) findViewById(R.id.static_image_view);
        mGifView = (GifView) findViewById(R.id.dynamic_image_view);
        init();
    }

    private void init(){
        mUri = getIntent().getStringExtra("uri");
        Log.i(TAG,"image uri:"+mUri);
        if(mUri == null){
            toast(getString(R.string.image_can_not_display));
            return;
        }
        String absolutePath = null;
        if(mUri.startsWith("file://")){
            absolutePath = mUri.substring(7);
        }else if(mUri.startsWith("/")){
            absolutePath = mUri;
        }else{
            toast(getString(R.string.image_can_not_display));
            return;
        }
        if(mUri.endsWith(".gif")){
            mGifView.setVisibility(View.VISIBLE);
            mImageView.setVisibility(View.GONE);
            mGifView.setGifImage(R.drawable.test_gif);
            mGifView.setGifImage(ImageUtil.getInputStreamFromLocalFile(absolutePath));
            mGifView.setShowDimension(SystemUtil.screenWidth, SystemUtil.screenHeight/2);
        }else{
            mGifView.setVisibility(View.GONE);
            mImageView.setVisibility(View.VISIBLE);
            mImageView.setImagePath(absolutePath);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
