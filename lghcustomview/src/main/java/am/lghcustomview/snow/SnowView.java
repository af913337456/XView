package am.lghcustomview.snow;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;

import am.lghcustomview.base.ShowView;

/**
 * Created by LinGuanHong on 2017/1/15.
 */

public class SnowView extends ShowView<SnowItem> {


    public SnowView(Context context) {
        super(context);
    }

    public SnowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void beforeLogicLoop() {

    }

    @Override
    public SnowItem getItem(int width, int height, Resources resources) {
        return new SnowItem(width,height,resources);
    }

    @Override
    public int getCount() {
        return 100;
    }
}
