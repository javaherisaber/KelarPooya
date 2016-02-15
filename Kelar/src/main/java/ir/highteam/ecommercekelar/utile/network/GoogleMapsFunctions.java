package ir.highteam.ecommercekelar.utile.network;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;

import com.crashlytics.android.Crashlytics;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.bundle.BundleLocation;
import ir.highteam.ecommercekelar.utile.SendToFunctions;

/**
 * Created by Mahdizit on 13/08/2016.
 */
public class GoogleMapsFunctions {

    private Activity activity;
    public GoogleMapsFunctions(Activity activity){
        this.activity = activity;
    }

    public void goToDefaultWithPin(BundleLocation location){
        String strUri = "http://maps.google.com/maps?q=loc:" + location.latitude + "," + location.longitude+ " (" + location.label + ")";
        try {

            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            this.activity.startActivity(intent);

        } catch (ActivityNotFoundException e) {
            Crashlytics.logException(e);
            new SendToFunctions(activity).sendToMarketToInstallApp("com.google.android.apps.maps",
                    activity.getString(R.string.NO_MAPS_FOUND_INSTALL_NOW),activity);
        }
    }
}
