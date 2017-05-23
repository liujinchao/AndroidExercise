package com.liujc.androidexercise;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.liujc.androidexercise.fileupload.FileUploadActivity;
import com.liujc.maplib.MapActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
    @OnClick({R.id.btn_map,R.id.btn_sencond,R.id.file_upload})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.file_upload:
                jumpToTarget(FileUploadActivity.class);
                break;
            case R.id.btn_map:
                jumpToTarget(MapActivity.class);
                break;
            case R.id.btn_sencond:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("TAG",doGetTest());
                    }
                }).start();

//                doGetUser();
//                Linkify.addLinks(textView, Linkify.PHONE_NUMBERS);
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

    private String doGetTest(){
        String path = "http://192.168.1.52:8080/login/helloWorld";
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200){
                InputStream stream = conn.getInputStream();

                byte[] buffer = new byte[2048];
                int readBytes = 0;
                StringBuilder stringBuilder = new StringBuilder();
                while((readBytes = stream.read(buffer)) > 0){
                    stringBuilder.append(new String(buffer, 0, readBytes));
                }
                return  stringBuilder.toString();
            }else {
                return conn.getResponseCode()+"";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String doGetUser(){
        String path = "http://192.168.1.52:8080/JavaWeb/HelloWorld?id=111111";
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200){
                InputStream stream = conn.getInputStream();

                byte[] buffer = new byte[2048];
                int readBytes = 0;
                StringBuilder stringBuilder = new StringBuilder();
                while((readBytes = stream.read(buffer)) > 0){
                    stringBuilder.append(new String(buffer, 0, readBytes));
                }
                return  stringBuilder.toString();
            }else {
                return conn.getResponseCode()+"";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
