package am.lghcustomview.base;

import android.content.res.Resources;
import android.graphics.Canvas;

/**
 * Created by LinGuanHong on 2017/1/15.
 *
 * My GitHub : https://github.com/af913337456/
 *
 * My Blog   : http://www.cnblogs.com/linguanh/
 *
 * 公共的属性和行为
 *
 */

public abstract class BaseItem {

    protected int width,height;      /** 景内宽高 */
    protected Resources resources;

    public BaseItem(){

    }

    public BaseItem(int width,int height,Resources resources){
        this.width  = width;
        this.height = height;
        this.resources = resources;
    }

    public abstract void draw(Canvas canvas); /** 显示 */
    public abstract void move();     /** 运动 */

}
