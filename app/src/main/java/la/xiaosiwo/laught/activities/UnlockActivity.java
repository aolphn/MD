package la.xiaosiwo.laught.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.util.ArrayList;

import la.xiaosiwo.laught.R;
import la.xiaosiwo.laught.callback.LaughterObjCallback;
import la.xiaosiwo.laught.common.Constant;
import la.xiaosiwo.laught.utils.StringUtil;
import la.xiaosiwo.laught.views.PatternLockView;
import la.xiaosiwo.laught.views.Point;

/**
 * author:OF,time:2015-06-28 21:51:12.
 */
public class UnlockActivity extends BaseActivity {

    private final String TAG = "UnlockActivity";
    private PatternLockView mView;
    private TextView mHintTv;
    private TextView mReset;
    private String mUnlockFrom;
    private SharedPreferences mShared;
    private LaughterObjCallback mGestureCompleteListener = new LaughterObjCallback() {

        @Override
        public void callback(Object o) {
            PatternLockView.Result result = (PatternLockView.Result)o;
            ArrayList<Point> list = result.getDrawPoint();
            if(list == null || list.size() == 0){

                return;
            }
            String pwd = StringUtil.getGestureFromPoints(list);
            String oldPwd  = mShared.getString(Constant.GESTURE_PWD,Constant.GESTURE_PWD_DEFAULT);
            if (oldPwd.equals(pwd)) {
                mShared.edit().putLong(Constant.UNLOCK_TIME,System.currentTimeMillis()).commit();
                finish();
            }else{
                Log.i(TAG,"old pwd:"+oldPwd+",cur pwd:"+pwd);
                toast(getString(R.string.pwd_error));
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.pattern_lock_layout);
        init();
    }

    private void init(){
        mView = (PatternLockView) findViewById(R.id.pattern_lock_view);
        Intent intent  = getIntent();
        if(intent != null){
            mUnlockFrom = intent.getStringExtra(Constant.UNLOCK_REASON);
        }
        mHintTv = (TextView) findViewById(R.id.gesture_hint);
        mHintTv.setText(getString(R.string.pls_unlock));
        mReset = (TextView) findViewById(R.id.reset_gesture);
        mReset.setVisibility(View.GONE);
        mView.setCompleteListener(mGestureCompleteListener);
        mShared = getSharedPreferences(Constant.SHARED_PROFILE, Activity.MODE_PRIVATE);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            moveTaskToBack(true);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
