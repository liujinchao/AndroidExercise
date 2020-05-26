package com.liujc.androidexercise;

import android.app.Application;
import android.util.Log;

import com.android.toolkits.methodtime.TraceAspect;
import com.android.toolkits.methodtime.TrackCallBack;
import com.android.toolkits.methodtime.TrackPoint;

/**
 * @author liujc
 * @date 2020/3/10
 * @Description (这里用一句话描述这个类的作用)
 */
public class MyApplication extends Application {

    private static final String TAG = TraceAspect.class.getSimpleName();

    private static MyApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initMethodTime();
    }

    public static MyApplication getInstance(){
        return instance;
    }

    private void initMethodTime() {
        TrackPoint.init(new TrackCallBack() {
            @Override
            public void onClick(String pageName, String viewIdName) {
                Log.d(TAG, "onClick:" + pageName + "-" + viewIdName);
            }

            @Override
            public void onPageOpen(String pageName) {
                Log.d(TAG, "onPageOpen:" + pageName);
            }

            @Override
            public void onPageClose(String pageName) {
                Log.d(TAG, "onPageClose:" + pageName);
            }
        });
    }

}

