package com.myy.locatclient.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.myy.locatclient.R;
import com.myy.locatclient.adapter.LocatStateLvAdapter;
import com.myy.locatclient.application.LocationClientApplication;

public class LocatStateFragment extends Fragment {

	private ListView lv_locat = null;
	private View view = null;
	private LocatStateLvAdapter lv_adatper = null;
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.locatestate_frgm, container,false);
		initListView();
		return view;
	}

	private void initListView()
	{
		lv_locat = (ListView)view.findViewById(R.id.lv_locat);
		//设置数据适配器
		lv_adatper = new LocatStateLvAdapter(getActivity());
		lv_locat.setAdapter(lv_adatper);
		//将adpter传给Application以完成定位后更新的功能
		((LocationClientApplication)getActivity().getApplicationContext())
		.setShowListViewAdapter(lv_adatper);
	}
}
