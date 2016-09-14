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

	// 需要上传的点队列
	private List<Point> pointsList = new ArrayList<Point>();
	// 用于存放上一个上传的点，用于去重
	private Point last_p = null;
	// private List<Point> removeList = new
	// ArrayList<UpLoadTraceThread.Point>();
	// 用于控制线程关闭
	private boolean isAliveKey = true;
	// 用于记录线程是否休眠
	// private boolean isWait = false;
	// 线程打包上传的间隔时间
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
		//更新围栏状态列表
		fence_state.clear();
	}
	
	/**
	 * 检测当前上传的点是否触发围栏警报,要使用检测功能需要先绑定围栏
	 * @param points 围栏点
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
					// 现在的状态
					Boolean isInFence = Distance.inFence(fence, p);
					Boolean lastState = fence_state.get(i);
					// 现在的状态与之前的状态不同
					if (isInFence.equals(lastState)==false) {
						String msg = "离开围栏";
						if (isInFence == true) {
							msg = "进入围栏";
						}
						MainActivity.showMessage("触发围栏警报:"+msg);
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
		// 若已包含相同的点则不添加
		if (last_p != null && p.equals(last_p) == true) {
			return;
		}

		pointsList.add(p);
		last_p = p;
		// 添加新点后唤醒线程
		synchronized (this) {
			showDebugLog("addPoint");
			this.notify();
		}

	}

	/**
	 * 返回是否已经结束，与isAlive不同，若线程未启动也会返回true
	 * 
	 * @return 上传线程是否已经启动并被结束（调用 stopThread方法）
	 */
	public boolean hasStoped() {
		return !isAliveKey;
	}

	public void waitThread() {
		// 添加新点后唤醒线程
		synchronized (this) {
			showDebugLog("wait methrod");
			this.notify();
		}
	}

	/**
	 * 自定义的线程关闭方法，会执行完最后一次上传后关闭
	 */
	public void stopThread() {
		isAliveKey = false;
	}

	@Override
	public void run() {
		while (isAliveKey) {
			//若已经登录成功
			if(LocationClientApplication.pup!=null)
			{
				// xx.wait 应该获得xx的锁
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
						//围栏检测
						checkFencing(pointsList);
						if (result.equals("error")) {
							showDebugLog("points is empty");
						}
						// 上传后将上传的点从列表中删除
						pointsList.clear();
						// 进入间隔等待
	
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
