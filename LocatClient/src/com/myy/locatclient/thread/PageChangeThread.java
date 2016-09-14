package com.myy.locatclient.thread;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import android.os.Handler;
import android.support.v4.app.Fragment;

public class PageChangeThread extends Thread {
	// ��Ϊ�ؼ���is_continue�޷������ô��ݣ�����ֻ�����ڲ���ʵ����
	private static final int duration = 5000;
	// �ṩ�̰߳�ȫ��int����
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
					// ����5��
					Thread.sleep(duration);
				} catch (Exception e) {
					e.printStackTrace();
				}
				// ���߹��������״̬�ı�������´�ѭ��
				if (is_continue == true) {
					// ViewPager�±���ǰ�ƶ�һλ
					processPos();
					// ������Ϣ����ViewPager
					handler.sendEmptyMessage(int_pos.get());
				}
			}
		}
	}

	/**
	 * ViewPager�±���ǰ�ƶ�һλ
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
