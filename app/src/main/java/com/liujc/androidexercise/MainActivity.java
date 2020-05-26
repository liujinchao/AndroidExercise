package com.liujc.androidexercise;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.toolkits.methodtime.ExecTime;
import com.liujc.androidexercise.fileupload.FileUploadActivity;
import com.liujc.maplib.MapActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    private ComponentName defaultComponent;
    private ComponentName testComponent;
    private PackageManager packageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initTestIcon();
        initView();
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
                break;
            default:
                break;
        }
    }

    @ExecTime
    public void jumpToTarget(Class target){
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

    private void initTestIcon() {
//        //拿到当前activity注册的组件名称
//        ComponentName componentName = getComponentName();

        //拿到我们注册的MainActivity组件
        defaultComponent = new ComponentName(this, "com.liujc.androidexercise.MainActivity");  //拿到默认的组件
        //拿到我注册的别名test组件
        testComponent = new ComponentName(this, "com.liujc.androidexercise.test");

        packageManager = getApplicationContext().getPackageManager();

    }

    private void initView() {
        Button btnOne=findViewById(R.id.btn_one);
        btnOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeIcon(view);
            }
        });
        Button btnTwo=findViewById(R.id.btn_two);
        btnTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDefaultIcon(view);
            }
        });
    }

    @ExecTime
    public void changeIcon(View view) {
        disableComponent(defaultComponent);
        enableComponent(testComponent);
    }

    @ExecTime
    public void changeDefaultIcon(View view) {
        enableComponent(defaultComponent);
        disableComponent(testComponent);
    }

    /**
     * 启用组件
     *
     * @param componentName
     */
    private void enableComponent(ComponentName componentName) {
        int state = packageManager.getComponentEnabledSetting(componentName);
        if (state == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
            //已经启用
            return;
        }
        packageManager.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    /**
     * 禁用组件
     *
     * @param componentName
     */
    private void disableComponent(ComponentName componentName) {
        int state = packageManager.getComponentEnabledSetting(componentName);
        if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
            //已经禁用
            return;
        }
        packageManager.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

}
