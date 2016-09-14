package com.myy.locatclient.thread;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

import android.os.AsyncTask;

import com.ms.utils.DataExchangeUtils;
import com.ms.utils.bean.Pupillus;
import com.myy.locatclient.activity.MainActivity;
import com.myy.locatclient.application.LocationClientApplication;
import com.myy.locatparent.common.LoadingPopupWindow;

/**
 * 子端注册任务
 * 
 * @author lenovo-Myy
 * 
 */
public class LoginTask extends AsyncTask<Object, String, Boolean> {

	private static final boolean DEBUG = true;
	private Pupillus pup;
	private LoadingPopupWindow loading_pop;
	private Exception e;
	private String str_result;

	private static Integer NUM=0;
	/**
	 * 
	 * @param pup
	 *            需要有IMEI，验证码
	 */
	private  LoginTask(Pupillus pup) {
		this.pup = pup;
	}

	public static synchronized LoginTask getInstace(Pupillus pup)
	{
		if(pup!=null&&NUM<=0)
		{
			NUM++;
			return new LoginTask(pup);
		}
		return null;
	}
	
	public void bindLoadingPopupWindow(LoadingPopupWindow window) {
		loading_pop = window;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (loading_pop != null) {
			loading_pop.setText("网络连接中");
		}
	}

	@Override
	protected Boolean doInBackground(Object... parpam) {
		try {
			String logResult = DataExchangeUtils.pupillusRegister(pup);
			//若注册成功
			if(logResult.equals("true"))
			{
				str_result = DataExchangeUtils.pupillusLogin(pup);
				//登录成功后设置初始信息
				Pupillus curPup = new Pupillus();
				curPup.setId(Integer.parseInt(str_result));
				LocationClientApplication.pup = curPup;
				LocationClientApplication.entities = DataExchangeUtils.
						getEntitiesByPupillus(curPup.getId());
				//根据实体列表查询围栏
				QueryFenceTask task = new QueryFenceTask(LocationClientApplication.entities);
				task.execute();
				
				//登录成功后更新白名单信息
				QueryWhiteTask whiteTask = new QueryWhiteTask(pup);
				whiteTask.execute();
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.e = e;
		}
		return null;
	}

	@Override
	protected void onPostExecute(Boolean result1) {
		super.onPostExecute(result1);
		synchronized(NUM)
		{
			NUM--;
		}
		if (e != null) {
			if (e instanceof TimeoutException
					|| e instanceof SocketTimeoutException) {
				MainActivity.showMessage("登录异常：" + "连接服务器超时，请检查当前网络配置");
			} else {
				MainActivity.showMessage("登录异常：" + e.getMessage());
			}
			if (loading_pop != null && loading_pop.isShowing()) {
				loading_pop.dismiss();
			}
			return;
		}
		if (str_result==null||str_result.equals("false")) {
			if (loading_pop != null) {
				loading_pop.dismiss();
			}
			MainActivity.showMessage("更新信息失败");
		} else {
			if (loading_pop != null) {
				loading_pop.dismiss();
			}
			MainActivity.showMessage("更新成功");
		}

	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

}
