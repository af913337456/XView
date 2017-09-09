package am.myapplication;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.Toast;

import java.util.Random;

import am.lghcustomview.base.BaseItem;
import am.lghcustomview.base.SurfaceShowView;

/**
 * 作者：林冠宏
 * <p>
 * author: LinGuanHong,lzq is my dear wife.
 * <p>
 * My GitHub : https://github.com/af913337456/
 * <p>
 * My Blog   : http://www.cnblogs.com/linguanh/
 * <p>
 * on 2017/9/6.
 */

public class ParticleView extends SurfaceShowView<ParticleView.Particle> {

    private Random random = new Random();
    private String targetText = "";
    private Config config =
            new Config().setParticleCallBack(new ParticleCallBack() {
                @Override
                public Particle setParticle(Particle p, int index, int x, int y) {
                    p.setDeafaultParticle(index,x,y);
                    return p;
                }

                @Override
                public boolean drawText(Bitmap bg,Canvas c) {
                    return false;
                }
            });
    private Bitmap bgBitmap = Bitmap.createBitmap(
            config.w,
            config.h,
            Bitmap.Config.ARGB_8888
    );
    private boolean isInit = false;

    public ParticleView(Context context) {
        super(context);
    }

    /**
     * 子类实现布局必须要重写这个构造方法
     *
     * @param context
     * @param attrs
     */
    public ParticleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void baseInit(int width, int height) {
        // 覆盖原来的，不使用默认的 添加 方式
        sleepTime = 50;
    }
    private Paint getDeafaultTxtPaint(){
        Paint p2 = new Paint();
        p2.setTextSize(280);
        p2.setFakeBoldText(true);
        p2.setColor(Color.GRAY);
        return p2;
    }

    private void drawDefaultText(Canvas c){
        Rect rect = new Rect();
        targetText = "L O V E";
        char[] arr = targetText.toCharArray();
        Paint p = getDeafaultTxtPaint();
        p.getTextBounds(
                arr,
                0,
                arr.length,
                rect
        );
        c.drawText(
                targetText,
                bgBitmap.getWidth() / 2 - rect.width() / 2,
                bgBitmap.getHeight() / 2 + rect.height() / 2,
                p
        );
    }

    public Config getConfig(){
        return config;
    }

    @Override
    public void drawSub(Canvas canvas) {
        super.drawSub(canvas);
        if(isInit)
            return;
        isInit = true; // 表明 已经初始化了
        canvas.drawBitmap(bgBitmap,new Matrix(),null);
        canvas.drawColor(Color.GRAY); // 改变背景颜色
        initParticleX_Y(
                new ParticleInitCallBack() {
                    @Override
                    public void setParticle(int index, int x, int y) {
                        Particle p = new Particle();
                        if(config.particleCallBack != null)
                            config.particleCallBack.setParticle(p,index,x,y);
                        else {
                            p.setDeafaultParticle(index,x,y);
                        }
                        itemList.add(p);
                    }

                    @Override
                    public void finish() {
                        isStopDraw = false;
                    }

                    @Override
                    public void draw(Canvas c) {
                        if (config.particleCallBack != null)
                            if(config.particleCallBack.drawText(bgBitmap,c))
                                return;
                        drawDefaultText(c);
                    }

                    @Override
                    public Bitmap setBgBitmap() {
                        return bgBitmap;
                    }

                    @Override
                    public int setX_loop_step() {
                        return config.xStep;
                    }

                    @Override
                    public int setY_loop_step() {
                        return config.yStep;
                    }
                }
        );
    }

    @Override
    public void beforeLogicLoop() {

    }

