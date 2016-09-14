package com.myy.locatclient.reciver;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.baidu.location.LocationClient;
import com.ms.utils.bean.Pupillus;
import com.ms.utils.bean.WhiteNum;
import com.myy.locatclient.application.LocationClientApplication;
import com.myy.locatclient.thread.QueryWhiteTask;
import com.myy.locatclient.utils.LogUtils;

public class PhoneStatReceiver extends BroadcastReceiver {

	private static final String TAG = PhoneStatReceiver.class.getName();
	TelephonyManager teleManager = null;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		teleManager = (TelephonyManager)context.getSystemService(Service.TELEPHONY_SERVICE);
		switch (teleManager.getCallState()) {
		case TelephonyManager.CALL_STATE_RINGING:
			String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
			LogUtils.i(TAG,"Phone call:"+number);
			List<WhiteNum> list_white = getWhiteList();
			//�������˰������Ҳ��ٰ�������
			if(list_white.size()>0&&checkPhoneNum(list_white,number)==false)
			{
				stopCall();
			}
			break;
		}
	}
	
	public List<WhiteNum> getWhiteList()
	{
		List<WhiteNum> list_white =  LocationClientApplication.whites;
//		WhiteNum white = new WhiteNum();
//		white.setNote("����");
//		white.setPhone_number("18776952648");
//		list_white.add(white);
		//��������Ϊ�����ѯ������
		if(list_white==null)
		{
			Pupillus pup = LocationClientApplication.pup;
			QueryWhiteTask task = new QueryWhiteTask(pup);
			task.execute();
			//����һ���յ��б�
			list_white = new ArrayList<WhiteNum>(0);
		}
		return list_white;
	}
	
	public boolean checkPhoneNum(List<WhiteNum> list_white,String phone)
	{
		for(WhiteNum white:list_white)
		{
			if(white.getPhone_number().equals(phone)==true)
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * �Ҷϵ绰
	 */
	public void stopCall()
	{
		 Class<TelephonyManager> c = TelephonyManager.class;           
	        try  
	        {  
	            Method getITelephonyMethod = c.getDeclaredMethod("getITelephony", (Class[]) null);  
	            getITelephonyMethod.setAccessible(true);  
	            ITelephony iTelephony = null;  
	            LogUtils.i(TAG, "End call.");  
	            iTelephony = (ITelephony) getITelephonyMethod.invoke(teleManager,(Object[]) null);  
	            iTelephony.endCall();  
	        }  
	        catch (Exception e)  
	        {  
	            LogUtils.e(TAG, "Fail to answer ring call.");  
	        }          
	}

}
