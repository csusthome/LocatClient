package com.myy.locatclient.thread;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import com.ms.utils.DataExchangeUtils;
import com.ms.utils.bean.Pupillus;
import com.ms.utils.bean.WhiteNum;
import com.myy.locatclient.activity.MainActivity;
import com.myy.locatclient.application.LocationClientApplication;
import com.myy.locatparent.common.AbstractTask;

/**
 * ��ѯ�Ӷ˰������б����񣬲��ܵ�������(δ���)
 * @author lenovo-Myy
 *
 */
public class QueryWhiteTask extends AbstractTask {

	private List<WhiteNum> white_list = null;
	private Pupillus pup;
	
	public QueryWhiteTask(Pupillus pup) {
		setTaskName("��ѯ������");
		this.pup = pup;
	}
		
	@Override
	protected boolean process() throws ClientProtocolException, IOException {
		white_list = DataExchangeUtils.getWhiteList(pup);
		if(white_list!=null)
		{
			return true;
		}
		return false;
	}
	
	@Override
	protected void doInSuccess() {
		if(white_list.size()==0)
		{
			MainActivity.showMessage(taskName+"��������");
		}
		MainActivity.updateWhiteList(white_list);
	}
	
	@Override
	protected void doInFail() {
		
	}
}