    @Override
    public Particle getItem(int width, int height, Resources resources) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }

    private long refreshTimeLimit = 0;
    @SuppressWarnings("java.util.ConcurrentModificationException")
    public void setConfigAndRefreshView(Config c){
        long l = System.currentTimeMillis();
        if(l - refreshTimeLimit < 400){
            Toast.makeText(getContext(),"别点太快",Toast.LENGTH_SHORT).show();
            return;
        }
        isStopDraw = true; // 停止绘制

        refreshTimeLimit = l;
        isInit = false;
        this.config = c;
        bgBitmap = Bitmap.createBitmap(
                config.w,
                config.h,
                Bitmap.Config.ARGB_8888
        );
        itemList.clear();
        requestLayout();
    }

    public void setParticleZoom(boolean isZoom){
        for(Particle p : itemList){
            p.setIsZoom(isZoom);
        }
    }
    public void setRectParticle(boolean isRect){
        for(Particle p : itemList){
            p.setRectParticle(isRect);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(config.w,config.h);
    }

    public interface ParticleInitCallBack{
        void setParticle(int index, int x, int y);
        void finish();
        void draw(Canvas c);
        Bitmap setBgBitmap();
        int setX_loop_step();
        int setY_loop_step();
    }
    private void initParticleX_Y(@NonNull final ParticleInitCallBack callBack){

        Bitmap bitmap = callBack.setBgBitmap();
        Canvas c = new Canvas(bitmap);
        callBack.draw(c);
        c.save(Canvas.ALL_SAVE_FLAG); /** 使文字能够画在 bitmap 上面 */
        c.restore();

        int xStep = callBack.setX_loop_step();
        int yStep = callBack.setY_loop_step();
        //------------
        //           |
        //           高
        //           |
        //-----宽-----
        new PathHelper().FastGetData(
                new PathHelper.CallbackBehaviour() {
                    @Override
                    public void callback(int index, int x, int y) {
                        callBack.setParticle(index, x, y);
                    }
                },
                bitmap,
                xStep,
                yStep
        );

        //javaMethod(bitmap,bitmap.getHeight(),bitmap.getWidth(),xStep,yStep,callBack);
        callBack.finish();
    }

    public interface ParticleCallBack{
        Particle setParticle(Particle p, int index, int x, int y);
        boolean drawText(Bitmap bgBitmap, Canvas c);
    }
    public static class Config{
        int ParticleRefreshTime = 50;
        int w = 900,h = 700;
        int xStep = 12,yStep = 16;
        ParticleCallBack particleCallBack;

        public Config setCanvasWidth(int w){
            this.w = w;
            return this;
        }

        public Config setCanvasHeight(int h){
            this.h = h;
            return this;
        }

        public Config setParticleRefreshTime(int r){
            this.ParticleRefreshTime = r;
            return this;
        }

        public Config setParticleCallBack(ParticleCallBack particleCallBack){
            this.particleCallBack = particleCallBack;
            return this;
        }

        public Config set_x_Step(int xStep){
            this.xStep = xStep;
            return this;
        }

        public Config set_y_Step(int yStep){
            this.yStep = yStep;
            return this;
        }

    }

    public class Particle extends BaseItem {

        private int x;
        private int y;
        private float vx;
        private float vy;
        private boolean isAdd  = false;
        private boolean isHash = false;// 散列
        private boolean isZoom = true; // 缩放
        private int randRadiusTimeLimit = -1;
        private int radiusMax = 10;
        private Paint paint = new Paint();
        private int radius;
        private boolean isRect = false;
        private Rect rect;

        public Particle(int width, int height, Resources resources) {
            super(width, height, resources);
        }

        public Particle(){
            super();
            paint.setColor(getColor());
        }

        private int getColor(){
            return 0xff000000 | new Random().nextInt(0x00ffffff); /**没透明度 */
        }

        public void setDeafaultParticle(int index,int x,int y){
            setX(x);
            setY(y);
            setIsZoom(false);
            setRadius(8);
            setRadiusMax(8);
            if (index % 3 == 0) {
                setIsHash(true, random.nextInt(15), random.nextInt(15));
            }
        }

        public void setRectParticle(boolean isRect) {
            this.isRect = isRect;
            if(isRect){
                rect = new Rect(x-radius,y-radius,x+radius,y+radius);
            }
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getRadius() {
            return radius;
        }

        public void setRadius(int radius) {
            this.radius = radius;
        }

        public void setRadiusMax(int radiusMax){
            this.radiusMax = radiusMax;
        }

        public void setIsHash(boolean isHash,int vx,int vy) {
            this.isHash = isHash;
            this.vx = vx;
            this.vy = vy;
        }

        public void setIsZoom(boolean isZoom) {
            this.isZoom = isZoom;
        }

        @Override
        public void draw(Canvas canvas) {
            if(isRect){
                canvas.drawRect(
                        rect,
                        paint
                );
            }else{
                canvas.drawCircle(
                        this.x,
                        this.y,
                        this.radius,
                        paint
                );
            }
        }

        @Override
        public void move() {
            if(isZoom) {
                randRadius_1(this);
                if(this.isRect){
                    rect.left   = x-radius;
                    rect.top    = y-radius;
                    rect.right  = x+radius;
                    rect.bottom = y+radius;
                }
            }
            if(isHash){
                moveCenter();
                boundCheck();
            }
        }

        private void moveCenter(){
            if(vx!=0 || vy!=0){
                x += vx;
                y += vy;
                if(this.isRect){
                    // x-radius,y-radius,x+radius,y+radius
                    rect.left   = x-radius;
                    rect.top    = y-radius;
                    rect.right  = x+radius;
                    rect.bottom = y+radius;
                }
            }
        }

        private void randRadius_1(Particle p){
            if(p.randRadiusTimeLimit == -1){
                p.randRadiusTimeLimit = new Random().nextInt(50)+5;
            }
            if(p.randRadiusTimeLimit != 0){
                p.randRadiusTimeLimit --;
                return;
            }
            if(p.radius <= 2 && !p.isAdd){
                p.isAdd = true;
            }else if(p.radius >= radiusMax && p.isAdd){
                p.isAdd = false;
            }
            if(p.isAdd){
                p.radius++;
            }else{
                p.radius--;
            }
        }

        /** 墙体 碰撞及其 速率角度调整 */
        private void boundCheck() {
            /** 加减5解决撞墙误差 */
            if (x + radius +vx >= config.w - 1) {
                // 右边
                x -= 5;
                vx = (-1 * vx);
            }

            if (x + vx - radius <= 1) {
                // 左边
                x += 5;
                vx = (-1 * vx);
            }

            if (y + radius + vy >= config.h - 1) {
                // 底部碰撞
                y -= 5;
                vy = (-1 * vy);
            }
            if (y + vy - radius <= 1) {
                // 顶部碰撞
                y += 5;
                vy = (-1 * vy);
            }
        }
    }
    
}
