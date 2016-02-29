package me.materialdesign.activities;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.materialdesign.R;

/**
 * author:OF,time:2015-10-31 21:14:29
 */
public class AutoZoomImageActivity extends BaseActivity {

    private final String TAG = "AutoZoomImageActivity";
    @Bind(R.id.auto_zoom_img)
    ImageView mAutoZoomImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auto_zoom_image);
        ButterKnife.bind(this);
        Animation scale = AnimationUtils.loadAnimation(this,R.anim.image_zoom);
        mAutoZoomImg.startAnimation(scale);
        init();

    }

    private void init() {

    }


    @Override
    protected void onResume() {
        super.onResume();
    }


}
