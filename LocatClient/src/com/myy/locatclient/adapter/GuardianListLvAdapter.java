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
			list.add(new ChildItem("�˻�1","wei6550215"));
			list.add(new ChildItem("�˻�2","13590944470"));
			list.add(new ChildItem("�˻�3","451939491"));
		}
//		for(Entity entity:listEntity)
//		{
//			ChildItem item = new ChildItem("��ص�",entity.get)
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
		TextView gl_desc;//��ʾ�˺���
		TextView gl_account;//�˺�
		Button but_func;//��ť
	}

	
		@Override
		public void onClick(View v) {//�����жϰ�ť�������洰��
			// TODO �Զ����ɵķ������
			AlertDialog.Builder warndialog = new AlertDialog.Builder(context);
			warndialog.setTitle("\t\t\t\t\t\t\t\t\t\t\t����");
			warndialog.setMessage("���˻������ٶԱ������м�أ�");
			warndialog.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO �Զ����ɵķ������
					
				}
			});
			warndialog.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO �Զ����ɵķ������
					
				}
			});
			warndialog.show();
			//warndialog.setAdapter(adapter, listener)
			Toast.makeText(context, "�жϴ��˻�", Toast.LENGTH_SHORT).show();
		}
}
