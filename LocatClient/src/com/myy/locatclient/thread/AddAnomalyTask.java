package com.myy.locatclient.thread;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

import android.os.AsyncTask;

import com.ms.utils.DataExchangeUtils;
import com.ms.utils.bean.AnomalyRecord;
import com.myy.locatclient.activity.MainActivity;
import com.myy.locatclient.utils.LogUtils;
import com.myy.locatparent.common.LoadingPopupWindow;

/**
 * ���Σ���鱨
 * 
 * @author lenovo-Myy
 * singleTask
 */
public class AddAnomalyTask extends AsyncTask<Object, String, Boolean> {

	private static final boolean DEBUG = true;
	private AnomalyRecord record = null;
	private LoadingPopupWindow loading_pop;
	private Exception e;
	private String str_result;

	public AddAnomalyTask(AnomalyRecord record) {
		this.record =record;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (loading_pop != null) {
			loading_pop.setText("����������");
		}
	}

	@Override
	protected Boolean doInBackground(Object... parpam) {
		try {
			if(record!=null)
			{
				str_result = DataExchangeUtils.addAnomalyRecord(record);
			}
		} catch (IOException e) {
			e.printStackTrace();
			this.e = e;
		}
		return null;
	}

	@Override
	protected void onPostExecute(Boolean result1) {
		super.onPostExecute(result1);
		if (e != null) {
			if (e instanceof TimeoutException
					|| e instanceof SocketTimeoutException) {
				MainActivity.showMessage("����쳣��¼�쳣��" + "���ӷ�������ʱ�����鵱ǰ��������");
			} else {
				MainActivity.showMessage("����쳣��¼�쳣��" + e.getMessage());
			}
			return;
		}
		if (str_result.equals("false")) {
			showLog("����쳣���ʧ�� :"+str_result);
		} else {
			showLog("����쳣����ɹ�:"+str_result);
		}

	}

	private void showLog(String msg)
	{
		LogUtils.i(AddAnomalyTask.class.getName(), msg);
	}

}
