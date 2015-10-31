package la.xiaosiwo.laught.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import la.xiaosiwo.laught.R;
import la.xiaosiwo.laught.callback.LaughterObjCallback;
import la.xiaosiwo.laught.common.Constant;
import la.xiaosiwo.laught.utils.StringUtil;
import la.xiaosiwo.laught.views.PatternLockView;
import la.xiaosiwo.laught.views.Point;

/**
 * author:OF,time:2015-06-28 21:51:12.
 */
public class SetPatternPwdActivity extends BaseActivity {

    private final String TAG = "SetPatternPwdActivity";
    @Bind(R.id.gesture_hint)
    TextView mHintTv;
    @Bind(R.id.reset_gesture)
    TextView mReset;
    @Bind(R.id.pattern_lock_view)
    PatternLockView mView;
    private ArrayList<Point> first;
    private ArrayList<Point> second;
    private LaughterObjCallback mGestureCompleteListener;

    {
        mGestureCompleteListener = new LaughterObjCallback() {

            @Override
            public void callback(Object o) {
                PatternLockView.Result result = (PatternLockView.Result) o;
                ArrayList<Point> list = result.getDrawPoint();
                if (list == null || list.size() == 0) {

                    return;
                } else if ((list.size() > 0 && list.size() < 4) && first == null) {
                    toast(getString(R.string.pwd_weak));
                    return;
                } else if (first == null) {
                    first = list;
                    mHintTv.setText(getString(R.string.draw_gesture_pwd_again));
                    return;
                } else if (first != null) {
                    second = list;
                    boolean isSame = StringUtil.isSamePattern(first, second);
                    if (!isSame) {
                        toast(getString(R.string.diff_pwd));
                        second = null;
                    } else {
                        toast(getString(R.string.pwd_set_suc));
                        if (mShared != null) {
                            String pwd = StringUtil.getGestureFromPoints(second);
                            first = null;
                            second = null;
                            mShared.edit().putString(Constant.GESTURE_PWD, pwd).commit();
                        }
                        finish();
                    }
                }
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pattern_lock_layout);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
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
