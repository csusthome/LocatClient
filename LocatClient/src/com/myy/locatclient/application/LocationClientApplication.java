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
	//�ϴ����ݵ��߳�
	public UpLoadTraceThread uploadThread;
	//��ʾListView��������
//	public LocatStateLvAdapter state_adpter = null;
	public Context showContext = null;
	//��λ����
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
	 * ������Ϣ�б�����������������ڸ�������������Ϣ
	 */
	public void setShowListViewAdapter(LocatStateLvAdapter state_adpter)
	{
		mMyLocationListener.setShowListViewAdapter(state_adpter);
	}
	
	/**
	 * ��ʼ����λ����
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
        option.setLocationMode(tempMode);//��ѡ��Ĭ�ϸ߾��ȣ����ö�λģʽ���߾��ȣ��͹��ģ����豸
        option.setCoorType(tempcoor);//��ѡ��Ĭ��gcj02�����÷��صĶ�λ�������ϵ��
        option.setScanSpan(span);//��ѡ��Ĭ��0��������λһ�Σ����÷���λ����ļ����Ҫ���ڵ���1000ms������Ч��
        option.setIsNeedAddress(true);//��ѡ�������Ƿ���Ҫ��ַ��Ϣ��Ĭ�ϲ���Ҫ
        option.setOpenGps(true);//��ѡ��Ĭ��false,�����Ƿ�ʹ��gps
        option.setLocationNotify(true);//��ѡ��Ĭ��false�������Ƿ�gps��Чʱ����1S1��Ƶ�����GPS���
        option.setIgnoreKillProcess(true);//��ѡ��Ĭ��true����λSDK�ڲ���һ��SERVICE�����ŵ��˶������̣������Ƿ���stop��ʱ��ɱ��������̣�Ĭ�ϲ�ɱ��
        option.setEnableSimulateGps(false);//��ѡ��Ĭ��false�������Ƿ���Ҫ����gps��������Ĭ����Ҫ
        option.setIsNeedLocationDescribe(true);//��ѡ��Ĭ��false�������Ƿ���Ҫλ�����廯�����������BDLocation.getLocationDescribe��õ�����������ڡ��ڱ����찲�Ÿ�����
        option.setIsNeedLocationPoiList(true);//��ѡ��Ĭ��false�������Ƿ���ҪPOI�����������BDLocation.getPoiList��õ�
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
				//�����ϴ��߳�
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
				//�������û����ص��࣬�޷����ж�λ�������ᱨ��
				mLocationClient.start();
			}
		}
		
	}
	
	public void stopLoactServer()
	{
		if(mLocationClient!=null&&uploadThread!=null&&uploadThread.isAlive())
		{
			mLocationClient.stop();
			//��ͣ�̶߳����ǹص��߳�
			uploadThread.waitThread();
//			uploadThread.stopThread();
		}
	}
	
	
}
