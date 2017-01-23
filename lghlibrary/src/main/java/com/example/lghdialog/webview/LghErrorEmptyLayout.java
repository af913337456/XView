package com.example.lghdialog.webview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lghdialog.R;
/**
 * Created by Administrator on 2016/8/30.
 */

public class LghErrorEmptyLayout extends LinearLayout implements View.OnClickListener{

    private Context mContext;
    private ImageView mErrorImg;
    private ProgressBar mProgress;
    private TextView mTextView;
    private int errorState;
    public static final int HIDE_LAYOUT=4;
    public static final int NETWORK_ERROR=1;
    public static final int NETWORK_LOADING=2;
    public static final int NO_DATA=3;
    public static final int SHOW_LAYOUT=5;
    private Animation fade_in,fade_out;
    private onLayoutClickListener listener;


    public LghErrorEmptyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        initView();
    }

    private void initView(){
        View view= View.inflate(mContext, R.layout.lgh_error_layout,null);
        LayoutParams params=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        mErrorImg= (ImageView) view.findViewById(R.id.fail_img);
        mProgress= (ProgressBar) view.findViewById(R.id.loading);
        mTextView= (TextView) view.findViewById(R.id.loading_txt);
        fade_in= AnimationUtils.loadAnimation(mContext, R.anim.lgh_fade_in);
        fade_in.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                LghErrorEmptyLayout.this.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        fade_out= AnimationUtils.loadAnimation(mContext, R.anim.lgh_fade_out);
        fade_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                LghErrorEmptyLayout.this.setVisibility(GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        addView(view);
    }

    public void setLayoutListener(onLayoutClickListener listener) {
        this.listener = listener;
    }

    public int getLayoutType(){
        return errorState;
    }

    public void setLayoutType(int state){
//        setVisibility(VISIBLE);
        switch (state){
            case NETWORK_ERROR:
                if (this.getVisibility()==GONE){
                    showLayout();
                }
                errorState=NETWORK_ERROR;
                mErrorImg.setVisibility(VISIBLE);
                mProgress.setVisibility(GONE);
                mTextView.setText("无法连接网络");
//                if (!common.hasInternet()){
//                    mTextView.setText(mContext.getString(R.string.error_no_network));
//                    mErrorImg.setBackgroundResource(R.mipmap.page_icon_network);
//                }else {
//                    mTextView.setText(mContext.getString(R.string.error_no_content));
//                    mErrorImg.setBackgroundResource(R.mipmap.pagefailed_bg);
//                }
                break;
            case NETWORK_LOADING:
                if (this.getVisibility()==GONE){
                    showLayout();
                }
                errorState=NETWORK_LOADING;
                mErrorImg.setVisibility(GONE);
                mProgress.setVisibility(VISIBLE);
                mTextView.setText(mContext.getString(R.string.loading_txt));
                break;
            case HIDE_LAYOUT:
                errorState=HIDE_LAYOUT;
                hideLayout();
                break;
            case SHOW_LAYOUT:
                errorState=SHOW_LAYOUT;
                showLayout();
                break;
            case NO_DATA:
                if (this.getVisibility()==GONE){
                    showLayout();
                }
                errorState=NO_DATA;
                mProgress.setVisibility(GONE);
                mErrorImg.setVisibility(VISIBLE);
                mErrorImg.setBackgroundResource(R.drawable.ic_block_black_24dp);
                mTextView.setText(mContext.getString(R.string.error_no_data));
                break;
        }
    }

    public TextView getmTextView() {
        return mTextView;
    }

    @Override
    public void onClick(View view) {
        if (listener!=null){
            listener.onClick();
        }
    }

    public interface onLayoutClickListener{
        void onClick();
    }

    public void hideLayout(){
        this.startAnimation(fade_out);
    }

    public void showLayout(){
        this.startAnimation(fade_in);
    }

}
