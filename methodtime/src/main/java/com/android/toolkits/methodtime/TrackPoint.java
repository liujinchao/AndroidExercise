package com.android.toolkits.methodtime;

/**
 * @author: liujc
 * @date: 2020/3/16
 * @Description: TODO(这里用一句话描述这个类的作用)
 */
public class TrackPoint {

    private static TrackCallBack mTrackCallBack;

    private TrackPoint() {

    }

    /**
     * 初始化
     * @param trackCallBack
     */
    public static void init(TrackCallBack trackCallBack) {
        mTrackCallBack = trackCallBack;
    }

    static void onClick(String pageName, String viewIdName) {
        if (mTrackCallBack == null) {
            return;
        }
        mTrackCallBack.onClick(pageName, viewIdName);
    }

    static void onPageOpen(String pageName) {
        if (mTrackCallBack == null) {
            return;
        }
        mTrackCallBack.onPageOpen(pageName);
    }

    static void onPageClose(String pageName) {
        if (mTrackCallBack == null) {
            return;
        }
        mTrackCallBack.onPageClose(pageName);
    }

}
