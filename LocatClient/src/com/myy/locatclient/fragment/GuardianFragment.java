package com.myy.locatclient.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.myy.locatclient.R;
import com.myy.locatclient.adapter.GuardianListLvAdapter;
import com.myy.locatclient.utils.LogUtils;

public class GuardianFragment extends Fragment {
	
	private View view=null;
	private ListView gl_list=null;
	private GuardianListLvAdapter adpter;
	private final String TAG = "GuardianFragment";
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		 view = inflater.inflate(R.layout.guardianlist_frag,container,false);
		initListView();
		LogUtils.d(TAG,"onCreatView");
		return view;
	}
	
	private void initListView(){
		//在View被销毁后，ListView对象应该变了
		//		if(gl_list==null)
//		{
//			
//		}
		gl_list=(ListView)view.findViewById(R.id.gl_list);
		LogUtils.d(TAG, "ListView:"+gl_list.toString());
		adpter = new GuardianListLvAdapter(getActivity());
		gl_list.setAdapter(adpter);
		adpter.updateData(null);
	}

	@Override
	public void onDestroyView() {
		LogUtils.d(TAG,"onDestroyView");
		super.onDestroyView();
	}
	
}
