package ir.highteam.ecommercekelar.json;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.google.gson.JsonObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.bundle.BundleApiResult;
import ir.highteam.ecommercekelar.bundle.BundleRegister;
import ir.highteam.ecommercekelar.utile.network.RESTFunctions;

/**
 * Created by Mahdizit on 24/09/2016.
 */
public class JsonRegister {

    public BundleApiResult register(Context context,BundleRegister register) {

        String URL = context.getString(R.string.URL_API_register);
        String params;
        try{
            params = "user=" + URLEncoder.encode(register.user,"UTF-8")
                    + "&pass=" + URLEncoder.encode(register.pass,"UTF-8")
                    + "&user_email=" + URLEncoder.encode(register.email,"UTF-8")
                    + "&first_name=" + URLEncoder.encode(register.firstName,"UTF-8")
                    + "&last_name=" + URLEncoder.encode(register.lastName,"UTF-8")
                    + "&phone=" + URLEncoder.encode(register.phone,"UTF-8");
        }catch (UnsupportedEncodingException e){
            Crashlytics.logException(e);
            return null;
        }

        BundleApiResult result = new BundleApiResult();
        JsonObject objectRoot;
        try {

            RESTFunctions jsf = new RESTFunctions(context);
            objectRoot = jsf.getJsonObjectResponse(RESTFunctions.RequestType.POST,URL,params,null);

        } catch (Exception e) {
            Crashlytics.logException(e);
            return null;
        }

        try {

            result.status = objectRoot.get("status").getAsBoolean();
            result.message = objectRoot.get("message").getAsString();

        } catch (Exception e) {
            Crashlytics.logException(e);
            return null;
        }

        return result;
    }
}
