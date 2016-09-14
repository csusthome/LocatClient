package com.myy.locatclient.thread;

import java.util.List;

import com.ms.utils.DataExchangeUtils;
import com.ms.utils.bean.Entity;
import com.ms.utils.bean.Fencing;
import com.myy.locatclient.application.LocationClientApplication;
import com.myy.locatclient.utils.LogUtils;
import com.myy.locatparent.common.AbstractTask;

public class QueryFenceTask extends AbstractTask {

	private List<Fencing> list_fences ;
	private List<Entity> list_entities;
	
	public QueryFenceTask(List<Entity> entities)
	{
		setTaskName("查询围栏");
		list_entities = entities;
	}
	@Override
	protected boolean process() throws Exception {
		if(list_entities!=null&&list_entities.size()>0)
		{
			list_fences = DataExchangeUtils.getFencings(list_entities);
		}
		if(list_fences!=null)
		{
			return true;
		}
		return false;
	}

	@Override
	protected void doInSuccess() {
		if(list_fences.size()>0)
		{
			LocationClientApplication.fencings = list_fences;
		}
		else
		{
			LogUtils.d(QueryFenceTask.class.getName(),taskName+"数据为空");
		}
	}

	@Override
	protected void doInFail() {
		
	}

}
