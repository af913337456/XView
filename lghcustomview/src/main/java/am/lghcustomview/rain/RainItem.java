package am.lghcustomview.rain;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.Random;

import am.lghcustomview.base.BaseItem;

/**
 * Created by LinGuanHong on 2017/1/15.
 */

public class RainItem extends BaseItem {

    private float opt;
    private int sizeX,sizeY; /** 充当角度 */
    private int startX,startY,stopX,stopY;
    private Paint paint;
    private Random random;

    public RainItem(int width, int height, Resources resources) {
        super(width,height,resources);
        init();
        loopInit();
    }

    @Override
    public void move() {
        startX += sizeX * opt;
        stopX  += sizeX * opt;

        startY += sizeY * opt;
        stopY  += sizeY * opt;
        if(startY > height){
            loopInit();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        Log.d("zzzzz","drawView "+startX+" "+startY+" "+stopX+" "+stopY);
        canvas.drawLine(startX,startY,stopX,stopY,paint);
    }

    private void loopInit(){
        sizeX = 1  + random.nextInt(10);
        sizeY = 10 + random.nextInt(20);

        startX = random.nextInt(width );
        startY = random.nextInt(height);

        opt = 0.2f + random.nextFloat();

        stopX = startX + sizeX;
        stopY = startY + sizeY;
    }

    private void init(){
        paint = new Paint(Paint.ANTI_ALIAS_FLAG); /** 抗锯齿  */
        paint.setColor(0xffffffff); /** a,r,g,b 255,255,255,255 */

        random = new Random();
    }

}
