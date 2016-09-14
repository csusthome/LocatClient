package com.myy.locatclient.application;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.content.Context;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.ms.utils.bean.Entity;
import com.ms.utils.bean.Fencing;
import com.ms.utils.bean.Pupillus;
import com.ms.utils.bean.WhiteNum;
import com.myy.locatclient.adapter.LocatStateLvAdapter;
import com.myy.locatclient.listen.MyLocationListener;
import com.myy.locatclient.thread.UpLoadTraceThread;
import com.myy.locatclient.utils.LogUtils;

public class LocationClientApplication extends Application {
	
	
	public static boolean DEBUG = false;
	public LocationClient mLocationClient;
	public MyLocationListener mMyLocationListener;
	//上传数据的线程
	public UpLoadTraceThread uploadThread;
	//显示ListView的适配器
//	public LocatStateLvAdapter state_adpter = null;
	public Context showContext = null;
	//定位配置
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
    private String tempcoor="bd09ll";
    private int span = 1000;
    
    public static List<WhiteNum> whites = null;
	public static String CHECK_CODE = null;
	public static String IMEI = null;
	public static Pupillus pup;
	public static List<Entity> entities = new ArrayList<Entity>();
	public static List<Fencing> fencings = new ArrayList<Fencing>();
    
	@Override
	public void onCreate() {
		super.onCreate();
		initLocatServer();
	}
	
	/**
	 * 设置信息列表的数据适配器，用于给监听器更新信息
	 */
	public void setShowListViewAdapter(LocatStateLvAdapter state_adpter)
	{
		mMyLocationListener.setShowListViewAdapter(state_adpter);
	}
	
	/**
	 * 初始化定位服务
	 */
	private void initLocatServer()
	{
//		startLocatServer();
		uploadThread = getUpLoadThread();
		mLocationClient = new LocationClient(this);
		LogUtils.i("UploadThread","init creat "+uploadThread.toString()+
				" "+uploadThread.isAlive());
		mMyLocationListener = new MyLocationListener(this,uploadThread);
		mLocationClient.registerLocationListener(mMyLocationListener);
		
		LocationClientOption option = new LocationClientOption();
        option.setLocationMode(tempMode);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType(tempcoor);//可选，默认gcj02，设置返回的定位结果坐标系，
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mLocationClient.setLocOption(option);
        
	}
	
	private UpLoadTraceThread getUpLoadThread()
	{
		UpLoadTraceThread thread = new UpLoadTraceThread();
		if(LocationClientApplication.fencings!=null
				&&LocationClientApplication.fencings.size()>0)
		{
			thread.bindFences(LocationClientApplication.fencings);
		}
		return thread;
	}
	
	public void startLocatServer()
	{
		if(uploadThread==null||uploadThread.hasStoped()==true)
		{
			uploadThread = getUpLoadThread();
			if(mMyLocationListener!=null)
			{
				//重置上传线程
				mMyLocationListener.resetUpLoadThread(uploadThread);
			}
			LogUtils.i("UploadThread","creat "+uploadThread.toString()+
					" "+uploadThread.isAlive());
		}
		if(mLocationClient==null)
		{
			mLocationClient = new LocationClient(this);
		}
		if(uploadThread.isAlive()==false)
		{
			uploadThread.start();
		}
		if(mLocationClient.isStarted()==false)
		{
			if(DEBUG==false)
			{
				//虚拟机上没有相关的类，无法进行定位，开启会报错
				mLocationClient.start();
			}
		}
		
	}
	
	public void stopLoactServer()
	{
		if(mLocationClient!=null&&uploadThread!=null&&uploadThread.isAlive())
		{
			mLocationClient.stop();
			//暂停线程而不是关掉线程
			uploadThread.waitThread();
//			uploadThread.stopThread();
		}
	}
	
	
}
