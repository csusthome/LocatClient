package com.myy.locatclient.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ms.utils.bean.Entity;
import com.myy.locatclient.R;

public class GuardianListLvAdapter extends BaseAdapter implements OnClickListener{

	private List<ChildItem> list = new ArrayList<GuardianListLvAdapter.ChildItem>();
	private Context context = null;
	
	
	
	public GuardianListLvAdapter(Context context) {
		super();
		this.context=context;
		updateData(null);
	}

	public void updateData(List<ChildItem> itemList)
	{
//		list.clear();
		if(itemList==null)
		{
			list = new ArrayList<GuardianListLvAdapter.ChildItem>();
			list.add(new ChildItem("账户1","wei6550215"));
			list.add(new ChildItem("账户2","13590944470"));
			list.add(new ChildItem("账户3","451939491"));
		}
//		for(Entity entity:listEntity)
//		{
//			ChildItem item = new ChildItem("监控的",entity.get)
//		}
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vholder = null;
		if(convertView==null)
		{
			vholder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.guardianlist_item,parent,false);
			TextView desc = (TextView)convertView.findViewById(R.id.tv_desc);
			TextView account = (TextView)convertView.findViewById(R.id.tv_account) ;
			Button but_func = (Button)convertView.findViewById(R.id.cut_but);
			vholder.gl_desc=desc;
			vholder.gl_account=account;
			vholder.but_func=but_func;
			convertView.setTag(vholder);
		}
		else
		{
			vholder=(ViewHolder)convertView.getTag();
		}
		ChildItem item  = list.get(position);
		vholder.gl_desc.setText(item.account);
		vholder.gl_account.setText(item.name);
		vholder.but_func.setOnClickListener(this);
		return convertView;
	}

	private class ChildItem
	{
		public String account;
		public String name;
		
		public ChildItem(String account,String name) {
			super();
			this.account = account;
			this.name = name;
		}
		
	}
	
	private class ViewHolder
	{
		TextView gl_desc;//显示账号名
		TextView gl_account;//账号
		Button but_func;//按钮
	}

	
		@Override
		public void onClick(View v) {//单击中断按钮弹出警告窗口
			// TODO 自动生成的方法存根
			AlertDialog.Builder warndialog = new AlertDialog.Builder(context);
			warndialog.setTitle("\t\t\t\t\t\t\t\t\t\t\t警告");
			warndialog.setMessage("该账户将不再对本机进行监控？");
			warndialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO 自动生成的方法存根
					
				}
			});
			warndialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO 自动生成的方法存根
					
				}
			});
			warndialog.show();
			//warndialog.setAdapter(adapter, listener)
			Toast.makeText(context, "中断此账户", Toast.LENGTH_SHORT).show();
		}
}
