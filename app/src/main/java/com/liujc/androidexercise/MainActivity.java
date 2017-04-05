package com.liujc.androidexercise;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.liujc.maplib.MapActivity;
import com.liujc.maplib.utils.APPUtil;

import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

//        findViewById(R.id.btn_map).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                jumpToTarget(MapActivity.class);
//            }
//        });
    }
    @OnClick({R.id.btn_map,R.id.btn_sencond})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_map:
                jumpToTarget(MapActivity.class);
                break;
            case R.id.btn_sencond:
//                APPUtil.
                break;
            default:
                break;
        }
    }

    private void jumpToTarget(Class target){
        Intent intent = new Intent(MainActivity.this, target);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
