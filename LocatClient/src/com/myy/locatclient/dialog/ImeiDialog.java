package com.myy.locatclient.dialog;

import java.util.Random;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.myy.locatclient.R;
import com.myy.locatclient.activity.MainActivity;
import com.myy.locatclient.application.LocationClientApplication;
import com.myy.locatclient.utils.PhoneUtils;

public class ImeiDialog extends Dialog implements
		android.view.View.OnClickListener {

	private Button refresh = null;
	private TextView identifycode = null;
	private TextView imeicode = null;
	private Context context;

	public ImeiDialog(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.imei_dialog);
		init();
	}

	private void init() {
		identifycode = (TextView) findViewById(R.id.identify_code);
		identifycode.setText(LocationClientApplication.CHECK_CODE);// 文本显示随机六位数
		imeicode = (TextView) findViewById(R.id.IMEI_code);
		imeicode.setText(PhoneUtils.getIMEI(context));
		refresh = (Button) findViewById(R.id.refresh);
		refresh.setOnClickListener(this);

	}

	/**
	 * 用于生成六位数字验证码
	 * @return
	 */
	public static String randomCheckCode() {
		Random rd = new Random();
		String number = Integer.toString(rd.nextInt(10))
				+ Integer.toString(rd.nextInt(10))
				+ Integer.toString(rd.nextInt(10))
				+ Integer.toString(rd.nextInt(10))
				+ Integer.toString(rd.nextInt(10))
				+ Integer.toString(rd.nextInt(10));
		return number;
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		if (v.getId() == R.id.refresh) {
			{
				LocationClientApplication.CHECK_CODE = randomCheckCode();
				identifycode.setText(LocationClientApplication.CHECK_CODE);
			}
		}
	}
}
