package com.myy.locatclient.thread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ms.utils.DataExchangeUtils;
import com.ms.utils.Distance;
import com.ms.utils.bean.AnomalyRecord;
import com.ms.utils.bean.Fencing;
import com.ms.utils.bean.FencingRecord;
import com.ms.utils.bean.Point;
import com.ms.utils.bean.Pupillus;
import com.myy.locatclient.activity.MainActivity;
import com.myy.locatclient.application.LocationClientApplication;
import com.myy.locatclient.utils.LogUtils;

public class UpLoadTraceThread extends Thread {

	// ��Ҫ�ϴ��ĵ����
	private List<Point> pointsList = new ArrayList<Point>();
	// ���ڴ����һ���ϴ��ĵ㣬����ȥ��
	private Point last_p = null;
	// private List<Point> removeList = new
	// ArrayList<UpLoadTraceThread.Point>();
	// ���ڿ����̹߳ر�
	private boolean isAliveKey = true;
	// ���ڼ�¼�߳��Ƿ�����
	// private boolean isWait = false;
	// �̴߳���ϴ��ļ��ʱ��
	private static int duartion = 5 * 1000;
	private List<Fencing> fences = null;
	private List<Boolean> fence_state = new ArrayList<Boolean>();
	private static boolean DEBUG = true;

	private void showDebugLog(String msg) {
		if (DEBUG == true) {
			LogUtils.i("UploadThread",
					msg + " " + this.toString() + " " + this.isAlive());
		}
	}

	public void bindFences(List<Fencing> fences) {
		this.fences = fences;
		//����Χ��״̬�б�
		fence_state.clear();
	}
	
	/**
	 * ��⵱ǰ�ϴ��ĵ��Ƿ񴥷�Χ������,Ҫʹ�ü�⹦����Ҫ�Ȱ�Χ��
	 * @param points Χ����
	 * @throws IOException
	 */
	private void checkFencing(List<Point> points) throws IOException {
		if (fences == null || fences.size() <= 0) {
			if(LocationClientApplication.fencings!=null&&LocationClientApplication.fencings.size()>0)
			{
				bindFences(LocationClientApplication.fencings);
			}
			else
			{
				QueryFenceTask task = new QueryFenceTask(LocationClientApplication.entities);
				task.execute();
				return;
			}
		}
		if (fence_state.size() <= 0) {
			for (Fencing fence : fences) {
				for (Point p : points) {
					Boolean isInFence = Distance.inFence(fence, p);
					fence_state.add(isInFence);
				}
			}
		} else {
			int i = 0;
			for (Fencing fence : fences) {
				for (Point p : points) {
					// ���ڵ�״̬
					Boolean isInFence = Distance.inFence(fence, p);
					Boolean lastState = fence_state.get(i);
					// ���ڵ�״̬��֮ǰ��״̬��ͬ
					if (isInFence.equals(lastState)==false) {
						String msg = "�뿪Χ��";
						if (isInFence == true) {
							msg = "����Χ��";
						}
						MainActivity.showMessage("����Χ������:"+msg);
						FencingRecord record = new FencingRecord();
						record.setDate(new Date(System.currentTimeMillis()));
//						Pupillus pup = new Pupillus();
//						pup.setId(LocationClientApplication.pupID);
						record.setFencing(fence);
						record.setMessage(msg);
						AddFenceRecoredTask task = new AddFenceRecoredTask(record);
						task.execute();
					}
					fence_state.set(i, isInFence);
				}
				i++;
			}
		}
	}

	public void addPoint(Point p) {
		// ���Ѱ�����ͬ�ĵ������
		if (last_p != null && p.equals(last_p) == true) {
			return;
		}

		pointsList.add(p);
		last_p = p;
		// ����µ�����߳�
		synchronized (this) {
			showDebugLog("addPoint");
			this.notify();
		}

	}

	/**
	 * �����Ƿ��Ѿ���������isAlive��ͬ�����߳�δ����Ҳ�᷵��true
	 * 
	 * @return �ϴ��߳��Ƿ��Ѿ������������������� stopThread������
	 */
	public boolean hasStoped() {
		return !isAliveKey;
	}

	public void waitThread() {
		// ����µ�����߳�
		synchronized (this) {
			showDebugLog("wait methrod");
			this.notify();
		}
	}

	/**
	 * �Զ�����̹߳رշ�������ִ�������һ���ϴ���ر�
	 */
	public void stopThread() {
		isAliveKey = false;
	}

	@Override
	public void run() {
		while (isAliveKey) {
			//���Ѿ���¼�ɹ�
			if(LocationClientApplication.pup!=null)
			{
				// xx.wait Ӧ�û��xx����
				synchronized (this) {
					try {
						showDebugLog("run");
						if (pointsList.size() <= 0) {
							showDebugLog("size<=0 wait");
							this.wait();
							showDebugLog("be notified");
						}
						showDebugLog("send points size=" + pointsList.size());
						String result = DataExchangeUtils.addPoints(pointsList);
						showDebugLog("upload points success" + pointsList.size());
						//Χ�����
						checkFencing(pointsList);
						if (result.equals("error")) {
							showDebugLog("points is empty");
						}
						// �ϴ����ϴ��ĵ���б���ɾ��
						pointsList.clear();
						// �������ȴ�
	
					} catch (Exception e) {
						showDebugLog("Network error!" + e.getMessage());
						e.printStackTrace();
					}
				}
			}
			try {
				Thread.sleep(duartion);
			} catch (InterruptedException e) {
				showDebugLog("Thread sleep error!");
				e.printStackTrace();
			}

		}

	}

}
