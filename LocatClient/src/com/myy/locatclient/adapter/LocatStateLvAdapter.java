package com.myy.locatclient.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.myy.locatclient.R;


public class LocatStateLvAdapter extends BaseAdapter {

	private ArrayList<ChildItem> data_list = null;
	private Context context = null;
	
	public LocatStateLvAdapter(Context context) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.locatstate_item,parent,false);
			TextView tv_name = (TextView)convertView.findViewById(R.id.tv_funcname);
			TextView tv_data = (TextView)convertView.findViewById(R.id.tv_data);
			vholder.tv_name=tv_name;
			vholder.tv_data=tv_data;
			convertView.setTag(vholder);
		}
		else
		{
			vholder=(ViewHolder)convertView.getTag();
		}
		ChildItem item  = data_list.get(position);
		vholder.tv_name.setText(item.func_name);
		vholder.tv_data.setText(item.data);
		return convertView;
	}

	/**
	 * 更新数据
	 */
	public void updateData(ArrayList<ChildItem> list)
	{
		
		if(list==null)
		{
			data_list = new ArrayList<LocatStateLvAdapter.ChildItem>();
			data_list.add(new ChildItem("坐标","经度23.5|维度36.7"));
			data_list.add(new ChildItem("定位时间","2016年04月23日"));
			data_list.add(new ChildItem("速度","12 米/秒"));
		}
		else
		{
			data_list = list;
		}
		this.notifyDataSetChanged();
	}
	
	public static class ChildItem
	{
		public String func_name;
		public String data;
		
		public ChildItem(String func_name,String data)
		{
			this.func_name=func_name;
			this.data=data;
		}
	}
	
	static class ViewHolder
	{
		private TextView tv_name;
		private TextView tv_data;
	}
}
