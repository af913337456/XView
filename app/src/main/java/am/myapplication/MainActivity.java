package am.myapplication;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.example.lghdialog.LghDialogUtil;

import am.lghcustomview.ball.BallView;
import am.lghcustomview.money.MoneyView;
import am.lghcustomview.rain.RainView;
import am.lghcustomview.snow.SnowView;

public class MainActivity extends AppCompatActivity {

    LinearLayout activity_main;

    RainView rainView;
    SnowView snowView;
    MoneyView moneyView;
    BallView ballView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity_main = (LinearLayout)findViewById(R.id.activity_main);

        rainView  = (RainView)  findViewById(R.id.rain);// new RainView (this);
        snowView  = (SnowView)  findViewById(R.id.snow);// new SnowView (this);
        moneyView = (MoneyView) findViewById(R.id.money);// new MoneyView(this);
        ballView  = (BallView)  findViewById(R.id.ball);

        findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LghDialogUtil.showSimpleListDialog(
                        MainActivity.this,
                        new String[]{"下雨", "飘雪", "红包雨","碰撞球"},
                        new LghDialogUtil.DialogItemClick() {
                            @Override
                            public void onItemClick(AlertDialog dlg, View view, int position) {
                                switch (position){
                                    case 0:
                                        handle(
                                                position,rainView, snowView,moneyView,ballView
                                        );
                                        break;
                                    case 1:
                                        handle(
                                                position,rainView, snowView,moneyView,ballView
                                        );
                                        break;
                                    case 2:
                                        handle(
                                                position,rainView, snowView,moneyView,ballView
                                        );
                                        break;
                                    case 3:
                                        handle(
                                                position,rainView, snowView,moneyView,ballView
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

}
