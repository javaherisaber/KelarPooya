package ir.highteam.ecommercekelar.json;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import com.crashlytics.android.Crashlytics;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.bundle.BundleParcelableUpdate;
import ir.highteam.ecommercekelar.utile.network.RESTFunctions;

public class JsonCheckForUpdate {
    
    public BundleParcelableUpdate CheckForUpdate(Context context){
    	BundleParcelableUpdate upBundle = new BundleParcelableUpdate();
    	upBundle.setTitle("no");
    	try 
    	{
	    	RESTFunctions jsf = new RESTFunctions(context);
	    	JsonArray jsonarray = jsf.getJsonArrayResponse(RESTFunctions.RequestType.POST,
					context.getString(R.string.URL_API_check_for_update)
					,PrepareUrlParameter(context),null);

	    	if(jsonarray != null)
	    	{
	    		JsonObject jsonobject = jsonarray.get(0).getAsJsonObject();
	            
	    		upBundle.setTitle(jsonobject.get("title").getAsString());
	    		if(upBundle.getTitle().equals("update") || upBundle.getTitle().equals("urgent"))
	    		{
	    			upBundle.setUrlAddress(jsonobject.get("address").getAsString());
	    			upBundle.setFileName(jsonobject.get("filename").getAsString());
	    			upBundle.setFileSizeByte(jsonobject.get("filesizebyte").getAsInt());
					upBundle.setVersion(jsonobject.get("version").getAsInt());
                    upBundle.setNote(jsonobject.get("note").getAsString());
	    		}
	    	}
		} catch (Exception e) {
            Crashlytics.logException(e);
		}

    	return upBundle;
    }
    
    private String PrepareUrlParameter(Context context){
		try {

			PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			int version = pInfo.versionCode;
			return "pn=" + context.getPackageName() + "&ver=" + String.valueOf(version);
//			return "pn=testUpdate&ver=1";
//          return "pn=testLock&ver=1";
//			return "pn=testUrgent&ver=1";
		} catch (NameNotFoundException e) {
			Crashlytics.logException(e);
			return "";
		}
    }
}
