package com.myy.locatclient.adapter;

import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.ms.utils.bean.AnomalyRecord;
import com.ms.utils.bean.Pupillus;
import com.myy.locatclient.R;
import com.myy.locatclient.activity.MainActivity;
import com.myy.locatclient.application.LocationClientApplication;
import com.myy.locatclient.database.MyDatabaseHelper;
import com.myy.locatclient.thread.AddAnomalyTask;
import com.myy.locatclient.utils.LogUtils;

public class LeftMenuFuncAdapter extends BaseAdapter {

	private ArrayList<ChildItem> list_options = new ArrayList<LeftMenuFuncAdapter.ChildItem>();
	private MainActivity mainActicity = null;
	public int isLocate;
	public int clearRepeat;
	public int whiltList;
	private MyDatabaseHelper dbHelper = null;

	public LeftMenuFuncAdapter(MainActivity mainActivity) {
		super();
		this.mainActicity = mainActivity;
		if(MainActivity.dbHelper ==null)
		{
			MainActivity.dbHelper = new MyDatabaseHelper(mainActivity, "LocalChild.db", null, 1);
		}
		dbHelper = MainActivity.dbHelper;
		initData();
		initFunction();
	}

	/**
	 * ��ʼ���໬�˵���ѡ������
	 */
	private void initData() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.query("switch", null, null, null, null, null, null);
		if (list_options.size() <= 0) {
			if (cursor.moveToFirst()) { // �����ݿ��ж�ȡ����
				isLocate = cursor.getInt(cursor.getColumnIndex("localstate"));
				clearRepeat = cursor.getInt(cursor.getColumnIndex("cutrepeation"));
				whiltList = cursor.getInt(cursor.getColumnIndex("blackwhitenamelist"));
				
				list_options.add(new ChildItem(R.drawable.ic_launcher, "��λ", isLocate == 1 ? true : false));
				list_options.add(new ChildItem(R.drawable.eraser, "��λȥ��", clearRepeat == 1 ? true : false));
				list_options.add(new ChildItem(R.drawable.black, "������", whiltList == 1 ? true : false));
			} else {
				list_options.add(new ChildItem(R.drawable.ic_launcher, "��λ", true));
				list_options.add(new ChildItem(R.drawable.eraser, "��λȥ��", true));
				list_options.add(new ChildItem(R.drawable.black, "������", false));
			}
		}
		
	}
	
	/**
	 * �ڳ�ʼ������֮�󣬸��ݳ�ʼ�������������ù��ܿ���
	 */
	private void initFunction()
	{
		//��ʼ������ʱ���ݶ�λ״̬�ı䶨λ���Ŀ���
		if(isLocate==1)
		{
			((LocationClientApplication)mainActicity.getApplication())
			.startLocatServer();
		}
	}
	
	public boolean isLocat()
	{
		if(isLocate==1)
			return true;
		else
			return false;
	}
	
	public boolean isClearRepeat()
	{
		if(clearRepeat==1)
			return true;
		else
			return false;
	}
	
	public boolean isWhileListEanble()
	{
		if(whiltList==1)
			return true;
		else
			return false;
	}

	/**
	 * ������˵��Ŀ���״̬�����ݴ��뵽���ݿ�
	 */
	public void saveData() {
		if(list_options==null&&list_options.size()<=0)
		{
			return ;
		}
		if (list_options.get(0).is_checked==true) {
			isLocate = 1;
		} else {
			isLocate = 0;
		}
		if (list_options.get(1).is_checked==true) {
			clearRepeat = 1;
		} else {
			clearRepeat = 0;
		}
		if (list_options.get(2).is_checked==true) {
			whiltList = 1;
		} else {
			whiltList = 0;
		}
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("localstate", isLocate);
		values.put("cutrepeation", clearRepeat);
		values.put("blackwhitenamelist", whiltList);
		db.update("switch", values,null , null);
	}

	@Override
	public int getCount() {
		return list_options.size();
	}

	@Override
	public Object getItem(int position) {
		return list_options.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder vholder = null;
		if (convertView == null) {
			vholder = new ViewHolder();
			convertView = LayoutInflater.from(mainActicity).inflate(R.layout.leftmenu_item, parent, false);
			ImageView iv_func = (ImageView) convertView.findViewById(R.id.iv_func);
			Switch sw_func = (Switch) convertView.findViewById(R.id.switch_but);
			sw_func.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if(isChecked){
					list_options.get(position).is_checked=true;
					if(position==0){	
						Toast.makeText(mainActicity, "��λ�Ѵ�", Toast.LENGTH_SHORT).show();
						((LocationClientApplication)mainActicity.getApplication())
						.startLocatServer();
					}else if(position==1){
						Toast.makeText(mainActicity, "��λȥ���Ѵ�", Toast.LENGTH_SHORT).show();
						
					}else if(position==2){
						Toast.makeText(mainActicity, "�ڰ������Ѵ�", Toast.LENGTH_SHORT).show();
					}
				}else{
					list_options.get(position).is_checked=false;
					if(position==0){
						Toast.makeText(mainActicity, "��λ�ѹر�", Toast.LENGTH_SHORT).show();
						LogUtils.i("LeftMenuFrgment","��λ�ѹر�");
						((LocationClientApplication)mainActicity.getApplication())
						.stopLoactServer();
					}else if(position==1){
						Toast.makeText(mainActicity, "��λȥ���ѹر�", Toast.LENGTH_SHORT).show();
					}else if(position==2){
						Toast.makeText(mainActicity, "�ڰ������ѹر�", Toast.LENGTH_SHORT).show();
					}
				}
			}
			});
			vholder.iv_func = iv_func;
			vholder.sw_func = sw_func;
			convertView.setTag(vholder);
		} else {
			vholder = (ViewHolder) convertView.getTag();
		}
		ChildItem item = list_options.get(position);
		vholder.iv_func.setImageResource(item.img_id);
		vholder.sw_func.setText(item.func_name);
		vholder.sw_func.setChecked(item.is_checked);
		return convertView;
	}

	private class ChildItem {
		public int img_id;
		public String func_name;
		public boolean is_checked;

		public ChildItem(int img_id, String func_name, boolean is_opend) {
			super();
			this.img_id = img_id;
			this.func_name = func_name;
			this.is_checked = is_opend;
		}
	}

	private class ViewHolder {
		ImageView iv_func;
		Switch sw_func;
	}

	
}
