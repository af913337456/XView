package am.myapplication;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.lghdialog.LghDialogUtil;

import java.util.Random;

import am.lghcustomview.ball.BallView;
import am.lghcustomview.money.MoneyView;
import am.lghcustomview.rain.RainView;
import am.lghcustomview.snow.SnowView;

import static am.myapplication.R.id.p;
import static am.myapplication.R.id.zoom;

public class MainActivity extends AppCompatActivity {

    LinearLayout activity_main;
    LinearLayout pContainer;

    ParticleView particleView;
    RainView rainView;
    SnowView snowView;
    MoneyView moneyView;
    BallView ballView;

    private void drawText(Bitmap bg,Canvas c,String s){
        Log.e("aaaaa","画字 "+s);
        Rect rect = new Rect();
        char[] arr = s.toCharArray();
        Paint p = new Paint();
        p.setTextSize(280);
        // p.setTextAlign(Paint.Align.CENTER);
        // p.setTypeface(Typeface.create("System", Typeface.BOLD));
        p.setFakeBoldText(true);
        p.setColor(Color.GRAY);
        p.getTextBounds(
                arr,
                0,
                arr.length,
                rect
        );
        c.drawText(
                s,
                bg.getWidth() / 2-rect.width()/2,
                bg.getHeight() / 2+rect.height()/2,
                p
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity_main = (LinearLayout)findViewById(R.id.activity_main);

        particleView = (ParticleView) findViewById(p);
        particleView.setTag(true);
        changeText("L O V E");

        findViewById(zoom).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean zoom = false;
                        if(v.getTag() != null){
                            zoom = !(boolean) v.getTag();
                        }
                        v.setTag(zoom);
                        particleView.setParticleZoom(zoom);
                        Toast.makeText(v.getContext(),zoom?"已开启":"已关闭",Toast.LENGTH_SHORT).show();
                    }
                }
        );

        findViewById(R.id.rect).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean rect = false;
                        if(v.getTag() != null){
                            rect = !(boolean) v.getTag();
                        }
                        v.setTag(rect);
                        particleView.setRectParticle(rect);
                        Toast.makeText(v.getContext(),rect?"矩形变幻":"圆形变幻",Toast.LENGTH_SHORT).show();
                    }
                }
        );

        findViewById(R.id.changeTxt).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeText(new String[]{"h e l l o","林","Android","C++"}[new Random().nextInt(4)]);
                    }
                }
        );

        pContainer = (LinearLayout)findViewById(R.id.pContainer);
        rainView   = (RainView)  findViewById(R.id.rain ); // new RainView (this);
        snowView   = (SnowView)  findViewById(R.id.snow ); // new SnowView (this);
        moneyView  = (MoneyView) findViewById(R.id.money); // new MoneyView(this);
        ballView   = (BallView)  findViewById(R.id.ball );

        findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LghDialogUtil.showSimpleListDialog(
                        MainActivity.this,
                        new String[]{"粒子变幻","下雨", "飘雪", "红包雨","碰撞球"},
                        new LghDialogUtil.DialogItemClick() {
                            @Override
                            public void onItemClick(AlertDialog dlg, View view, int position) {
                                switch (position){
                                    case 0:
                                        handle(
                                                position,pContainer,rainView, snowView,moneyView,ballView
                                        );
                                        break;
                                    case 1:
                                        handle(
                                                position,pContainer,rainView, snowView,moneyView,ballView
                                        );
                                        break;
                                    case 2:
                                        handle(
                                                position,pContainer,rainView, snowView,moneyView,ballView
                                        );
                                        break;
                                    case 3:
                                        handle(
                                                position,pContainer,rainView, snowView,moneyView,ballView
                                        );
                                        break;
                                    case 4:
                                        handle(
                                                position,pContainer,rainView, snowView,moneyView,ballView
                                        );
                                        break;
                                }
                                dlg.dismiss();
                            }
                        }
                );
            }
        });
    }

    private void handle(int show,View... views){
        int length = views.length;
        for(int i=0;i<length;i++){
            if(i == show){
                views[i].setVisibility(View.VISIBLE);
            }else{
                views[i].setVisibility(View.GONE);
            }
        }
    }

    private void changeText(final String s){
        particleView.setConfigAndRefreshView(
                new ParticleView.Config()
                        .setCanvasWidth(
                                // 设置画布宽度
                                getWindowManager().getDefaultDisplay().getWidth()
                        )
                        .setCanvasHeight(800) // 设置画布高度
                        .setParticleRefreshTime(50) // 设置每帧刷新间隔
                        .set_x_Step(15) // 设置 x 轴每次取像素点的间隔
                        .set_y_Step(19) // 设置  轴每次取像素点的间隔
                        .setParticleCallBack(
                                new ParticleView.ParticleCallBack() {
                                    @Override
                                    public ParticleView.Particle setParticle(ParticleView.Particle p, int index, int x, int y) {
                                        p.setX(x); // 设置获取回来的 x 为该 粒子的x坐标
                                        p.setY(y); // 设置获取回来的 y 为该 粒子的y坐标
                                        p.setIsZoom(true);  // 设置当前颗粒子是否启动缩放
                                        p.setRadiusMax(12); // 设置当前颗粒子最大的缩放半径
                                        p.setRadius(12);    // 设置当前颗粒子默认的半径

                                        /** 下面的 %3 是我演示 分批次 显示不同效果而设置 **/
                                        if(index % 3==0){
                                            p.setRectParticle(true); // 这个粒子是 正方形 的
                                            p.setIsHash(  // 设置它是否是散列的，即是随机移动
                                                    true,
                                                    new Random().nextInt(30)-15, // x 速率
                                                    new Random().nextInt(30)-15  // y 速率
                                            );
                                        }
                                        return p; // 返回这个粒子
                                    }

                                    @Override
                                    public boolean drawText(Bitmap bg,Canvas c) {
                                        /** 这里就是我们要自定义显示任意文字的地方 */
                                        MainActivity.this.drawText(bg,c,s);
                                        return true; /** 告诉它不要使用默认的 txt */
                                    }
                                }
                        )
        );
    }

}
