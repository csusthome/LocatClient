package com.myy.locatclient.thread;

import java.util.concurrent.atomic.AtomicInteger;

import android.os.Handler;

/**
 * 用于实现主线程中内容ViewPager的自动滚动
 * @author lenovo-Myy
 *
 */
public class RelayThread implements Runnable {

	//5秒
	private static final int duration = 5000;
	private boolean key = true;
	//主线程的Handler
	private Handler handler = null;
	private AtomicInteger int_pos = null;
	//ViewPager的大小
	private int len = 0;
	//是否继续执行，用于外部控制是否处于计时阶段,boolean.Boolean无法使用，因为一个是基本
	//类型一个没有set方法，只提供工具方法
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
					//休眠5秒
					Thread.sleep(duration);
				}catch(Exception e)
				{
					e.printStackTrace();
				}
				//休眠过程中如果状态改变则进行下次循环
				if(is_continue.intValue()==1)
				{
					//ViewPager下标向前移动一位
					processPos();
					//发送消息更新ViewPager
					handler.sendEmptyMessage(int_pos.get());
				}
			}
		}

	}
	
	/**
	 * ViewPager下标向前移动一位
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

