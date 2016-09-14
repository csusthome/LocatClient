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
	//��ʾListView��������
	private LocatStateLvAdapter listAdpter = null;
	private Context context = null;
	private  UpLoadTraceThread uploadThread;
	
	public MyLocationListener(Context context,UpLoadTraceThread uploadThread)
	{
		this.context = context;
		this.uploadThread = uploadThread;
	}
	

	/**
	 * ��Ϊ�ϴ��̹߳رպ�Ͳ��������ˣ���Ҫʵ����һ���߳�
	 */
	public void resetUpLoadThread(UpLoadTraceThread thread)
	{
		uploadThread = thread;
	}
	/**
	 * ������Ϣ�б�����������������ڸ�������������Ϣ
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
		list.add(new ChildItem("ʱ��",str_date));
		list.add(new ChildItem("����",location.getLatitude()+""));
		list.add(new ChildItem("γ��",location.getLongitude()+""));
		list.add(new ChildItem("radius",location.getRadius()+""));
		
		if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS��λ���
			list.add(new ChildItem("�ٶ�",location.getSpeed()+""));
			list.add(new ChildItem("satellite",location.getSatelliteNumber()+""));
			list.add(new ChildItem("����",location.getAltitude()+""));
			list.add(new ChildItem("λ��",location.getAddrStr()+""));
			list.add(new ChildItem("�ص���Ϣ","gps��λ�ɹ�"));

		} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// ���綨λ���
			list.add(new ChildItem("λ��",location.getAddrStr()));
//			list.add(new ChildItem("��Ӫ����Ϣ",location.getOperators()+""));
			list.add(new ChildItem("�ص���Ϣ","���綨λ�ɹ�"));
		} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// ���߶�λ���
			list.add(new ChildItem("�ص���Ϣ","���߶�λ�ɹ�"));
		} else if (location.getLocType() == BDLocation.TypeServerError) {
			list.add(new ChildItem("�ص���Ϣ","��������綨λʧ�ܣ�"+
		"���Է���IMEI�źʹ��嶨λʱ�䵽loc-bugs@baidu.com��������׷��ԭ��"));
		} else if (location.getLocType() == BDLocation.TypeNetWorkException) {
			list.add(new ChildItem("�ص���Ϣ","���粻ͬ���¶�λʧ�ܣ�"+
		"���������Ƿ�ͨ��"));
		} else if (location.getLocType() == BDLocation.TypeCriteriaException) {
			list.add(new ChildItem("�ص���Ϣ","�޷���ȡ��Ч��λ���ݵ��¶�λʧ�ܣ�"+
		"һ���������ֻ���ԭ�򣬴��ڷ���ģʽ��һ���������ֽ�����������������ֻ�"));
		}
		list.add(new ChildItem("λ�����廯��Ϣ",location.getLocationDescribe()+""));
		showMsg(list);
		Pupillus curPup = LocationClientApplication.pup;
		if(curPup==null)
		{
			MainActivity.showMessage("�쳣,�Ӷ˵�¼ʧ�ܣ���ǰ��λ��Ϣ��Ч");
			Pupillus pup = new Pupillus();
			pup.setMeid(LocationClientApplication.IMEI);
			pup.setVerification_code(LocationClientApplication.CHECK_CODE);
			LoginTask task =LoginTask.getInstace(pup);
			if(task==null)
			{
				return;
			}
			task.execute();
			//����¼δ�ɹ�������
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
