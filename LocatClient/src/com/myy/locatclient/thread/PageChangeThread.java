package com.myy.locatclient.thread;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import android.os.Handler;
import android.support.v4.app.Fragment;

public class PageChangeThread extends Thread {
	// 因为关键的is_continue无法按引用传递，所以只能用内部类实现了
	private static final int duration = 5000;
	// 提供线程安全的int操作
	private AtomicInteger int_pos;
	private boolean is_continue = true;
	private List<Fragment> frgm_list;
	private Handler handler;
	public PageChangeThread(List<Fragment> listFrgm,Handler handler,AtomicInteger posIndex)
	{
		frgm_list = listFrgm;
		this.handler = handler;
		int_pos = posIndex;
	}
	
	public void setIsContinue(boolean isContinue)
	{
		is_continue = isContinue;
	}
	
	@Override
	public void run() {
		while (true) {
			if (is_continue == true) {
				try {
					// 休眠5秒
					Thread.sleep(duration);
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 休眠过程中如果状态改变则进行下次循环
				if (is_continue == true) {
					// ViewPager下标向前移动一位
					processPos();
					// 发送消息更新ViewPager
					handler.sendEmptyMessage(int_pos.get());
				}
			}
		}
	}

	/**
	 * ViewPager下标向前移动一位
	 * 
	 * @return
	 */
	private int processPos() {
		if (int_pos.get() + 1 >= frgm_list.size()) {
			int_pos.set(0);
		} else {
			int_pos.addAndGet(1);
		}
		return int_pos.get();
	}
}
