package la.xiaosiwo.laught.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import la.xiaosiwo.laught.R;
import la.xiaosiwo.laught.callback.LaughterObjCallback;

/**
 * Created by OF on 14:43.
 */
public class PatternLockView extends View {

    private String TAG = "PatternLockView";
    private Paint paint1;
    private Paint paint2;
    private Paint paint3;
    private Paint pressPaint;
    private Paint errorPaint;
    float mouseX,mouseY;
    private boolean  isDraw = false;
    private LaughterObjCallback mCompleteListener;
    private ArrayList<Point> pointsList = new ArrayList<>();
    public PatternLockView(Context context) {
        super(context);
    }

    public PatternLockView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PatternLockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!inited){
            init();
        }

        if(pointsList.size() > 0) {
            Point a = pointsList.get(0);
            for(int i= 1;i<pointsList.size();i++) {
                Point b = pointsList.get(i);
                drawLine(canvas,a,b);
                a= b;
            }
            if(isDraw) {
                drawLine(canvas,a,new Point(mouseX,mouseY));
            }
        }
        drawPoints(canvas);
    }

    private void drawPoints(Canvas canvas){
        for(int i =0; i< points.length;i++){
            for(int j= 0; j<points[i].length;j++){
                if(points[i][j].state == Point.STATE_NORMAL){
                    canvas.drawCircle(points[i][j].x,points[i][j].y,100,paint1);
                    canvas.drawCircle(points[i][j].x,points[i][j].y,90,paint3);
                }else if(points[i][j].state == Point.STATE_PRESS){
                    canvas.drawCircle(points[i][j].x,points[i][j].y,100,paint2);
                    canvas.drawCircle(points[i][j].x,points[i][j].y,90,paint1);
                }else if(points[i][j].state == Point.STATE_ERROR){
                    canvas.drawCircle(points[i][j].x,points[i][j].y,50,paint3);
                }
            }
        }
    }
    private boolean inited = false;
    private Point[][] points = new Point[3][3];
    private void init(){
        int width = getWidth();
        int height = getHeight();
        int offset = Math.abs(width-height)/2;
        int offsetX,offsetY;
        int space;
        if(width > height){
            space = height/4;
            offsetX = offset;
            offsetY = 0;
        }else{
            space = width/4;
            offsetX = 0;
            offsetY = offset;
        }
        points[0][0] = new Point(offsetX+space*1,offsetY+space*1);
        points[0][0].setValue("one");
        points[0][1] = new Point(offsetX+space*2,offsetY+space*1);
        points[0][1].setValue("two");
        points[0][2] = new Point(offsetX+space*3,offsetY+space*1);
        points[0][2].setValue("three");
        points[1][0] = new Point(offsetX+space*1,offsetY+space*2);
        points[1][0].setValue("four");
        points[1][1] = new Point(offsetX+space*2,offsetY+space*2);
        points[1][1].setValue("five");
        points[1][2] = new Point(offsetX+space*3,offsetY+space*2);
        points[1][2].setValue("six");
        points[2][0] = new Point(offsetX+space*1,offsetY+space*3);
        points[2][0].setValue("seven");
        points[2][1] = new Point(offsetX+space*2,offsetY+space*3);
        points[2][1].setValue("eight");
        points[2][2] = new Point(offsetX+space*3,offsetY+space*3);
        points[2][2].setValue("nine");
        inited = true;
        paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint1.setColor(Color.WHITE);
        paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint2.setColor(Color.RED);
        paint3 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint3.setColor(getResources().getColor(R.color.green_5));
        pressPaint = new Paint();
        pressPaint.setColor(getResources().getColor(R.color.green_4));
        pressPaint.setStrokeWidth(10);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mouseX = event.getX();
        mouseY = event.getY();
        int[] target ;
        int i = -1,j = -1;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                target = getSelectedPoint();
                if(target != null){
                    isDraw = true;
                    i = target[0];
                    j = target[1] ;
                    points[i][j].state = Point.STATE_PRESS;
                    pointsList.add(points[i][j]);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(isDraw){
                    target = getSelectedPoint();
                    if(target != null){
                        i = target[0];
                        j = target[1];
                        if(!pointsList.contains(points[i][j]) && pointsList.size() < 9){
                            points[i][j].state = Point.STATE_PRESS;
                            pointsList.add(points[i][j]);
                        }else if(pointsList.get(0).equals(points[i][j]) && pointsList.size() == 9){
                            pointsList.add(points[i][j]);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                isDraw = false;
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(!isDraw){
                            if(mCompleteListener != null){
                                mCompleteListener.callback(new Result(new ArrayList<Point>(pointsList)));
                                resetState();
                            }
                        }
                    }
                },500);
                break;
        }
        Log.i(TAG,"select position,i:"+i+",j:"+j);
        this.postInvalidate();
        return true;
    }

    private int[] getSelectedPoint(){
        Point point = new Point(mouseX,mouseY);
        for(int i = 0;i < points.length;i++){
            for(int j = 0;j < points[i].length;j++){
                if(points[i][j].distance(point) < 90){
                    int[] result = new int[2];
                    result[0] = i;
                    result[1] = j;
                    return result;
                }
            }
        }
        return  null;
    }

    private void drawLine(Canvas canvas,Point a,Point b){
        if(a.state == Point.STATE_PRESS){
            canvas.drawLine(a.x,a.y,b.x,b.y,pressPaint);
        }else if(a.state == Point.STATE_ERROR) {
            canvas.drawLine(a.x,a.y,b.x,b.y,errorPaint);
        }
    }

    /**
     * 将view重置为初始状态
     */
    private void resetState(){
        for(int i =0; i < points.length;i++){
            for(int j = 0; j < points[i].length;j++){
                points[i][j].state = Point.STATE_NORMAL;
            }
        }
        pointsList.clear();
        this.invalidate();
    }

    /**
     * 设置密码图案绘制结束之后的回调
     * 该回调用于保存第一次绘制的结果，做第二次密码绘制的准备
     * 或者将两次
     *
     * @param l
     */
    public void setCompleteListener(LaughterObjCallback l){
        mCompleteListener = l;
    }

    public class Result{
       ArrayList<Point> drawP;
        public Result(ArrayList<Point> list){
            drawP = list;
        }
        public ArrayList<Point> getDrawPoint(){
            return  drawP;
        }
    }
}
