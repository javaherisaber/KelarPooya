package ir.highteam.ecommercekelar.downloadmanager;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import java.io.File;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.utile.PrefsFunctions;
import ir.highteam.ecommercekelar.view.CustomToast;

/**
 * Created by Mahdi on 29/05/2016.
 */
public class DownloadBroadCast extends BroadcastReceiver {
    private Context context;
    private String fileName;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        PrefsFunctions prefs = new PrefsFunctions(context);
        long savedDownloadIds = prefs.getDownloadApkId();
        this.fileName = prefs.getDownloadApkFileName();

        Bundle extras = intent.getExtras();
        DownloadManager.Query q = new DownloadManager.Query();
        Long downloaded_id = extras.getLong(DownloadManager.EXTRA_DOWNLOAD_ID);

        if(savedDownloadIds == downloaded_id )
        { // so it is my file that has been completed

            q.setFilterById(downloaded_id);
            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Cursor c = manager.query(q);
            if (c.moveToFirst()) {
                int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                if (status == DownloadManager.STATUS_SUCCESSFUL) {

                    CustomToast toast=new CustomToast(context.getString(R.string.DOWNLOAD_SUCCESSFUL), context);
                    toast.showToast(true);

                    prefs.putDownloadApkId(0);

                    PromptInstallApk();
                }
            }

            c.close();

        }
    }

    private void PromptInstallApk() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile
                        (new File(Environment.getExternalStorageDirectory() + context.getString(R.string.DOWNLOAD_APK_DIR) +"/" + fileName))
                , "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
