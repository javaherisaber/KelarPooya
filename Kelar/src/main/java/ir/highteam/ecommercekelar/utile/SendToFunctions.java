package ir.highteam.ecommercekelar.utile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.customtabs.CustomTabsIntent;

import com.crashlytics.android.Crashlytics;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.view.CustomAlertDialog;
import ir.highteam.ecommercekelar.view.CustomToast;
import ir.highteam.ecommercekelar.view.customtabs.CustomTabActivityHelper;

/**
 * Created by Mahdizit on 13/08/2016.
 */
public class SendToFunctions {

    private Activity activity;
    public SendToFunctions(Activity activity){
        this.activity = activity;
    }

    public void sendToEmail(String emailAddress, String subject){

        try{

            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:" + emailAddress));
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            this.activity.startActivity(intent);

        } catch (ActivityNotFoundException e1) {
            Crashlytics.logException(e1);
            sendToMarketToInstallApp("com.google.android.gm",activity.getString(R.string.NO_EMAIL_APPLICATION_FOUND),activity);
        }
    }

    public void sendToCustomTab(String url){
        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
        CustomTabActivityHelper.openCustomTab(activity, customTabsIntent, Uri.parse(url),
                new CustomTabActivityHelper.CustomTabFallback() {
                    @Override
                    public void openUri(Activity activity, Uri uri) {
                        try{
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            activity.startActivity(intent);
                        }catch (ActivityNotFoundException e){
                            Crashlytics.logException(e);
                            sendToMarketToInstallApp("com.android.chrome",
                                    SendToFunctions.this.activity.getString(R.string.BROWSER_NOT_FOUND_INSTALL_NOW),SendToFunctions.this.activity);
                        }
                    }
                });
    }

    public void sendToTelegramBot(String botUrl){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(botUrl));
        final String packageName = "org.telegram.messenger";
        PackageFunctions functions = new PackageFunctions(this.activity);
        if (functions.isAppAvailable(packageName)){
            try {
                i.setPackage(packageName);
                this.activity.startActivity(i);
            }catch (ActivityNotFoundException e){
                Crashlytics.logException(e);
            }
        } else {
            sendToMarketToInstallApp(packageName,activity.getString(R.string.TELEGRAM_NOT_FOUND),activity);
        }
    }

    public void sendToMarketToInstallApp(final String packageName,final String message,final Activity activity){
        CustomAlertDialog customAlertDialog = new CustomAlertDialog(activity);

        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setCustomTitle(customAlertDialog.getTitleText(activity.getString(R.string.INSTALL_APPLICATION)))
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(activity.getString(R.string.YES), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try{

                            Uri marketUri = Uri.parse("market://details?id=" + packageName);
                            activity.startActivity(new Intent(Intent.ACTION_VIEW, marketUri));
                        } catch (ActivityNotFoundException e) {
                            Crashlytics.logException(e);
                            try{
                                activity.startActivity(new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)));
                            }catch (ActivityNotFoundException e2){
                                Crashlytics.logException(e2);
                                new CustomToast(activity.getString(R.string.NO_GOOGLE_PLAY_FOUND),activity).showToast(true);
                            }
                        }
                    }
                })
                .setNegativeButton(activity.getString(R.string.NO), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })
                .show();

        customAlertDialog.setDialogStyle(dialog);
    }

    public void sendToEnableDownloadManager(final Activity activity){

        CustomAlertDialog customAlertDialog = new CustomAlertDialog(activity);
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setCustomTitle(customAlertDialog.getTitleText(activity.getString(R.string.GUIDE)))
                .setMessage(activity.getString(R.string.ENABLE_DOWNLOAD_MANAGER_GUIDE) + " و " + "\n" +
                        (LanguageFunctions.isRtlLanguage()? activity.getString(R.string.CLICK_ON_ENABLE_DOWNLOAD_MANAGER_PERSIAN)
                                :activity.getString(R.string.CLICK_ON_ENABLE_DOWNLOAD_MANAGER_ENGLISH))
                        + "\n" + activity.getString(R.string.RESTART_YOUR_DEVICE))
                .setCancelable(false)
                .setPositiveButton(activity.getString(R.string.SETTINGS), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        String packageName = "com.android.providers.downloads";
                        try {
                            //Open the specific App Info page:
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.parse("package:" + packageName));
                            activity.startActivity(intent);

                        } catch ( ActivityNotFoundException e ) {
                            new CustomToast(activity.getString(R.string.DOWNLOAD_MANAGER_NOT_FOUND),activity).showToast(true);
                        }
                    }
                })
                .show();
        customAlertDialog.setDialogStyle(dialog);
    }
}
