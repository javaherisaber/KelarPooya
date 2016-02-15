package ir.highteam.ecommercekelar.json;

import android.content.Context;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.gson.JsonObject;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.bundle.BundleLoginResult;
import ir.highteam.ecommercekelar.utile.network.RESTFunctions;

/**
 * Created by Mahdizit on 18/09/2016.
 */
public class JsonLogin {

    public BundleLoginResult checkLogin(Context context, String user, String pass){

        String URL = context.getString(R.string.URL_API_Login);
        String params = "username=" + user + "&password=" + pass;

        JsonObject jsonObject;
        try {

            RESTFunctions jsf = new RESTFunctions(context);
            jsonObject = jsf.getJsonObjectResponse(RESTFunctions.RequestType.POST,URL,params,null);

        } catch (Exception e) {
            Crashlytics.logException(e);
            return null;
        }

        try {
            BundleLoginResult login = new BundleLoginResult();
            login.status = jsonObject.get("status").getAsBoolean();
            if(login.status){
                login.token = jsonObject.get("token").getAsString();
                JsonObject info = jsonObject.get("info").getAsJsonObject();
                login.firstName = info.get("first_name").getAsString();
                login.lastName = info.get("last_name").getAsString();
                login.email = info.get("email").getAsString();
            }
            return login;
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e("Tag ecommerce",e.toString());
            return null;
        }
    }
}