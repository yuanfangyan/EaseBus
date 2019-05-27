package com.yuan.easebus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.yuan.buslibrary.Subscrible;
import com.yuan.buslibrary.ThreadMode;
import com.yuan.buslibrary.EaseBus;

public class TwoActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void send(View view) {
        EaseBus.getDefault().post(new Test("qwr", "123"));
    }

}
