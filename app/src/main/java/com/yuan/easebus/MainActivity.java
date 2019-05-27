package com.yuan.easebus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.yuan.buslibrary.EaseBus;
import com.yuan.buslibrary.Subscrible;
import com.yuan.buslibrary.ThreadMode;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        EaseBus.getDefault().register(this);

    }

    @Subscrible(threadMode = ThreadMode.MAIN)
    public void getMessage(Test test) {
        Log.e("getMessage: ", test.toString());
    }

    public void send(View view) {
        startActivity(new Intent(this, TwoActivity.class));
    }
}
