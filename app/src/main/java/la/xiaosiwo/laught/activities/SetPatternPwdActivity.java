package la.xiaosiwo.laught.activities;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.util.ArrayList;

import la.xiaosiwo.laught.R;
import la.xiaosiwo.laught.callback.LaughterObjCallback;
import la.xiaosiwo.laught.common.Constant;
import la.xiaosiwo.laught.utils.MD5Util;
import la.xiaosiwo.laught.utils.StringUtil;
import la.xiaosiwo.laught.views.PatternLockView;
import la.xiaosiwo.laught.views.Point;

/**
 * author:OF,time:2015-06-28 21:51:12.
 */
public class SetPatternPwdActivity extends BaseActivity {

    private final String TAG = "SetPatternPwdActivity";
    private PatternLockView mView;
    private TextView mHintTv;
    private TextView mReset;
    private ArrayList<Point> first;
    private ArrayList<Point> second;
    private LaughterObjCallback mGestureCompleteListener = new LaughterObjCallback() {

        @Override
        public void callback(Object o) {
            PatternLockView.Result result = (PatternLockView.Result)o;
            ArrayList<Point> list = result.getDrawPoint();
            if(list == null || list.size() == 0){

                return;
            }else if((list.size() > 0 && list.size() < 4) && first == null){
                toast(getString(R.string.pwd_weak));
                return;
            }else if(first == null){
                first = list;
                mHintTv.setText(getString(R.string.draw_gesture_pwd_again));
                return;
            }else if(first != null){
                second = list;
                boolean isSame = StringUtil.isSamePattern(first,second);
                if(!isSame){
                    toast(getString(R.string.diff_pwd));
                    second = null;
                }else{
                    toast(getString(R.string.pwd_set_suc));
                    if(mShared != null){
                        String pwd = StringUtil.getGestureFromPoints(second);
                        first = null;
                        second = null;
                        mShared.edit().putString(Constant.GESTURE_PWD, MD5Util.MD5Encode(pwd)).commit();
                    }
                    finish();
                }
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
        mHintTv = (TextView) findViewById(R.id.gesture_hint);
        mReset = (TextView) findViewById(R.id.reset_gesture);
        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                first = null;
                second = null;
                mHintTv.setText(getString(R.string.draw_gesture_pwd));
            }
        });
        mView.setCompleteListener(mGestureCompleteListener);

    }
    @Override
    protected void onResume() {
        super.onResume();
    }
}
