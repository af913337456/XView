package am.lghcustomview.base;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;

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

public abstract class BaseSurfaceView extends SurfaceView {

    public BaseSurfaceView(Context context) {
        super(context);
    }

    public BaseSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected String TAG = "zzzzz";
    protected int sleepTime = 30;
    private RefreshThread refreshThread = null;

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
