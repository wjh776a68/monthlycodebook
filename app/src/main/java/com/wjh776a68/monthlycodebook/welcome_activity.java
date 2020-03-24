package com.wjh776a68.monthlycodebook;


import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class welcome_activity extends AppCompatActivity implements Animation.AnimationListener {

    //声名 ImageView id
    private ImageView welcomeImg;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_welcome);

        welcomeImg = (ImageView) findViewById(R.id.welcomeImg);

        //设置动画时间 结束 后跳转到指定页面
        final Intent it = new Intent(this, ScrollingActivity.class); //你要转向的Activity
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                startActivity(it); //执行
            }
        };
        timer.schedule(task, 1500); //1.5秒后跳转
        task= new TimerTask() {
            @Override
            public void run() {
                finish(); //执行
            }
        };
        timer.schedule(task, 1500); //1.5秒后跳转
    }

    @Override
    public  void  onAnimationStart (Animation animation)  {

    }
    @Override
    public  void  onAnimationEnd (Animation animation) {
    }
    @Override
    public  void  onAnimationRepeat (Animation animation) {
    }
}