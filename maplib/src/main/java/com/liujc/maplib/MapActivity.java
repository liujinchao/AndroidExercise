package com.liujc.maplib;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.liujc.maplib.bean.Location;
import com.liujc.maplib.utils.LocationService;
import com.liujc.maplib.utils.NativeDialog;


public class MapActivity extends Activity implements OnClickListener {
	
	private Location loc_now = new Location(31.247228, 121.455805, "s");
	private Location loc_end = new Location(31.239658, 121.499716, "e");
	private LocationService locationService;
	private TextView LocationResult;
	private Button startLocation;
	double latitude;
	double longitude;
	String city;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		findViewById(R.id.other).setOnClickListener(this);
		LocationResult = (TextView) findViewById(R.id.textView1);
		LocationResult.setMovementMethod(ScrollingMovementMethod.getInstance());
		startLocation = (Button) findViewById(R.id.addfence);
	}

	@Override
	public void onClick(View v) {
		NativeDialog msgDialog = new NativeDialog(this, loc_now, loc_end);
		msgDialog.show();
	}


	/***
	 * Stop location service
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		locationService.unregisterListener(mListener); //注销掉监听
		locationService.stop(); //停止定位服务
		super.onStop();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// -----------location config ------------
		locationService = new LocationService(getApplicationContext());
		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		SDKInitializer.initialize(getApplicationContext());
		//获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
		locationService.registerListener(mListener);
		//注册监听
		int type = getIntent().getIntExtra("from", 0);
		if (type == 0) {
			locationService.setLocationOption(locationService.getDefaultLocationClientOption());
		} else if (type == 1) {
			locationService.setLocationOption(locationService.getOption());
		}
		startLocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (startLocation.getText().toString().equals(getString(R.string.startlocation))) {
					locationService.start();// 定位SDK
					// start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
					startLocation.setText(getString(R.string.stoplocation));
				} else {
					locationService.stop();
					startLocation.setText(getString(R.string.startlocation));
				}
			}
		});
	}


	/*****
	 * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
	 *
	 */
	private BDLocationListener mListener = new BDLocationListener() {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
			if (null != location && location.getLocType() != BDLocation.TypeServerError) {
				StringBuffer sb = new StringBuffer(256);
				latitude = location.getLatitude();
				longitude = location.getLongitude();
				city = location.getCity();
				sb.append("time : ");
				/**
				 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
				 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
				 */
				sb.append(location.getTime());
				sb.append("\nerror code : ");
				sb.append(location.getLocType());
				sb.append("\nlatitude : ");
				sb.append(location.getLatitude());
				sb.append("\nlontitude : ");
				sb.append(location.getLongitude());
				sb.append("\nradius : ");
				sb.append(location.getRadius());
				sb.append("\nCountryCode : ");
				sb.append(location.getCountryCode());
				sb.append("\nCountry : ");
				sb.append(location.getCountry());
				sb.append("\ncitycode : ");
				sb.append(location.getCityCode());
				sb.append("\ncity : ");
				sb.append(location.getCity());
				sb.append("\nDistrict : ");
				sb.append(location.getDistrict());
				sb.append("\nStreet : ");
				sb.append(location.getStreet());
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				sb.append("\nDescribe: ");
				sb.append(location.getLocationDescribe());
				sb.append("\nDirection(not all devices have value): ");
				sb.append(location.getDirection());
				sb.append("\nPoi: ");
				if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
					for (int i = 0; i < location.getPoiList().size(); i++) {
						Poi poi = (Poi) location.getPoiList().get(i);
						sb.append(poi.getName() + ";");
					}
				}
				if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
					sb.append("\nspeed : ");
					sb.append(location.getSpeed());// 单位：km/h
					sb.append("\nsatellite : ");
					sb.append(location.getSatelliteNumber());
					sb.append("\nheight : ");
					sb.append(location.getAltitude());// 单位：米
					sb.append("\ndescribe : ");
					sb.append("gps定位成功");
				} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
					// 运营商信息
					sb.append("\noperationers : ");
					sb.append(location.getOperators());
					sb.append("\ndescribe : ");
					sb.append("网络定位成功");
				} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
					sb.append("\ndescribe : ");
					sb.append("离线定位成功，离线定位结果也是有效的");
				} else if (location.getLocType() == BDLocation.TypeServerError) {
					sb.append("\ndescribe : ");
					sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
				} else if (location.getLocType() == BDLocation.TypeNetWorkException) {
					sb.append("\ndescribe : ");
					sb.append("网络不同导致定位失败，请检查网络是否通畅");
				} else if (location.getLocType() == BDLocation.TypeCriteriaException) {
					sb.append("\ndescribe : ");
					sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
				}
				logMsg(sb.toString());
			}
		}

	};
	/**
	 * 显示请求字符串
	 *
	 * @param str
	 */
	public void logMsg(String str) {
		try {
			if (LocationResult != null)
				LocationResult.setText(str);
			loc_now = new Location(latitude,longitude, "s");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
