package ir.highteam.ecommercekelar.utile;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;

import com.crashlytics.android.Crashlytics;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.view.CustomToast;

public class ShareFunctions {

	Activity activity;
	
	public ShareFunctions(Activity act) {
		this.activity = act;
	}
	
	public void sharePlainText(String text)
	{
		try{

			Intent sharingIntent = new Intent(Intent.ACTION_SEND);
			sharingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			sharingIntent.setType("text/plain");
//        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
			sharingIntent.putExtra(Intent.EXTRA_TEXT, text);
			activity.startActivity(Intent.createChooser(sharingIntent, "اشتراک گذاری با "));

		} catch (ActivityNotFoundException e1) {
			Crashlytics.logException(e1);
			new CustomToast(activity.getString(R.string.NO_SHARE_APPLICATION_FOUND), activity)
					.showToast(true);
		}
	}

}
