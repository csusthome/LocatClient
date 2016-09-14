package com.myy.locatparent.common;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.myy.locatclient.R;

public class LoadingPopupWindow extends PopupWindow {
	private LayoutInflater inflater;
	private View view = null;
	private TextView tv_desc = null;

	public LoadingPopupWindow(Activity activity) {

		inflater = LayoutInflater.from(activity);
		view = inflater.inflate(R.layout.loading_layout, null);
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		// 使用MeasureSpec后才能正确的得到popwindow的宽高
		view.measure(w, h);
		initTextView();

		// 设置了具体的宽高后才能在上方显示
		this.setContentView(view);
		this.setWidth(view.getMeasuredWidth());
		this.setHeight(view.getMeasuredHeight());
		// 设置一个空的背景才能够点击其他地方
		setBackgroundDrawable(null);
		setTouchable(true);
		setFocusable(true);
		setOutsideTouchable(false);
	}

	private void initTextView() {
		tv_desc = (TextView) view.findViewById(R.id.tv_desc);
	}

	public void setText(String desc) {
		tv_desc.setText(desc);
	}

}
