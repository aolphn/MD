package la.xiaosiwo.laught.views;

/**
 * Created by OF on 14:48.
 */
public class Point {
    float x;
    float y;
    public static int STATE_NORMAL = 0;
    public static int STATE_PRESS = 1;
    public static int STATE_ERROR = 2;
    public int state = STATE_NORMAL;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String value;
    public Point(float x,float y){
        this.x = x;
        this.y = y;
    }

    public float distance(Point p){
        float distance = (float)Math.sqrt((x-p.x)*(x-p.x)+(y-p.y)*(y-p.y));
        return  distance;
    }
}
