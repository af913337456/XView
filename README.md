# XView
下雨，飘雪，红包雨，碰撞球，粒子变幻、粒子隧道，自定义View</br>
代码解析(Code parsing)：http://www.cnblogs.com/linguanh/p/6342099.html

<pre>

  Created by 林冠宏（指尖下的幽灵）.
 
  Blog : http://www.cnblogs.com/linguanh/;
  
</pre>

# 效果展示
Video http://pan.baidu.com/s/1miyPn76 </br>
其中，球具备和边界的碰撞检测和每个球之间的碰撞</br></br>
<img src="http://images2015.cnblogs.com/blog/690927/201701/690927-20170122232010785-141851348.jpg" width = "400" height = "650" alt="下雨"/>
<img src="http://images2015.cnblogs.com/blog/690927/201701/690927-20170122232045348-1505543503.jpg" width = "400" height = "650" alt="下雨"/>
<img src="http://images2015.cnblogs.com/blog/690927/201701/690927-20170122232112754-817148735.jpg" width = "400" height = "650" alt="下雨"/>
<img src="http://images2015.cnblogs.com/blog/690927/201701/690927-20170122232134566-1969545427.jpg" width = "400" height = "650" alt="下雨"/>
<img src="http://images.cnblogs.com/cnblogs_com/linguanh/1076053/o_psb.png" width = "400" height = "650" alt="粒子变化"/>
<img src="http://images.cnblogs.com/cnblogs_com/linguanh/1076053/o_Screenshot_20170909-220734.png" width = "400" height = "650" alt="粒子变化"/>

```java
// 粒子变幻隧道
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

```

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

