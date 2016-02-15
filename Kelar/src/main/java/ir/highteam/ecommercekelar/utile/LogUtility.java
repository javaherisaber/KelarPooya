package ir.highteam.ecommercekelar.utile;

import android.content.Context;
import android.os.Environment;

import com.crashlytics.android.Crashlytics;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Mahdizit on 18/08/2016.
 */
public class LogUtility {

    Context context;

    public LogUtility(Context ctx)
    {
        this.context = ctx;
    }

    public void insertLogREST(String url,String type,String elapseTime){

        String logBuilder = getCurrentDateTime() + "#" + url + "#" + type + "#T=" + elapseTime + "\n";
        writeToFile("/YOUR_DIR","LogREST",logBuilder);
    }

    public void writeToFile(final String fileDir,final String fileName,final String data)
    {
        Thread thread = new Thread() {
            @Override
            public void run() {
                // Get the directory for the user's public pictures directory.
                final File path = Environment.getExternalStoragePublicDirectory(Environment.getDataDirectory() + "/"+fileDir+"/");

                // Make sure the path directory exists.
                if(!path.exists())
                {
                    // Make it, if it doesn't exit
                    path.mkdirs();
                }

                final File file = new File(path, fileName+".txt");

                // Save your stream, don't forget to flush() it before closing it.
                try
                {
                    boolean status = file.createNewFile();
                    if (status || file.exists()){

                        FileOutputStream fOut = new FileOutputStream(file,true);
                        OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                        myOutWriter.write(data);

                        myOutWriter.close();

                        fOut.flush();
                        fOut.close();
                    }

                } catch (IOException e) {
                    Crashlytics.logException(e);
                }
            }
        };

        thread.start();
    }

    private String getCurrentDateTime() {
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm:ss", Locale.US);
        return df.format(Calendar.getInstance().getTime());
    }

}
