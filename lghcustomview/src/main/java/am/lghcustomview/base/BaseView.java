package am.lghcustomview.base;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by LinGuanHong on 2017/1/15.
 *
 * My GitHub : https://github.com/af913337456/
 *
 * My Blog   : http://www.cnblogs.com/linguanh/
 *
 */

public abstract class BaseView extends View {

    protected String TAG = "zzzzz";
    private static final int sleepTime = 30;
    private RefreshThread refreshThread = null;

    public BaseView(Context context) {
        super(context);
    }

    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public abstract void drawSub(Canvas canvas);

    public abstract void baseInit(int width,int height);

    public abstract void logic();

    @Override
    protected final void onDraw(Canvas canvas) {
        if(refreshThread == null){
            refreshThread = new RefreshThread();
            refreshThread.start();
        }else{
            drawSub(canvas);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        running = false;
        super.onDetachedFromWindow();
    }

    private boolean running = true;
    private class RefreshThread extends Thread{

        @Override
        public void run() {
            baseInit(getWidth(),getHeight());
            while (running){
                try{
                    logic();
                    postInvalidate();
                    Thread.sleep(sleepTime);
                }catch (Exception e){
                    Log.d(TAG,e.toString());
                }
            }
        }
    }
}
