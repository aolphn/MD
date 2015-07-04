package la.xiaosiwo.laught.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yixia.weibo.sdk.util.StringUtils;

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
        hideProgress();
        mProgressDialog = null;
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

    protected ProgressDialog mProgressDialog;

    public ProgressDialog showProgress(String title, String message) {
        return showProgress(title, message, -1);
    }

    public ProgressDialog showProgress(String title, String message, int theme) {
        if (mProgressDialog == null) {
            if (theme > 0)
                mProgressDialog = new ProgressDialog(this, theme);
            else
                mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mProgressDialog.setCanceledOnTouchOutside(false);// 不能取消
            mProgressDialog.setIndeterminate(true);// 设置进度条是否不明确
        }

        if (!StringUtils.isEmpty(title))
            mProgressDialog.setTitle(title);
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
        return mProgressDialog;
    }

    public void hideProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}
