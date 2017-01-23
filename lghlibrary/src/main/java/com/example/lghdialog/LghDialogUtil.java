package com.example.lghdialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by lzq-lgh on 2017/1/7.
 *
 * 这个也是我自己封装的 dialog 工具类
 *
 * lzq is my wife
 *
 */


/**
 *
 * support.v7.app.AlertDialog 用这个，Meri 风格，且兼容低版本
 *
 * */


public class LghDialogUtil {

    public interface DialogCallBack{
        int getLayoutId();
        int getDlgWidth();
        void onHandle(AlertDialog dlg, View rootView);
    }

    public interface DialogOkCallBack{
        void onOk(AlertDialog dlg, View rootView,String... data);
    }

    public interface DialogItemClick{
        void onItemClick(AlertDialog dlg,View view,int position);
    }

    /** 内部处理 */
    private interface BodyViewCallBack{
        String[] onHandleBodyView(View view);
    }

    public static void showSimpleMessageDialog(
            Activity activity,
            String title,
            String message
    ){
        AlertDialog dialog=new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create();
        dialog.show();
    }

    public static void showSecondSimpleDialog(
            final Activity activity
    ){
        showCustomDialog(activity,true, new DialogCallBack() {
            @Override
            public int getLayoutId() {
                return R.layout.second_simple_input_demo;
            }

            @Override
            public int getDlgWidth() {
                return 0;
            }

            @Override
            public void onHandle(AlertDialog dlg, View rootView) {

            }
        });
    }

    public static void showSimplePasswordInputDoialg(
            final Activity activity,
            final String title,
            final int inputLimitNumber,
            final DialogOkCallBack okCallBack
    ){
        showCustomDialog(activity,true, new DialogCallBack() {
            @Override
            public int getLayoutId() {
                return R.layout.simple_password_input_demo;
            }

            @Override
            public int getDlgWidth() {
                return 0;
            }

            @Override
            public void onHandle(AlertDialog dlg, View rootView) {
                commonInputDemoHandler(
                        activity,
                        rootView,
                        dlg,
                        title,
                        "最少输入" + inputLimitNumber + "位",
                        inputLimitNumber,
                        false,
                        okCallBack,
                        new BodyViewCallBack() {
                            @Override
                            public String[] onHandleBodyView(View view) {
                                EditText oldPw,firstInput;
                                oldPw = (EditText) view.findViewById(R.id.oldPw);
                                firstInput = (EditText) view.findViewById(R.id.new_first_input);
                                return new String[]{
                                        oldPw.getText().toString(),
                                        firstInput.getText().toString()
                                };
                            }
                        }
                );
            }
        });
    }

    public static void showSimpleInputDialog(
            final Activity activity,
            final String title,
            final int inputLimitNumber,
            final DialogOkCallBack okCallBack
    ){
        showCustomDialog(activity,true, new DialogCallBack() {
            @Override
            public int getLayoutId() {
                return R.layout.simple_input_demo;
            }

            @Override
            public int getDlgWidth() {
                return 0;
            }

            @Override
            public void onHandle(final AlertDialog dlg, final View rootView) {

                commonInputDemoHandler(
                        activity,
                        rootView,
                        dlg,
                        title,
                        "最多输入"+inputLimitNumber+"位",
                        inputLimitNumber,
                        true,
                        okCallBack,
                        null
                    );
            }
        });
    }

    public static void showSimpleCheckBoxDialog(

            Activity activity,
            final String title
    ){
        showCustomDialog(activity,false, new DialogCallBack() {
            @Override
            public int getLayoutId() {
                return R.layout.simple_checkbox_demo;
            }

            @Override
            public int getDlgWidth() {
                return 0;
            }

            @Override
            public void onHandle(AlertDialog dlg, View rootView) {
                TextView tv = (TextView) rootView.findViewById(R.id.title);
                tv.setText(title);
            }
        });
    }

