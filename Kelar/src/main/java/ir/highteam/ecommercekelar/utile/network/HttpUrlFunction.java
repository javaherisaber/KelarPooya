package ir.highteam.ecommercekelar.utile.network;

import android.content.Context;
import android.net.http.HttpResponseCache;
import android.os.Build;

import com.crashlytics.android.Crashlytics;

import java.io.File;
import java.io.IOException;

public class HttpUrlFunction {

	private Context context;
	
	public HttpUrlFunction(Context ctx) {
		this.context = ctx;
	}
	
	public void enableHttpCaching()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        {
            try {
              File httpCacheDir = new File(context.getCacheDir()
                      , "http");
              long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
              HttpResponseCache.install(httpCacheDir, httpCacheSize);
            } catch (IOException e) {
                Crashlytics.logException(e);
            }
        }
        else
        {
            File httpCacheDir = new File(context.getCacheDir()
                    , "http");
            try {
                com.integralblue.httpresponsecache.HttpResponseCache.install
                    (httpCacheDir, 10 * 1024 * 1024);
            } catch (IOException e) {
                Crashlytics.logException(e);
            }
        }
    }
	
	
	public void flushHttpCache()
	{
		HttpResponseCache cache = HttpResponseCache.getInstalled();
	       if (cache != null) {
	           cache.flush();
	       }
	}
}
