package com.lk.myproject.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.lk.myproject.R;
import com.lk.myproject.toast.ToastUtils;

public class ToastActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        TextView textView1 = findViewById(R.id.text);
        textView1.setText("origin toast");
        findViewById(R.id.text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showView();

                Toast.makeText(ToastActivity.this, "origin toast", Toast.LENGTH_LONG).show();
            }
        });
        TextView textView2 = findViewById(R.id.update);
        textView2.setText("safe toast");
        findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showView();

                ToastUtils.showToast(ToastActivity.this, "saft toast .", Toast.LENGTH_LONG);
            }
        });
        findViewById(R.id.text2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    void showView(){
        WindowManager mw = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        TextView tv = new TextView(this);
        tv.setLayoutParams(new WindowManager.LayoutParams(50,50));
        tv.setBackgroundColor(Color.WHITE);
        tv.setText("windowmanager 添加悬浮窗");
        WindowManager.LayoutParams  params =new WindowManager.LayoutParams();
        params.width=50;
        params.height=50;
        params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        mw.addView(tv,params);
    }
}
