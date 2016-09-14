package com.myy.locatclient.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

public class PhoneUtils {

	public static String getIMEI(Context context)
	{
		TelephonyManager tm= (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		 return tm.getDeviceId();
	}
}
