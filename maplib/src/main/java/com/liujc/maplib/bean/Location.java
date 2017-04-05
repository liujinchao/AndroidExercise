package com.liujc.maplib.bean;

import java.io.Serializable;

/**
 * 类名称：Location
 * 创建者：Create by liujc
 * 创建时间：Create on 2017/4/5 13:44
 * 描述：TODO
 */
public class Location implements Serializable{
	private double lat;
	private double lng;
	private String address;
	public Location() {
		super();
	}
	public Location(double lat, double lng) {
		super();
		this.lat = lat;
		this.lng = lng;
	}
	public Location(double lat, double lng, String address) {
		super();
		this.lat = lat;
		this.lng = lng;
		this.address = address;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getStringLatLng(){
		return lat+","+lng;
	}
	
	public String getStringLngLat(){
		return lng+","+lat;
	}
	
	@Override
	public String toString() {
		return "Location [lat=" + lat + ", lng=" + lng + ", address=" + address
				+ "]";
	}
}
