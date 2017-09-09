package am.lghcustomview.base;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

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

public abstract class SurfaceShowView <T extends BaseItem>
        extends BaseSurfaceView
{

    protected List<T> itemList = new ArrayList<>();
    protected boolean isStopDraw = false;
    protected int size = 1;

    public SurfaceShowView(Context context) {
        super(context);
    }

    /** 子类实现布局必须要重写这个构造方法 */
    public SurfaceShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void drawSub(Canvas canvas) {
        if(isStopDraw)
            return;
        for(T t:itemList){
            t.draw(canvas);
        }
    }

    @Override
    public void logic() {
        beforeLogicLoop();
        for(T t:itemList){
            t.move();
        }
    }

    public abstract void beforeLogicLoop();
    public abstract T getItem(int width, int height,Resources resources);
    public abstract int getCount();

    @Override
    public void baseInit(final int width,final int height) {
        size = getCount();
        final Resources resources = getResources();
        for(int i = 0; i< size; i++){
            //Thread.sleep(100);
            itemList.add(
                    getItem(
                            width,
                            height,
                            resources
                    )
            );
        }
    }

}
