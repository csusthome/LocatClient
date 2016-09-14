package com.myy.locatclient.fragment;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.baidu.location.LocationClient;
import com.ms.utils.bean.Pupillus;
import com.ms.utils.bean.WhiteNum;
import com.myy.locatclient.R;
import com.myy.locatclient.activity.MainActivity;
import com.myy.locatclient.adapter.WhiteListLvAdapter;
import com.myy.locatclient.application.LocationClientApplication;
import com.myy.locatclient.thread.QueryWhiteTask;

public class WhiteListFragment extends Fragment implements OnClickListener {

	private View view = null;
	private ListView lv_whitelist = null;
	private WhiteListLvAdapter adapter;
	private Button butRefresh;
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,  Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.whitelist_frgm,container,false);
		initListView();
		initButton();
		return view;
	}

	private void initButton()
	{
		butRefresh = (Button)view.findViewById(R.id.but_refresh);
		butRefresh.setOnClickListener(this);
	}
	private void initListView()
	{
		lv_whitelist = (ListView)view.findViewById(R.id.lv_blacklist);
		adapter = new WhiteListLvAdapter(getActivity());
		lv_whitelist.setAdapter(adapter);
		updateWhiteList();
	}
	
	private void updateWhiteList()
	{
		Pupillus pup = LocationClientApplication.pup;
		//若还未更新白名单数据则获取最新的白名单数据
		if(pup!=null)
		{
			QueryWhiteTask task = new QueryWhiteTask(pup);
			task.execute();
		}
	}
	
	public void updateItem(List<WhiteNum> whiteList)
	{
		adapter.updateData(whiteList);
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.but_refresh)
		{
			updateWhiteList();
		}
	}
	
}
