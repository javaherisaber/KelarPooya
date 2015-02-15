package ir.highteam.ecommercekelar.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import ir.highteam.ecommercekelar.R;

@SuppressLint("InflateParams")
public class CustomToast {
	
	Context context;
//	String toastBackColorHex;
//	String toastTextColorHex;
	String toastText;
	
	public CustomToast(String toastText,Context context){
		this.toastText = toastText;
//		this.toastBackColorHex = toastBackColorHex;
//		this.toastTextColorHex =toastTextColorHex;
		this.context = context;
	}
	
	public void showToast(boolean isLong) {
		Toast toast = new Toast(context);
		if(isLong){
			toast.setDuration(Toast.LENGTH_LONG);
		}
		else {
			toast.setDuration(Toast.LENGTH_SHORT);
		}
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.custom_toast_view, null);
		//LinearLayout toastLay = (LinearLayout) view.findViewById(R.id.toast_lay);
		//toastLay.setBackgroundColor(Color.parseColor("#"+toastBackColorHex));
		TextView toastTextView = (TextView) view.findViewById(R.id.toast_text);
        Typeface tfsans = Typeface.createFromAsset(context.getAssets(),context.getString(R.string.FONT_IRAN_SANS));
//		toastTextView.setTextColor(Color.parseColor("#"+toastTextColorHex));
		toastTextView.setTypeface(tfsans);
        toastTextView.setText(toastText);
		toast.setView(view);
		toast.show();
		
	}
	
	

}
