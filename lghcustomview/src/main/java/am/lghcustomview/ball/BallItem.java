package am.lghcustomview.ball;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import java.util.Random;

import am.lghcustomview.base.BaseItem;

/**
 * Created by LinGuanHong on 2017/1/16.
 */

public class BallItem extends BaseItem {

    private PointF center;
    private int radius;
    private float vx;
    private float vy;
    private Random random = new Random();
    private Paint paint;

    public BallItem(int width,int height,Resources resources) {
        super(width,height,resources);
        init();
        loopInit();
    }

    private void init(){
        paint = new Paint();
        paint.setColor(getColor());
    }

    private void loopInit(){

        radius = 150 + random.nextInt(20);
        center = new PointF(
                random.nextInt(width -radius),
                random.nextInt(height-radius)
        );
        /** 卡墙纠正 */
        if(center.x-radius<0){
            center.x += radius;
        }
        if(center.y-radius<0){
            center.y += radius;
        }
        vx = random.nextInt(23);
        vy = random.nextInt(23);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(center.x,center.y,radius,paint);
    }

    @Override
    public void move() {
        if(vx!=0 || vy!=0){
            center.x += vx;
            center.y += vy;
        }
        setCenter(center); /** 重置圆心 */
        boundChech(this);
    }

    public void setCenter(PointF center)
    {
        this.center=center;
    }
    public void setCenter(float x,float y) {
        this.center.x = x;
        this.center.y = y;
    }

    public PointF getCenter()
    {
        return center;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public float getVx() {
        return vx;
    }

    public void setVx(float vx) {
        this.vx = vx;
    }

    public float getVy() {
        return vy;
    }

    public void setVy(float vy) {
        this.vy = vy;
    }

    /** 球体碰撞在这里处理 */
    /** 边界碰撞交给 item */
    /** 细分有：(也是算法优化)
     *     纯垂直
     *     纯水平
     *     纯切面
     * */
    /** r1+r2 > 开方(x1-x2)^2+(y2-y1)^2 */
    public boolean collide(BallItem b1) {
        if(getVx()!= 0 || b1.getVx()!=0 || getVy() !=0 || b1.getVy() !=0 ) {
            double xSquare = Math.pow(center.x - (b1.center.x), 2);
            double ySquare = Math.pow(center.y - (b1.center.y), 2);
            double radiusSquare = Math.pow(radius + b1.getRadius(), 2);
            double distanceSquare = xSquare+ySquare;
            return distanceSquare <= radiusSquare;
        }else {
            return false;
        }
    }

    private int getColor(){
        return 0xff000000 | random.nextInt(0x00ffffff); /**没透明度 */
    }

    /** 墙体 */
    private void boundChech(
            BallItem b1
    ) {
        /** 加减5解决撞墙误差 */
        if (b1.getCenter().x + b1.getRadius() +b1.getVx() >= width - 1) {
            b1.getCenter().x -= 5;
            b1.setVx(-1 * b1.getVx());
        }

        if (b1.getCenter().x + b1.getVx() - b1.getRadius() <= 1) {
            b1.getCenter().x += 5;
            b1.setVx(-1 * b1.getVx());
        }

        if (b1.getCenter().y + b1.getRadius() + b1.getVy() >= height - 1) {
            b1.getCenter().y -= 5;
            b1.setVy(-1 * b1.getVy());
        }
        if (b1.getCenter().y + b1.getVy() - b1.getRadius() <= 1) {
            b1.getCenter().y += 5;
            b1.setVy(-1 * b1.getVy());
        }
    }

}
