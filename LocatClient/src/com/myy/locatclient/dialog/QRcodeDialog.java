package com.myy.locatclient.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.myy.locatclient.R;

public class QRcodeDialog extends Dialog {

	private static int theme = android.R.style.Theme_Holo_Light_Dialog; // ����
	private Context context=null;
	private ImageView qrcode=null;
	private TextView account=null;
	private int width; // �Ի�����
    private int height; // �Ի���߶�
    
    
	public QRcodeDialog(Context context,int width, int height ) {
		super(context);
		this.context=context;
		this.width=width;
		this.height=height;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.qrcodedialog);
		initView();
	}
	private void initView(){
		
		qrcode=(ImageView)findViewById(R.id.qrcode_image);
		account=(TextView)findViewById(R.id.account_code);
	}
}
