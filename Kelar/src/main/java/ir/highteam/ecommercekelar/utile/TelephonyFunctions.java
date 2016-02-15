package ir.highteam.ecommercekelar.utile;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;

import com.crashlytics.android.Crashlytics;

import java.math.BigInteger;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.view.CustomToast;

public class TelephonyFunctions {

	private Activity activity;
	
	public TelephonyFunctions(Activity act){
		this.activity = act;
	}
	
    public void sendSms(String phoneNumber, String messege){

		try{

			BigInteger phoneNumberInt = new BigInteger(phoneNumber);
			Intent sendIntent = new Intent(Intent.ACTION_VIEW);
			sendIntent.setData(Uri.parse("sms:" + String.valueOf(phoneNumberInt)));
			sendIntent.putExtra("sms_body", messege);
			activity.startActivity(sendIntent);

		} catch (ActivityNotFoundException e1) {
			Crashlytics.logException(e1);
			new CustomToast(activity.getString(R.string.NO_SMS_APPLICATION_FOUND), activity)
					.showToast(true);
		}
    }

	public void makeCall(String PhoneNum){
		try{

			Intent callIntent = new Intent(Intent.ACTION_DIAL);
			callIntent.setData(Uri.parse("tel:"+Uri.encode(PhoneNum.trim())));
			callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			activity.startActivity(callIntent);

		} catch (ActivityNotFoundException e1) {
			Crashlytics.logException(e1);
			new CustomToast(activity.getString(R.string.NO_CALL_APPLICATION_FOUND), activity)
					.showToast(true);
		}
	}
}
