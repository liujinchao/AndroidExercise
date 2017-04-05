package com.liujc.maplib.bean;

import android.graphics.drawable.Drawable;

/**
 * 类名称：AppInfo
 * 创建者：Create by liujc
 * 创建时间：Create on 2017/4/5 13:42
 * 描述：APP相关信息
 */
public class AppInfo {
	private String appName = "";
	private String packageName = "";
	private String versionName = "";
	private int versionCode = 0;
	private Drawable appIcon = null;
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public int getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}
	public Drawable getAppIcon() {
		return appIcon;
	}
	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
	}
}
