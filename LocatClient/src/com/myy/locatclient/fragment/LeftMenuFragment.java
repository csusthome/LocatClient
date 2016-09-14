package com.myy.locatclient.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.myy.locatclient.R;
import com.myy.locatclient.activity.MainActivity;
import com.myy.locatclient.adapter.LeftMenuFuncAdapter;
import com.myy.locatclient.database.MyDatabaseHelper;

public class LeftMenuFragment extends Fragment{

	private MyDatabaseHelper dbHelper=null;
	private ListView lv_func = null;
	private View view = null;
	private LeftMenuFuncAdapter leftmenu_adapter = null;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.leftmenu_frgm,container,false);
		initListView();
		return view;
	}
	
	
	
	public void onDestroyView(){
		super.onDestroyView();
		leftmenu_adapter.saveData();
	}
	
	
	private void initListView()
	{	
		lv_func = (ListView)view.findViewById(R.id.lv_func);
		leftmenu_adapter = new LeftMenuFuncAdapter((MainActivity)this.getActivity());
		lv_func.setAdapter(leftmenu_adapter);
	}
	
}
