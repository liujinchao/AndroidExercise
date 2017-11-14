package com.liujc.androidexercise.h5;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.liujc.androidexercise.R;

import static android.content.ContentValues.TAG;
import static com.liujc.androidexercise.R.id.webview;

/**
 * 类名称：JsCallNativeActivity
 * 创建者：Create by liujc
 * 创建时间：Create on 2017/9/16 11:24
 * 描述：TODO
 */

public class JsCallNativeActivity extends Activity {
    private WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_js_call_native);
        initWebView();
        initView();
    }

    private void initView() {
        findViewById(R.id.native_call_js).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
               //当出入变量名时，需要用转义符隔开
                String content="hello js";
                mWebView.loadUrl("javascript:alertMessage(\""   +content+   "\")"   );

                //Android调用有返回值js方法，安卓4.4以上才能用这个方法
                mWebView.evaluateJavascript("sum(1,2)", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        Log.d(TAG, "js返回的结果为=" + value);
                        Toast.makeText(JsCallNativeActivity.this,"js返回的结果为=" + value,Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void initWebView() {
        mWebView = (WebView) findViewById(webview);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true); //允许在WebView中使用js
        JsMethod m = new JsMethod(this, mWebView);
        //其实就是告诉js，我提供给哪个对象给你调用，这样js就可以调用对象里面的方法
        //第二个参数就是该类中的字符串常量
        mWebView.addJavascriptInterface(m, JsMethod.JAVAINTERFACE);
        mWebView.loadUrl("file:///android_asset/index.html");
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());
    }
}
