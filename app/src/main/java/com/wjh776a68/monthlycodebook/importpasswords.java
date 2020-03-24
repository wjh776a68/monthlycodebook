package com.wjh776a68.monthlycodebook;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class importpasswords extends AppCompatActivity {
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            Uri uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
            if ("audio/".equals(type)) {
                // 处理发送来音频
               // ToastUtils.showToast(getContext(),"");
            } else if (type.startsWith("text/plain")) {
                // 处理发送来的视频
                Log.v("received","get");
            } else if (type.startsWith("*/")) {
                //处理发送过来的其他文件

            }
        }

    }


}
