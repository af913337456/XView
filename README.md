# XView
下雨，飘雪，红包雨，碰撞球，自定义View</br>
代码解析(Code parsing)：http://www.cnblogs.com/linguanh/p/6342099.html


# 效果展示
Video http://pan.baidu.com/s/1miyPn76 </br>
其中，球具备和边界的碰撞检测和每个球之间的碰撞</br></br>
<img src="http://images2015.cnblogs.com/blog/690927/201701/690927-20170122232010785-141851348.jpg" width = "400" height = "800" alt="下雨"/>
<img src="http://images2015.cnblogs.com/blog/690927/201701/690927-20170122232045348-1505543503.jpg" width = "400" height = "800" alt="下雨"/>
<img src="http://images2015.cnblogs.com/blog/690927/201701/690927-20170122232112754-817148735.jpg" width = "400" height = "800" alt="下雨"/>
<img src="http://images2015.cnblogs.com/blog/690927/201701/690927-20170122232134566-1969545427.jpg" width = "400" height = "800" alt="下雨"/>

# MainActivity
```javasetContentView(R.layout.activity_main);

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


```

