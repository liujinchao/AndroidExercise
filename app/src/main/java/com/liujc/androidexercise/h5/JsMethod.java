package com.liujc.androidexercise.h5;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

/**
 * 类名称：JsMethod
 * 创建者：Create by liujc
 * 创建时间：Create on 2017/9/16 11:21
 * 描述：TODO
 */

public class JsMethod {
    public static final String JAVAINTERFACE = "javaInterface";
    private Context mContext;
    private WebView mWebView;
    public JsMethod(Context context, WebView webView) {
        mContext = context;
        mWebView = webView;
    }
    @JavascriptInterface
    public void showToast(String text){
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }
}
