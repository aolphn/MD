package me.materialdesign.listener;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import me.materialdesign.R;
import me.materialdesign.activities.ImagePreviewActivity;

/**
 * Created by Administrator on 2015/6/28.
 */
public class ImageClickListener implements View.OnClickListener {
    private  final Activity mActivity;
    private String TAG = "ImageClickListener";
    public ImageClickListener(Activity activity){
        mActivity = activity;
    }
    @Override
    public void onClick(View v) {
        String uri = (String)v.getTag(R.drawable.default_img);
        Log.i(TAG,"image uri from tag:"+uri);
        Intent intent = new Intent(mActivity, ImagePreviewActivity.class);
        intent.putExtra("uri",uri);
        mActivity.startActivity(intent);
    }
}
