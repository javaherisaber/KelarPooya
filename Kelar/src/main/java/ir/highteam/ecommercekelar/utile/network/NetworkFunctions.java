package ir.highteam.ecommercekelar.utile.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkFunctions {

	private Context context;
	
	public NetworkFunctions(Context ctx) {
		
		this.context = ctx;
	}

	public Boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
	}

}
