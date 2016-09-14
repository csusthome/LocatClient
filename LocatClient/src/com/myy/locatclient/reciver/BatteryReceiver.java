package com.myy.locatclient.reciver;

import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ms.utils.bean.AnomalyRecord;
import com.ms.utils.bean.Pupillus;
import com.myy.locatclient.activity.MainActivity;
import com.myy.locatclient.application.LocationClientApplication;
import com.myy.locatclient.thread.AddAnomalyTask;
import com.myy.locatclient.utils.LogUtils;

public class BatteryReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		//判断它是否是为电量变化的Broadcast Action
		if(Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())){
			//获取当前电量
			int level = intent.getIntExtra("level", 0);
			//电量的总刻度
			int scale = intent.getIntExtra("scale", 100);
			//电量百分比
			int power = (level*100)/scale;
			LogUtils.i(BatteryReceiver.class.getName(),"当前电量 level:"+level+" scale:"+scale
					+" power:"+power);
			if(power<=10)
			{
				AnomalyRecord record = new AnomalyRecord();
				record.setDate(new Date(System.currentTimeMillis()));
				record.setMessage("电量低于20%");
				Pupillus pup = LocationClientApplication.pup;
				if(pup==null)
				{
					return ;
				}
				record.setPupillus(pup);
				AddAnomalyTask task = new AddAnomalyTask(new AnomalyRecord());
				task.execute();
			}
		}
	}
}
