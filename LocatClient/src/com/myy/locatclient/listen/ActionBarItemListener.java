package com.myy.locatclient.listen;

import java.util.HashMap;

import com.myy.locatclient.dialog.ImeiDialog;
import com.myy.locatclient.dialog.QRcodeDialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ActionBarItemListener implements OnItemClickListener {

	private Context context =null;
	private int width;
	private int height;
	private int Id;
	
	public ActionBarItemListener(Context context,int width,int height)
	{
		this.context=context;
		this.width=width;
		this.height=height;
	}
	
	@Override
	public void onItemClick(AdapterView<?> views, View view, int pos, long row) {
		@SuppressWarnings("unchecked")
		HashMap<String,Object> map = (HashMap<String,Object>)views.getItemAtPosition(pos);
		String text = (String)map.get("text");
		if(text.equals("IMEI绑定")){//如果点击的是IMEI绑定的方式
		ImeiDialog imeidialog= new ImeiDialog(context);
		Window window = imeidialog.getWindow();
        window.setGravity(Gravity.CENTER);
        imeidialog.setCancelable(true);
        imeidialog.show();}
		if(text.equals("二维码绑定")){
			QRcodeDialog qrcodedialog= new QRcodeDialog(context,width,2*height);
			Window window = qrcodedialog.getWindow();
			window.setGravity(Gravity.CENTER);
			qrcodedialog.setCancelable(true);
	        qrcodedialog.show();
		}
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
}