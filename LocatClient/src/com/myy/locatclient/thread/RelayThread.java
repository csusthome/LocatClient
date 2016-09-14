package com.myy.locatclient.thread;

import java.util.concurrent.atomic.AtomicInteger;

import android.os.Handler;

/**
 * ����ʵ�����߳�������ViewPager���Զ�����
 * @author lenovo-Myy
 *
 */
public class RelayThread implements Runnable {

	//5��
	private static final int duration = 5000;
	private boolean key = true;
	//���̵߳�Handler
	private Handler handler = null;
	private AtomicInteger int_pos = null;
	//ViewPager�Ĵ�С
	private int len = 0;
	//�Ƿ����ִ�У������ⲿ�����Ƿ��ڼ�ʱ�׶�,boolean.Boolean�޷�ʹ�ã���Ϊһ���ǻ���
	//����һ��û��set������ֻ�ṩ���߷���
	private Integer is_continue = null;
	
	public RelayThread(Handler handler,AtomicInteger pos,int len,Integer is_continue)
	{
		this.handler = handler;
		int_pos = pos;
		this.len = len;
		this.is_continue = is_continue;
	}
	
	@Override
	public void run() {
		while(key)
		{
			if(is_continue.intValue()==1)
			{
				try{
					//����5��
					Thread.sleep(duration);
				}catch(Exception e)
				{
					e.printStackTrace();
				}
				//���߹��������״̬�ı�������´�ѭ��
				if(is_continue.intValue()==1)
				{
					//ViewPager�±���ǰ�ƶ�һλ
					processPos();
					//������Ϣ����ViewPager
					handler.sendEmptyMessage(int_pos.get());
				}
			}
		}

	}
	
	/**
	 * ViewPager�±���ǰ�ƶ�һλ
	 * @return
	 */
	private int processPos()
	{
		if(int_pos.get()+1>=len)
		{
			int_pos.set(0);
		}
		else
		{
			int_pos.addAndGet(1);
		}
		return int_pos.get();
	}
	
	public void stop()
	{
		key = false;
	}

}

