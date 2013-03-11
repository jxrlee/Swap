package com.swap;

import android.content.Context;
import android.telephony.TelephonyManager;

public class SwapUtility {
	static public String getPhoneNumber(Context context, int digitCount) {

		TelephonyManager mTelephonyMgr;
	    mTelephonyMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE); 
	    String num = mTelephonyMgr.getLine1Number();
	    if(num!=null && num.length()>digitCount)
	    {
	    	num= num.substring(num.length()-digitCount);
	    }
	    else
	    {
	    	num = "5555555555";
	    }
	    return num;
	}
}
