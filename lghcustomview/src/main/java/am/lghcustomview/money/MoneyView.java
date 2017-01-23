package am.lghcustomview.money;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;

import am.lghcustomview.base.ShowView;

/**
 * Created by LinGuanHong on 2017/1/15.
 */

public class MoneyView extends ShowView<MoneyItem> {

    public MoneyView(Context context) {
        super(context);
    }

    public MoneyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void beforeLogicLoop() {

    }

    @Override
    public MoneyItem getItem(int width, int height, Resources resources) {
        return new MoneyItem(width,height,resources);
    }

    @Override
    public int getCount() {
        return 50;
    }
}
