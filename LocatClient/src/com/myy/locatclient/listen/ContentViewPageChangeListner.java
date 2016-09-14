package com.myy.locatclient.listen;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.ImageView;

import com.myy.locatclient.R;

public class ContentViewPageChangeListner implements OnPageChangeListener {

	private List<ImageView> iv_list = null;
	private AtomicInteger int_pos = null;
	public ContentViewPageChangeListner(ArrayList<ImageView> iv_indicator_list,AtomicInteger int_pos) {
		iv_list = iv_indicator_list;
		this.int_pos = int_pos;
	}


	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int pos) {
		//需要更新Pager的索引
		int_pos.set(pos);
		ImageView iv = null;
		//更新圆点指示器
		for(int i =0;i<iv_list.size();i++)
		{
			iv = iv_list.get(i);
			if(i == pos)
			{
				iv.setBackgroundResource(R.drawable.pageindicator_fous_shape);
			}
			else
			{
				iv.setBackgroundResource(R.drawable.pageindicator_infous_shape);
			}
		}
	}


	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

}