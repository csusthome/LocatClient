package com.myy.locatparent.common;

import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

import android.os.AsyncTask;

import com.myy.locatclient.utils.LogUtils;

/**
 * 异步任务基类,可以设置同时执行同一任务的最大上限,还封装了其他基本逻辑
 * ps:因为这个基类是后面打算重用代码才做的，而改之前的Task工作量太大
 * 所以有部分就没改了，sorry
 * @author lenovo-Myy
 *
 */
public abstract  class AbstractTask extends AsyncTask<Object, String, Boolean> {
	
	protected String taskName = "任务执行"; 
	private LoadingPopupWindow loading_pop;
	private Exception e;
	private static Integer TASK_NUM=0;
	private static int MAX_TASKNUM = 1;
	//用于记录任务执行结果
	private boolean bool_result=false;

	public final void bindLoadingPopupWindow(LoadingPopupWindow window)
	{
		loading_pop = window;
	}
	
	/*
	 * 返回任务实例
	 */
//	public static <E extends AbstrctTask> E getInstance(Class<E> clazz)
//	{
//		synchronized (TASK_NUM) {
//			if(TASK_NUM<=MAX_TASKNUM)
//			{
//				TASK_NUM++;
//				try {
//					return clazz.newInstance();
//				} catch (Exception e) {
//					e.printStackTrace();
//				} 
//			}
//			return null;
//		}
//	}
	
	
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if(loading_pop!=null)
		{
			loading_pop.setText(taskName+"中..");
		}
	}

	protected final void setTaskName(String name)
	{
		this.taskName = name;
	}
	
	/**
	 * 设置同时能够运行的任务的最大上限数
	 * @param max 上限数 1及1以上
	 */
	public final void setMaxTaskCount(int max)
	{
		if(max>=1)
		{
			MAX_TASKNUM = max;
		}
		else
		{
			throw new IllegalStateException("Task max run num can not blow 1.");
		}
	}
	
	@Override	
	protected Boolean doInBackground(Object... params) {
		boolean flag = false;
		//检测是否达到任务上限
		synchronized (TASK_NUM) {
			if(TASK_NUM<MAX_TASKNUM)
			{
				TASK_NUM++;
				flag = true;
			}
		}
		if(flag==true)
		{
			try {
				bool_result = process();
			} catch (Exception e) {
				this.e = e;
				e.printStackTrace();
			}
		}
		else
		{
			LogUtils.i(AbstractTask.class.getName(),this.taskName+"任务执行达到上限:"+TASK_NUM);
		}
		return bool_result;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		synchronized(TASK_NUM)
		{
			TASK_NUM--;
		}
		if (e != null) {
			if (e instanceof TimeoutException
					|| e instanceof SocketTimeoutException) {
				LogUtils.i(AbstractTask.class.getName(),taskName+"异常：连接服务器超时，请检查当前网络配置");
//				MainActivity.showMessage(taskName+"异常：连接服务器超时，请检查当前网络配置");
			} else {
//				MainActivity.showMessage();
				LogUtils.i(AbstractTask.class.getName(),taskName+"异常：" + e.getMessage());
			}
			if (loading_pop != null && loading_pop.isShowing()) {
				loading_pop.dismiss();
			}
			return;
		}
		if (this.bool_result==false) {
			if (loading_pop != null) {
				loading_pop.dismiss();
			}
			doInFail();
			LogUtils.i(AbstractTask.class.getName(),taskName+"失败");
//			MainActivity.showMessage(taskName+"失败");
		} else {
			if (loading_pop != null) {
				loading_pop.dismiss();
			}
			doInSuccess();
//			MainActivity.showMessage(taskName+"成功");
			LogUtils.i(AbstractTask.class.getName(),taskName+"成功");
		}
	}
	/**
	 * 需要异步执行的线程在这里写
	 * @return 任务成功返回true
	 */
	protected abstract boolean process()throws Exception;
	
	/**
	 * 当任务执行成功,在主线程运行
	 */
	protected abstract void doInSuccess();
	/**
	 * 当任务执行失败后执行,在主线程运行
	 */
	protected abstract void doInFail();
	
	
}
