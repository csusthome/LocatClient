package com.myy.locatclient.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.ms.utils.bean.Entity;
import com.ms.utils.bean.Fencing;
import com.ms.utils.bean.Pupillus;
import com.ms.utils.bean.WhiteNum;
import com.myy.locatclient.R;
import com.myy.locatclient.adapter.ContentPagerAdapter;
import com.myy.locatclient.application.LocationClientApplication;
import com.myy.locatclient.database.MyDatabaseHelper;
import com.myy.locatclient.fragment.GuardianFragment;
import com.myy.locatclient.fragment.LocatStateFragment;
import com.myy.locatclient.fragment.WhiteListFragment;
import com.myy.locatclient.listen.ActionBarItemListener;
import com.myy.locatclient.listen.ContentViewPageChangeListner;
import com.myy.locatclient.reciver.BatteryReceiver;
import com.myy.locatclient.thread.LoginTask;
import com.myy.locatclient.thread.PageChangeThread;
import com.myy.locatclient.utils.PhoneUtils;

@SuppressLint("NewApi")
public class MainActivity extends FragmentActivity implements OnClickListener {
	public static boolean DEBUG = true;

	private static ArrayList<Fragment> frgm_list = new ArrayList<Fragment>();
	private ViewPager pager = null;
	private DrawerLayout darwer = null;
	private ImageButton ibut_settion = null;
	private GridView gv_actionbar = null;
	private ViewGroup vg_indicator = null;
	private ArrayList<ImageView> iv_indicator_list = new ArrayList<ImageView>();
	// �ṩ�̰߳�ȫ��int����
	private AtomicInteger int_pos = new AtomicInteger(0);
	private Handler handler = null;
	private PageChangeThread pageChangeThread = null;
	
	private BatteryReceiver batteryReceiver;
	public int width; // �Ի�����
	public int height; // �Ի���߶�
	public static MyDatabaseHelper dbHelper = null;
	private static Toast toast;

