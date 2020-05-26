package com.android.toolkits.methodtime;

/**
 * @author: liujc
 * @date: 2020/3/16
 * @Description: TODO(这里用一句话描述这个类的作用)
 */
public interface TrackCallBack {

    /**
     * 当View被点击
     *
     * @param pageName
     * @param viewIdName
     */
    void onClick(String pageName, String viewIdName);

    /**
     * 当页面打开时
     *
     * @param pageName
     */
    void onPageOpen(String pageName);

    /**
     * 当页面关闭时
     *
     * @param pageName
     */
    void onPageClose(String pageName);

}
