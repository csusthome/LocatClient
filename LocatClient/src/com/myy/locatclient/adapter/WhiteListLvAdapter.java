package com.myy.locatclient.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ms.utils.bean.WhiteNum;
import com.myy.locatclient.R;

public class WhiteListLvAdapter extends BaseAdapter {

	private ArrayList<ChildItem> data_list = new ArrayList<WhiteListLvAdapter.ChildItem>();
	private Context context = null;
	
	public WhiteListLvAdapter(Context context) {
		this.context=context;
		updateData(null);
	}

	@Override
	public int getCount() {
		return data_list.size();
	}

	@Override
	public Object getItem(int position) {
		return data_list.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.whitelist_item,parent,false);
			TextView tv_name = (TextView)convertView.findViewById(R.id.tv_desc);
			TextView tv_data = (TextView)convertView.findViewById(R.id.tv_tel);
			vholder.tv_desc=tv_name;
			vholder.tv_tel=tv_data;
			convertView.setTag(vholder);
		}
		else
		{
			vholder=(ViewHolder)convertView.getTag();
		}
		ChildItem item  = data_list.get(position);
		vholder.tv_desc.setText(item.desc);
		vholder.tv_tel.setText(item.tel);
		return convertView;
	}

	/**
	 * 更新数据
	 */
	public void updateData(List<WhiteNum> whiteList)
	{
		data_list.clear();
		if(whiteList==null||whiteList.size()<=0)
		{
			ChildItem item = new ChildItem("无白名单数据","");
			data_list.add(item);
			notifyDataSetChanged();
			return;
		}
		for(WhiteNum white:whiteList)
		{
			ChildItem item = new ChildItem(white.getNote(),white.getPhone_number());
			data_list.add(item);
		}
		notifyDataSetChanged();
//		if(data_list==null)
//		{
//			data_list = new ArrayList<WhiteListLvAdapter.ChildItem>();
//			data_list.add(new ChildItem("陌生人1","13607443896"));
//			data_list.add(new ChildItem("陌生人2","13776952648"));
//			data_list.add(new ChildItem("陌生人3","13776952232"));
//		}
	}
	
	private class ChildItem
	{
		public String desc;
		public String tel;
		
		public ChildItem(String desc,String tel)
		{
			this.desc=desc;
			this.tel=tel;
		}
	}
	
	static class ViewHolder
	{
		private TextView tv_desc;
		private TextView tv_tel;
	}
}