	// private MyDatabaseHelper dbHelper=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// �����ޱ�������AcitonBar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initData();
		initDrawerLayout();
		initTitleFragment();
		initActionGridView();
		initViewPage();
		iniDialog();
		initLoactClient();
		initBroadcastReciver();
	}

	/**
	 * ��ʼ���Ӷ˵����ݣ�IMEI����֤��
	 */
	private void initData() {
		if (dbHelper == null) {
			dbHelper = new MyDatabaseHelper(this, "LocalChild.db", null, 1);
		}
		LocationClientApplication.IMEI = PhoneUtils.getIMEI(this);
		LocationClientApplication.CHECK_CODE = dbHelper.getCheckCode();
	}

	public static void updateWhiteList(List<WhiteNum> listWhtie) {
		for(Fragment frag:frgm_list)
		{
			if(frag instanceof WhiteListFragment)
			{
				LocationClientApplication.whites = listWhtie;
				WhiteListFragment frgm = (WhiteListFragment)frgm_list.get(1);
				frgm.updateItem(listWhtie);
			}
		}
	}

	private void initLoactClient() {
		Pupillus pup = new Pupillus();
		pup.setMeid(PhoneUtils.getIMEI(this));
		pup.setVerification_code(LocationClientApplication.CHECK_CODE);
		LoginTask task = LoginTask.getInstace(pup);
		if (task != null) {
			task.execute();
		}
	}

	private void iniDialog() {// ��ȡ��Ļ���ȿ��
		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
		height = dm.heightPixels;
	}

	/**
	 * ��ʼ����Ϣ�����ߣ���Ҫ��������ViewPager�Ķ�ʱ�л�
	 */
	private void initHandler() {
		handler = new Handler() {
			public void handleMessage(Message msg) {
				pager.setCurrentItem(msg.what);
				super.handleMessage(msg);
			}
		};
	}

	private void initActionGridView() { // ��ȡIMEI��
		LinkedList<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
		int[] icon_ids = { R.drawable.imei_bind, R.drawable.qr_code,
				R.drawable.nfc_feed };
		String[] function_names = { "IMEI��", "��ά���", "NFC��" };
		// �������
		for (int i = 0; i < icon_ids.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("image", icon_ids[i]);
			map.put("text", function_names[i]);
			list.add(map);
		}
		String from[] = { "image", "text" };
		int to[] = { R.id.gvitem_img, R.id.gvitem_text };
		gv_actionbar = (GridView) findViewById(R.id.gv_actionbar);
		gv_actionbar.setAdapter(new SimpleAdapter(this, list,
				R.layout.main_griditem, from, to));
		gv_actionbar.setOnItemClickListener(new ActionBarItemListener(this,
				width, height));
	}

	private void initTitleFragment() {
		// �ܹ��Ҵ���View�еĿؼ�
		ibut_settion = (ImageButton) findViewById(R.id.but_setting);
		ibut_settion.setOnClickListener(this);
	}

	private void initDrawerLayout() {
		darwer = (DrawerLayout) findViewById(R.id.drawer_main);
	}

	private void initViewPage() {
		initHandler();
		pager = (ViewPager) findViewById(R.id.vp_content);
		//��ʼ��ViewPager�е�������Ƭ
		if(frgm_list.size()==0)
		{
			LocatStateFragment locatstate_frgm = new LocatStateFragment();
			WhiteListFragment list_frgm = new WhiteListFragment();
			GuardianFragment content_frgm1 = new GuardianFragment();
			frgm_list.add(locatstate_frgm);
			frgm_list.add(list_frgm);
			frgm_list.add(content_frgm1);
		}
		pager.setAdapter(new ContentPagerAdapter(getSupportFragmentManager(),
				frgm_list));

		pager.setOnPageChangeListener(new ContentViewPageChangeListner(
				iv_indicator_list, int_pos));
		// ���ô��������������ڿ��ƴ���ʱ��ʱ��ʧЧ
		pager.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {

				switch (event.getAction()) {
				// �������ƶ�ʱ��ʱ��ʧЧ���ɿ���ʱ��������Ч
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_MOVE:
//					is_continue = false;
					pageChangeThread.setIsContinue(false);
					break;
				case MotionEvent.ACTION_UP:
					pageChangeThread.setIsContinue(true);
//					is_continue = true;
					break;
				default:
					pageChangeThread.setIsContinue(true);
//					is_continue = true;
					break;
				}
				return false;
			}
		});
		// ��ʼ��Pager��ʱ�л��̣߳���ʱ����
		pageChangeThread = new PageChangeThread(frgm_list, handler,int_pos);
		
		if (DEBUG == false) {
			pageChangeThread.start();
		}
		initPageIndicator();
	}

	/**
	 * ��ʼ��ViewPagerָʾ��
	 */
	private void initPageIndicator() {
		vg_indicator = (ViewGroup) findViewById(R.id.ll_page_indicator);
		// ָʾ����������Fragment��ȷ��
		int len = frgm_list.size();

		ImageView iv = null;
		for (int i = 0; i < len; i++) {
			iv = new ImageView(this);
			// ���ñ߾�
			iv.setPadding(5, 5, 5, 5);
			// Ĭ�ϵ�һ��Ϊѡ��
			if (i == 0) {
				iv.setBackgroundResource(R.drawable.pageindicator_fous_shape);
			} else {
				iv.setBackgroundResource(R.drawable.pageindicator_infous_shape);
			}
			iv_indicator_list.add(iv);
			vg_indicator.addView(iv);
		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.but_setting:
			// ����˵�
			darwer.openDrawer(Gravity.LEFT);
			// �ر���˵�
			// darwer.closeDrawer(Gravity.LEFT);
			break;
		default:
			break;
		}
	}

	/**
	 * ע��㲥������ ���������ļ����Ѿ�ע������Ҫ��̬����
	 */
	private void initBroadcastReciver() {
		// ע��㲥������java����,�����仯������
		IntentFilter intentFilter = new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED);
		// �����㲥�����߶���
		batteryReceiver = new BatteryReceiver();
		// ע��receiver
		registerReceiver(batteryReceiver, intentFilter);
	}

	protected void onDestroy() {
		super.onDestroy();
		if (batteryReceiver != null) {
			unregisterReceiver(batteryReceiver);
		}
	}

	public static void showMessage(String string) {
		if (toast != null) {
			toast.setText(string);
			toast.show();
		}
	}

}
