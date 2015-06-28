package la.xiaosiwo.laught.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import la.xiaosiwo.laught.appliaction.LaughterApplication;
import la.xiaosiwo.laught.utils.DateUtil;

/**
 * Created by OF on 2015/6/21.
 */
public abstract  class BaseActivity extends Activity {


    protected ImageLoader imageLoader = ImageLoader.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((LaughterApplication)getApplication()).removeOneActivity(this);
        Log.i(this.getLocalClassName(), ":onCreate at:" + DateUtil.getCurDateStr());
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i(this.getLocalClassName(), ":onResume at:"+ DateUtil.getCurDateStr());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(this.getLocalClassName(), ":onPause at:"+ DateUtil.getCurDateStr());
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.i(this.getLocalClassName(), ":onStop at:"+ DateUtil.getCurDateStr());
    }


    @Override
    protected void onDestroy() {
        ((LaughterApplication)getApplication()).removeOneActivity(this);
        super.onDestroy();
        Log.i(this.getLocalClassName(), ":onDestroy at:"+ DateUtil.getCurDateStr());
    }

    protected void toast(String content){
        Toast.makeText(this,content,Toast.LENGTH_SHORT).show();
    }
}
