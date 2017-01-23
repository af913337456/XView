package am.lghcustomview.money;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.Random;

import am.lghcustomview.R;
import am.lghcustomview.base.BaseItem;

/**
 * Created by LinGuanHong on 2017/1/15.
 */

public class MoneyItem extends BaseItem {

    private Bitmap snowBitmap;
    private Paint paint;
    private Random random;
    private float opt;

    private int dx;
    private int posX,posY;
    private int distance = 0; /** 方向 1 左边飘，2 垂直，3 右边 */
    private int finalDx = 0;

    public MoneyItem(int width, int height, Resources resources) {
        super(width, height,resources);
        init();
    }

    private void init(){
        paint = new Paint(Paint.ANTI_ALIAS_FLAG); /** 抗锯齿  */
        random = new Random();
        snowBitmap = BitmapFactory.decodeResource(resources, R.drawable.red);
        loopInit();
    }

    @Override
    public void draw(Canvas canvas) {
        Log.d("zzzzz","雪 "+posX+" "+posY+" ");
        canvas.drawBitmap(snowBitmap, posX, posY, paint);
    }

    private void loopInit(){
        posX = random.nextInt(width);
        posY = random.nextInt(15); /** 限制从头部不远处下 */
        opt = 0.5f + random.nextFloat();
        dx  = 5    + random.nextInt(10);

        distance = random.nextInt(10);
        /** 改变 x 轨迹为不定情况 */
        /** 不放置到 move 否则一直在改变 */
        int dxRand = 2;
        switch(distance){
            case 1:
            case 2:
            case 3:
                finalDx += -random.nextInt(dxRand);
                break;
            case 4:
            case 5:
                /** no change */
                break;
            case 6:
            case 7:
            case 8:
            case 9:
                finalDx += random.nextInt(dxRand);
                break;
        }
    }

    @Override
    public void move() {
        posX += finalDx;
        posY += dx * opt;  /** 注意相乘变成 0 的情况 */
        if(posY > height){
            loopInit();
        }
    }
}
