package com.myy.locatclient.thread;

import com.ms.utils.DataExchangeUtils;
import com.ms.utils.bean.FencingRecord;
import com.myy.locatparent.common.AbstractTask;
import com.myy.locatparent.common.LoadingPopupWindow;

/**
 * 添加危机情报
 * 
 * @author lenovo-Myy
 * singleTask
 */
public class AddFenceRecoredTask extends AbstractTask {

	private static final boolean DEBUG = true;
	private FencingRecord record = null;
	private LoadingPopupWindow loading_pop;
	private Exception e;
	private String str_result;

	public AddFenceRecoredTask(FencingRecord record) {
		setTaskName("添加围栏警报");
		this.record =record;
	}
	
	@Override
	protected boolean process() throws Exception {
		if(record!=null)
		{
			str_result = DataExchangeUtils.addFencingRecord(record.getDate()
					,record.getFencing().getId(),record.getMessage());
		}
		if(str_result.equals("false"))
		{
			return false;
		}
		return true;
	}

	@Override
	protected void doInSuccess() {
	}

	@Override
	protected void doInFail() {
	}

}
