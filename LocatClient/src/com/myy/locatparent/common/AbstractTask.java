package com.myy.locatparent.common;

import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

import android.os.AsyncTask;

import com.myy.locatclient.utils.LogUtils;

/**
 * �첽�������,��������ͬʱִ��ͬһ������������,����װ�����������߼�
 * ps:��Ϊ��������Ǻ���������ô�������ģ�����֮ǰ��Task������̫��
 * �����в��־�û���ˣ�sorry
 * @author lenovo-Myy
 *
 */
public abstract  class AbstractTask extends AsyncTask<Object, String, Boolean> {
	
	protected String taskName = "����ִ��"; 
	private LoadingPopupWindow loading_pop;
	private Exception e;
	private static Integer TASK_NUM=0;
	private static int MAX_TASKNUM = 1;
	//���ڼ�¼����ִ�н��
	private boolean bool_result=false;

	public final void bindLoadingPopupWindow(LoadingPopupWindow window)
	{
		loading_pop = window;
	}
	
	/*
	 * ��������ʵ��
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
			loading_pop.setText(taskName+"��..");
		}
	}

	protected final void setTaskName(String name)
	{
		this.taskName = name;
	}
	
	/**
	 * ����ͬʱ�ܹ����е���������������
	 * @param max ������ 1��1����
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
		//����Ƿ�ﵽ��������
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
			LogUtils.i(AbstractTask.class.getName(),this.taskName+"����ִ�дﵽ����:"+TASK_NUM);
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
				LogUtils.i(AbstractTask.class.getName(),taskName+"�쳣�����ӷ�������ʱ�����鵱ǰ��������");
//				MainActivity.showMessage(taskName+"�쳣�����ӷ�������ʱ�����鵱ǰ��������");
			} else {
//				MainActivity.showMessage();
				LogUtils.i(AbstractTask.class.getName(),taskName+"�쳣��" + e.getMessage());
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
			LogUtils.i(AbstractTask.class.getName(),taskName+"ʧ��");
//			MainActivity.showMessage(taskName+"ʧ��");
		} else {
			if (loading_pop != null) {
				loading_pop.dismiss();
			}
			doInSuccess();
//			MainActivity.showMessage(taskName+"�ɹ�");
			LogUtils.i(AbstractTask.class.getName(),taskName+"�ɹ�");
		}
	}
	/**
	 * ��Ҫ�첽ִ�е��߳�������д
	 * @return ����ɹ�����true
	 */
	protected abstract boolean process()throws Exception;
	
	/**
	 * ������ִ�гɹ�,�����߳�����
	 */
	protected abstract void doInSuccess();
	/**
	 * ������ִ��ʧ�ܺ�ִ��,�����߳�����
	 */
	protected abstract void doInFail();
	
	
}
