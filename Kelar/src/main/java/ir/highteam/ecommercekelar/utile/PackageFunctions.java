package ir.highteam.ecommercekelar.utile;

import android.app.Activity;
import android.content.pm.PackageManager;

import com.crashlytics.android.Crashlytics;

/**
 * Created by Mahdizit on 13/08/2016.
 */
public class PackageFunctions {

    private Activity activity;

    public PackageFunctions(Activity activity){
        this.activity = activity;
    }

    public boolean isAppAvailable(String appName)
    {
        PackageManager pm = this.activity.getPackageManager();
        try
        {
            pm.getPackageInfo(appName, PackageManager.GET_ACTIVITIES);
            return true;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            Crashlytics.logException(e);
            return false;
        }
    }

}
