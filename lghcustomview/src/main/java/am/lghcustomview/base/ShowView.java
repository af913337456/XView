package am.lghcustomview.base;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LinGuanHong on 2017/1/15.
 *
 * My GitHub : https://github.com/af913337456/
 *
 * My Blog   : http://www.cnblogs.com/linguanh/
 *
 */

public abstract class ShowView<T extends BaseItem> extends BaseView {

    protected List<T> itemList = new ArrayList<>();
    protected int size = 1;

    public ShowView(Context context) {
        super(context);
    }

    /** 子类实现布局必须要重写这个构造方法 */
    public ShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void drawSub(Canvas canvas) {
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
    public void baseInit(int width, int height) {
        size = getCount();
        Resources resources = getResources();
        for(int i = 0; i< size; i++){
            itemList.add(getItem(width,height,resources));
        }
    }

}
