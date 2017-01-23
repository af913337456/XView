package am.lghcustomview.rain;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;

import am.lghcustomview.base.ShowView;

/**
 * Created by LinGuanHong on 2017/1/15.
 *
 * 造雨，造多少个，160个，具体是什么雨，交给 item 实现
 *
 */

public class RainView extends ShowView<RainItem> {


    public RainView(Context context) {
        super(context);
    }

    public RainView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void beforeLogicLoop() {

    }

    @Override
    public RainItem getItem(int width, int height, Resources resources) {
        return new RainItem(width,height,resources); /** 要造的雨，是什么雨就在这里传入 */
    }

    @Override
    public int getCount() { /** 要制作的雨数目 */
        return 160;
    }
}
