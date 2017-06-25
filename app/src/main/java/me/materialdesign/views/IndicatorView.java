package me.materialdesign.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;


import me.materialdesign.R;

/**
 * 引导页的导航小点点
 * Created by OF on 2016/11/4 0004.
 */

public class IndicatorView extends View {

    private int mFocusColor = Color.CYAN;
    private int mUnFocusColor = Color.CYAN;
    private int mPagemCount;
    private int mSelectedIndex = 0;
    public IndicatorView(Context context) {
        this(context,null);
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public IndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.IndicatorView,defStyleAttr,0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mFocusColor = attributes.getColor(R.styleable.IndicatorView_focus_color, context.getResources().getColor(R.color.green_2, context.getTheme()));
            mUnFocusColor = attributes.getColor(R.styleable.IndicatorView_un_focus_color,context.getResources().getColor(R.color.white,context.getTheme()));
        } else {
            mUnFocusColor = attributes.getColor(R.styleable.IndicatorView_un_focus_color,context.getResources().getColor(R.color.green_2));
            mFocusColor = attributes.getColor(R.styleable.IndicatorView_focus_color,context.getResources().getColor(R.color.white));
        }
        mPagemCount = attributes.getInt(R.styleable.IndicatorView_total_page_count,1);
        attributes.recycle();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }


    private String TAG = "IndicatorView";
    private Paint mPaint;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawColor(Color.RED);
        int left = getLeft();
        int top = getTop();
        int width = getWidth();
        int height = getHeight();
        float wDiff = width/((mPagemCount)*1.00f);
        float hDiff = height/(2*1.00f);
        //计算圆心所在位置
        float[] circlePoint = new float[mPagemCount];

//        Log.i(TAG,"left:"+left+",right:"+right+",top:"+top+",bottom:"+bottom);
        int radius = 20;
        canvas.restore();
        for(int i=0;i<mPagemCount;i++ ) {
            circlePoint[i] = left+wDiff*(i+0.5f);
            Log.i(TAG,"circle point:"+circlePoint[i]);
            if (i == mSelectedIndex) {
                mPaint.setColor(mFocusColor);
                canvas.drawCircle(circlePoint[i] - radius, top + hDiff - radius, radius, mPaint);
            } else {
                mPaint.setColor(mUnFocusColor);
                canvas.drawCircle(circlePoint[i]-radius,top+hDiff-radius,radius,mPaint);
            }
        }
    }

    public void setSelectedIndex(){
        int index = mSelectedIndex;
        if (index < mPagemCount-1) {
            index++;
        } else {
            index = 0;
        }
        mSelectedIndex = index;
        invalidate();
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(true, 0, 100, 720, 300);
    }
}
