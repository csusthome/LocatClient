package com.myy.locatclient.listen;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.ms.utils.bean.Point;
import com.ms.utils.bean.Pupillus;
import com.myy.locatclient.activity.MainActivity;
import com.myy.locatclient.adapter.LocatStateLvAdapter;
import com.myy.locatclient.adapter.LocatStateLvAdapter.ChildItem;
import com.myy.locatclient.application.LocationClientApplication;
import com.myy.locatclient.thread.LoginTask;
import com.myy.locatclient.thread.UpLoadTraceThread;

public class MyLocationListener implements BDLocationListener {

	public static String DATEFOMAT ="yyyy-MM-dd HH:mm:ss" ;
	//显示ListView的适配器
	private LocatStateLvAdapter listAdpter = null;
	private Context context = null;
	private  UpLoadTraceThread uploadThread;
	
	public MyLocationListener(Context context,UpLoadTraceThread uploadThread)
	{
		this.context = context;
		this.uploadThread = uploadThread;
	}
	

	/**
	 * 因为上传线程关闭后就不能再用了，需要实例化一个线程
	 */
	public void resetUpLoadThread(UpLoadTraceThread thread)
	{
		uploadThread = thread;
	}
	/**
	 * 设置信息列表的数据适配器，用于给监听器更新信息
	 */
	public void setShowListViewAdapter(LocatStateLvAdapter state_adpter)
	{
		this.listAdpter = state_adpter;
	}
	
	@Override
	public void onReceiveLocation(BDLocation location) {
		// TODO Auto-generated method stub
		ArrayList<LocatStateLvAdapter.ChildItem> list = new ArrayList<LocatStateLvAdapter.ChildItem>();
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat(DATEFOMAT);
		String str_date = sdf.format(date);
		list.add(new ChildItem("时间",str_date));
		list.add(new ChildItem("经度",location.getLatitude()+""));
		list.add(new ChildItem("纬度",location.getLongitude()+""));
		list.add(new ChildItem("radius",location.getRadius()+""));
		
		if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
			list.add(new ChildItem("速度",location.getSpeed()+""));
			list.add(new ChildItem("satellite",location.getSatelliteNumber()+""));
			list.add(new ChildItem("海拔",location.getAltitude()+""));
			list.add(new ChildItem("位置",location.getAddrStr()+""));
			list.add(new ChildItem("回调信息","gps定位成功"));

		} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
			list.add(new ChildItem("位置",location.getAddrStr()));
//			list.add(new ChildItem("运营商信息",location.getOperators()+""));
			list.add(new ChildItem("回调信息","网络定位成功"));
		} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
			list.add(new ChildItem("回调信息","离线定位成功"));
		} else if (location.getLocType() == BDLocation.TypeServerError) {
			list.add(new ChildItem("回调信息","服务端网络定位失败，"+
		"可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因"));
		} else if (location.getLocType() == BDLocation.TypeNetWorkException) {
			list.add(new ChildItem("回调信息","网络不同导致定位失败，"+
		"请检查网络是否通畅"));
		} else if (location.getLocType() == BDLocation.TypeCriteriaException) {
			list.add(new ChildItem("回调信息","无法获取有效定位依据导致定位失败，"+
		"一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机"));
		}
		list.add(new ChildItem("位置语义化信息",location.getLocationDescribe()+""));
		showMsg(list);
		Pupillus curPup = LocationClientApplication.pup;
		if(curPup==null)
		{
			MainActivity.showMessage("异常,子端登录失败，当前定位信息无效");
			Pupillus pup = new Pupillus();
			pup.setMeid(LocationClientApplication.IMEI);
			pup.setVerification_code(LocationClientApplication.CHECK_CODE);
			LoginTask task =LoginTask.getInstace(pup);
			if(task==null)
			{
				return;
			}
			task.execute();
			//若登录未成功先跳过
			return ;
		}
		
		Point p = new Point();
		p.setLatitude(location.getLatitude());
		p.setLongitude(location.getLongitude());
		Date date1 = null;
		try {
			date1 = sdf.parse(str_date);
			p.setDate(date1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		p.setPupillus(curPup);
		uploadPoints(p);
	}
	
	private void showMsg(ArrayList<LocatStateLvAdapter.ChildItem> list)
	{
		if(listAdpter==null)
		{
			return ;
		}
		else
		{
			listAdpter.updateData(list);
		}
	}
	
	private void uploadPoints(Point p)
	{
		if(uploadThread!=null)
		{
			uploadThread.addPoint(p);
		}
	}
}