    public static void showSimpleListDialog(
            final Activity activity,
            final String[] itemStrs,
            final DialogItemClick dialogItemClick
    ){
        showCustomDialog(activity,false, new DialogCallBack() {
            @Override
            public int getLayoutId() {
                return R.layout.simple_list_dlg;
            }

            @Override
            public int getDlgWidth() {
                return 0;
            }

            @Override
            public void onHandle(final AlertDialog dlg, View rootView) {
                LinearLayout linearLayout = (LinearLayout) rootView;
                int size = itemStrs.length;
                for(int i=0;i<size;i++){
                    final TextView tv = new TextView(activity);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            70
                    );
                    lp.height = 120;
                    lp.width = 600;
                    //lp.setMargins(30,15,15,15);

                    tv.setPadding(30,15,15,15);
                    tv.setGravity(Gravity.CENTER_VERTICAL);
                    tv.setBackgroundResource(R.drawable.lghbg);
                    tv.setTextSize(17);
                    tv.setTextColor(Color.GRAY);
                    tv.setLayoutParams(lp);
                    tv.setText(itemStrs[i]);

                    final int position = i;
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(dialogItemClick!=null){
                                dialogItemClick.onItemClick(dlg,tv,position);
                            }
                        }
                    });

                    linearLayout.addView(tv);
                }
            }
        });
    }

    public static void showCustomDialog(
            Activity activity,
            boolean inputModel,
            DialogCallBack dialogCallBack
    ){
        AlertDialog dlg = new AlertDialog.Builder(activity).create();
        if(dialogCallBack == null){
            return;
        }
        View rootView = LayoutInflater
                .from(activity)
                .inflate(dialogCallBack.getLayoutId(), null);

        dialogCallBack.onHandle(dlg,rootView);

        Window localWindow = dlg.getWindow();

        dlg.show();
        localWindow.setContentView(rootView);
        WindowManager.LayoutParams params = localWindow.getAttributes();
        if(dialogCallBack.getDlgWidth() != 0){
            params.width = dialogCallBack.getDlgWidth();//如果不设置,可能部分机型出现左右有空隙,也就是产生margin的感觉
        }
        /** 区分输入模式 */
        if(inputModel){
            params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;//显示dialog的时候,就显示软键盘
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;//就是这个属性导致不能获取焦点,默认的是FLAG_NOT_FOCUSABLE,故名思义不能获取输入焦点,
        }
        params.dimAmount = 0.2f;     //  设置布局之外的透明程度
        //params.alpha   = 0.5f;    // 调节透明度，布局整体，无视布局颜色
        localWindow.setAttributes(params);
    }

    /** demo 的公共处理输入情况 */
    /** todo make it looks more simple,in english
      * todo 让它看起来更加简短 in chinese */
    private static void commonInputDemoHandler(
            final Activity activity,
            final View rootView,
            final AlertDialog dlg,
            final String title,
            final String limitStr,
            final int inputLimitNumber,
            final boolean isMore,
            final DialogOkCallBack okCallBack,
            final BodyViewCallBack bodyViewCallBack
    ){
        TextView tv = (TextView) rootView.findViewById(R.id.title);
        tv.setText(title);

        TextView limit = (TextView) rootView.findViewById(R.id.limit);
        limit.setText(limitStr);

        final EditText input = (EditText) rootView.findViewById(R.id.input);

        /** 文本改变监听 */
        input.addTextChangedListener(
                new SimpleTextWatcher(activity,input,inputLimitNumber,isMore)
        );

        Button cancel,ok;
        cancel = (Button) rootView.findViewById(R.id.cancel);
        ok     = (Button) rootView.findViewById(R.id.ok);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] otherDatas = null;
                if(bodyViewCallBack!=null){
                    otherDatas = bodyViewCallBack.onHandleBodyView(rootView);
                }
                String data = input.getText().toString().trim();
                if(!data.equals("")){
                    if(okCallBack!=null){
                        String[] results = null;
                        if(!isMore){
                            /** 处理最少输入情况 */
                            if(data.length() < inputLimitNumber){
                                Toast.makeText(
                                        activity,
                                        "输入内容不能少于"+inputLimitNumber+"位", Toast.LENGTH_SHORT
                                ).show();
                                return;
                            }
                            if(otherDatas != null){
                                int datasTempLength = otherDatas.length;
                                for(int i=0;i<datasTempLength;i++) {
                                    if(otherDatas[i].length() < inputLimitNumber){
                                        Toast.makeText(
                                                activity,
                                                "输入内容不能少于"+inputLimitNumber+"位", Toast.LENGTH_SHORT
                                        ).show();
                                        return;
                                    }
                                }
                            }
                        }
                        if(otherDatas != null){
                            int datasTempLength = otherDatas.length;
                            results = new String[datasTempLength+1];
                            for(int i=0;i<datasTempLength;i++){
                                results[i] = otherDatas[i];
                            }
                            results[datasTempLength] = data;
                            /** 密码情况，不再在这里处理 两次新密码要相同 */
                            okCallBack.onOk(dlg,rootView,results);
                        }else{
                            okCallBack.onOk(dlg,rootView,data);
                        }
                    }
                }else{
                    Toast.makeText(
                            activity,
                            "输入内容不能为空", Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });
    }

    private static class SimpleTextWatcher implements TextWatcher {

        private Activity activity; /** 如果不想只为了一句 toast 而传入，可以自定义去掉 */
        private EditText editText;
        private CharSequence temp;
        private int editStart,editEnd;

        private int inputLimitNumber;
        private boolean isMore;

        public SimpleTextWatcher(
                Activity activity,
                EditText editText,
                int inputLimitNumber,
                boolean isMore
        ){
            this.activity = activity;
            this.editText = editText;
            this.inputLimitNumber = inputLimitNumber;
            this.isMore = isMore;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            temp = charSequence;
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            editStart = editText.getSelectionStart();
            editEnd   = editText.getSelectionEnd();
            if(isMore){
                if (temp.length() > inputLimitNumber) {
                    Toast.makeText(
                            activity,
                            "你输入的字数已经超过了限制！", Toast.LENGTH_SHORT
                    ).show();
                    editable.delete(editStart - 1, editEnd);//删除多余的
                    int tempSelection = editStart;
                    editText.setText(editable);
                    editText.setSelection(tempSelection);
                }
            }else{
                /** todo something */
            }
        }
    }

}
