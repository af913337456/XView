//package am.lghcustomview.snow;
//
//import android.content.res.Resources;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.Paint;
//import android.util.Log;
//
//import java.util.Random;
//
//import am.lghcustomview.R;
//import am.lghcustomview.base.BaseItem;
//
///**
// * Created by LinGuanHong on 2017/1/15.
// */
//
//public class SnowItem extends BaseItem {
//
//    private Bitmap snowBitmap;
//    private Paint  paint;
//    private Random random;
//    private float opt;
//
//    private int dx;
//    private int posX,posY;
//    private int distance = 0; /** 方向 -1 左边飘，0 垂直，1 右边 */
//
//    public SnowItem(int width, int height, Resources resources) {
//        super(width, height,resources);
//        init();
//    }
//
//    private void init(){
//        paint = new Paint(Paint.ANTI_ALIAS_FLAG); /** 抗锯齿  */
//        random = new Random();
//        snowBitmap = BitmapFactory.decodeResource(resources, R.drawable.snow);
//        loopInit();
//    }
//
//    @Override
//    public void draw(Canvas canvas) {
//        Log.d("zzzzz","雪 "+posX+" "+posY+" ");
//        canvas.drawBitmap(snowBitmap, posX, posY, paint);
//    }
//
//    private void loopInit(){
//        posX = random.nextInt(width);
//        posY = random.nextInt(15); /** 限制从头部不远处下 */
//        opt = 0.5f + random.nextFloat();
//        dx  = 6    + random.nextInt(10);
//
//        distance = -1 + random.nextInt(2);
//    }
//
//    @Override
//    public void move() {
//        /** 改变 x 轨迹为不定情况 */
//        switch (distance){
//            case -1:
//                posX += -10 + random.nextInt(0);
//                break;
//            case 0:
//                /** no change */
//                break;
//            case 1:
//                posX += random.nextInt(10);
//                break;
//        }
//        posX += 2;
//        posY += dx * opt;  /** 注意相乘变成 0 的情况 */
//        if(posY > height){
//            loopInit();
//        }
//    }
//}
